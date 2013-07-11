package org.exoplatform.addons.populator.services;

import org.exoplatform.services.organization.*;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.image.ImageUtils;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.manager.RelationshipManager;
import org.exoplatform.social.core.model.AvatarAttachment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.logging.Logger;

@Named("userService")
@ApplicationScoped
public class UserService {

  OrganizationService organizationService_;
  UserHandler userHandler_;
  IdentityManager identityManager_;
  RelationshipManager relationshipManager_;
  Logger log = Logger.getLogger("UserService");

  private final static String PLATFORM_USERS_GROUP = "/platform/administrators";
  private final static String MEMBERSHIP_TYPE_MANAGER = "*";
  private final static int WIDTH = 200;

  @Inject
  public UserService(OrganizationService organizationService, IdentityManager identityManager, RelationshipManager relationshipManager)
  {
    organizationService_ = organizationService;
    userHandler_ = organizationService_.getUserHandler();
    identityManager_ = identityManager;
    relationshipManager_ = relationshipManager;
  }

  public void createUsers(String username, String fullname) {
    String[] fn = fullname.split(" ");
    createUser("john", "John", "Smith", "john@acme.com", "gtngtn", true);
    createUser("mary", "Mary", "Williams", "mary@acme.com", "gtngtn");
    createUser("james", "James", "Potter", "james@acme.com", "gtngtn");
    createUser("jack", "Jack", "Marker", "jack@acme.com", "gtngtn");
    createUser(username, fn[0], fn[1], "demo@acme.com", "gtngtn");

  }

  private boolean createUser(String username, String firstname, String lastname, String email, String password)
  {
    return createUser(username, firstname, lastname, email, password, false);
  }

  private boolean createUser(String username, String firstname, String lastname, String email, String password, boolean isAdmin)
  {
    Boolean ok = true;

    User user = null;
    try {
      user = userHandler_.findUserByName(username);
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    if (user!=null)
    {
      return true;
    }


    user = userHandler_.createUserInstance(username);
    user.setDisplayName(firstname + " " + lastname);
    user.setEmail(email);
    user.setFirstName(firstname);
    user.setLastName(lastname);
    user.setPassword(password);

    try {
      userHandler_.createUser(user, true);
    } catch (Exception e) {
      log.info(e.getMessage());
      ok = false;
    }

    if (isAdmin)
    {
      // Assign the membership "*:/platform/administrators"  to the created user
      try {
        Group group = organizationService_.getGroupHandler().findGroupById(PLATFORM_USERS_GROUP);
        MembershipType membershipType = organizationService_.getMembershipTypeHandler().findMembershipType(MEMBERSHIP_TYPE_MANAGER);
        organizationService_.getMembershipHandler().linkMembership(user, group, membershipType, true);
      } catch (Exception e) {
        log.warning("Can not assign *:/platform/administrators membership to the created user");
        ok = false;
      }


    }

    return ok;
  }

  public void createRelations(String username)
  {
    Identity idJohn = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "john");
    Identity idMary = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "mary");
    Identity idJames = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "james");
    Identity idJack = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "jack");
    Identity idBenjamin = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, username);

    relationshipManager_.inviteToConnect(idBenjamin, idJohn);
    relationshipManager_.confirm(idJohn, idBenjamin);
    relationshipManager_.inviteToConnect(idBenjamin, idMary);
    relationshipManager_.confirm(idMary, idBenjamin);
    relationshipManager_.inviteToConnect(idJack, idBenjamin);

    relationshipManager_.inviteToConnect(idJames, idJohn);
    relationshipManager_.confirm(idJohn, idJames);
    relationshipManager_.inviteToConnect(idJames, idMary);
    relationshipManager_.confirm(idMary, idJames);
    relationshipManager_.inviteToConnect(idMary, idJohn);
    relationshipManager_.confirm(idJohn, idMary);

  }

  public boolean attachAvatars(String username) {
    boolean ok = true;
    this.saveUserAvatar("john", "eXo-Face-John.png");
    this.saveUserAvatar("jack", "eXo-Face-Jack.png");
    this.saveUserAvatar("james", "eXo-Face-James.png");
    this.saveUserAvatar("mary", "eXo-Face-Mary.png");
    this.saveUserAvatar(username, "eXo-Face-Benjamin.png");

    return ok;
  }

  private void saveUserAvatar(String username, String fileName)
  {
    try
    {

      AvatarAttachment avatarAttachment = Utils.getAvatarAttachment(fileName);
      Profile p = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, username, true).getProfile();
      p.setProperty(Profile.AVATAR, avatarAttachment);
      Map<String, Object> props = p.getProperties();

      // Removes avatar url and resized avatar
      for (String key : props.keySet()) {
        if (key.startsWith(Profile.AVATAR + ImageUtils.KEY_SEPARATOR)) {
          p.removeProperty(key);
        }
      }

      identityManager_.updateProfile(p);

    }
    catch (Exception e)
    {
      log.info(e.getMessage());
    }
  }
}

package org.exoplatform.addons.populator.services;

import org.exoplatform.addons.populator.bean.RelationBean;
import org.exoplatform.addons.populator.bean.UserBean;
import org.exoplatform.services.organization.*;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.image.ImageUtils;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.manager.RelationshipManager;
import org.exoplatform.social.core.model.AvatarAttachment;
import org.exoplatform.webui.exception.MessageException;

import juzu.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named("userService")
@SessionScoped
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

  public void createUsers(List<UserBean> users) {
    for (UserBean user:users)
    {
      createUser(user.getUsername(), user.getPosition(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword(), user.getIsAdmin());
    }
  }

  public boolean attachAvatars(List<UserBean> users) {
    boolean ok = true;
    for (UserBean user:users)
    {
      this.saveUserAvatar(user.getUsername(), user.getAvatar());
    }
    return ok;
  }

  public void createRelations(List<RelationBean> relations)
  {
    for (RelationBean relation:relations)
    {
      Identity idInviting = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, relation.getInviting());
      Identity idInvited = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, relation.getInvited());
      relationshipManager_.inviteToConnect(idInviting, idInvited);
      if (relation.getConfirm())
      {
        relationshipManager_.confirm(idInvited, idInviting);
      }
    }
  }

  private boolean createUser(String username, String position, String firstname, String lastname, String email, String password, boolean isAdmin)
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

    if (!"".equals(position)) {
      Identity identity = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, username, true);
      if (identity!=null) {
        Profile profile = identity.getProfile();
        profile.setProperty(Profile.POSITION, position);
        try {
          identityManager_.updateProfile(profile);
        } catch (MessageException e) {
          e.printStackTrace();
        }
      }
    }

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

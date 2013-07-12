package org.exoplatform.addons.populator.services;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.SpaceUtils;
import org.exoplatform.social.core.space.impl.DefaultSpaceApplicationHandler;
import org.exoplatform.social.core.space.model.Space;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

@Named("spaceService")
@ApplicationScoped
public class SpaceService {

  org.exoplatform.social.core.space.spi.SpaceService spaceService_;
  IdentityManager identityManager_;
  Logger log = Logger.getLogger("SpaceService");

  @Inject
  public SpaceService(org.exoplatform.social.core.space.spi.SpaceService spaceService, IdentityManager identityManager)
  {
    spaceService_ = spaceService;
    identityManager_ = identityManager;
  }

  public void createSpaces(String username)
  {
    createSpace("Public Discussions", "public_discussions", "john");
    createSpace("Bank Project", "bank_project", username);
    createSpace("Marketing Analytics", "marketing_analytics", username);
    createSpace("Human Resources", "human_resources", username);
    createSpace("Help Center", "help_center", "john");
  }

  public void addSpacesAvatars(String username)
  {
    createSpaceAvatar("Public Discussions", "john", "eXo-Space-Public-color.png");
    createSpaceAvatar("Bank Project", username, "eXo-Space-Sales-color.png");
    createSpaceAvatar("Marketing Analytics", username, "eXo-Space-Marketing-color.png");
    createSpaceAvatar("Human Resources", username, "eXo-Space-RH-color.png");
    createSpaceAvatar("Help Center", "john", "eXo-Space-Intranet-color.png");
  }

  public void joinSpaces(String username)
  {
    Space space = spaceService_.getSpaceByDisplayName("Public Discussions");
    if (space!=null)
    {
      spaceService_.addMember(space, username);
      spaceService_.addMember(space, "mary");
      spaceService_.addMember(space, "james");
    }

    space = spaceService_.getSpaceByDisplayName("Bank Project");
    if (space!=null)
    {
      spaceService_.addMember(space, "john");
    }

    space = spaceService_.getSpaceByDisplayName("Human Resources");
    if (space!=null)
    {
      spaceService_.addMember(space, "mary");
    }


  }
    private void createSpaceAvatar(String name, String editor, String avatarFile)
  {
    Space space = spaceService_.getSpaceByDisplayName(name);
    if (space!=null)
    {
      try {
        space.setAvatarAttachment(Utils.getAvatarAttachment(avatarFile));
        spaceService_.updateSpace(space);
        space.setEditor(editor);
        spaceService_.updateSpaceAvatar(space);
      } catch (Exception e) {
        log.info(e.getMessage());
      }
    }
  }

  private void createSpace(String name, String prettyName, String creator)
  {
    Space target = spaceService_.getSpaceByDisplayName(name);
    if (target!=null)
    {
      return;
    }

    Space space = new Space();
//    space.setId(name);
    space.setDisplayName(name);
    space.setPrettyName(prettyName);
    space.setDescription(StringUtils.EMPTY);
    space.setGroupId("/spaces/" + space.getPrettyName());
    space.setRegistration(Space.OPEN);
    space.setVisibility(Space.PRIVATE);
    space.setPriority(Space.INTERMEDIATE_PRIORITY);


    Identity identity = identityManager_.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), true);
    if (identity != null) {
      space.setPrettyName(SpaceUtils.buildPrettyName(space));
    }
    space.setType(DefaultSpaceApplicationHandler.NAME);


    spaceService_.createSpace(space, creator);

  }

}

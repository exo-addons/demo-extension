package org.exoplatform.addons.populator.services;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.addons.populator.bean.SpaceBean;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.SpaceUtils;
import org.exoplatform.social.core.space.impl.DefaultSpaceApplicationHandler;
import org.exoplatform.social.core.space.model.Space;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
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

  public void createSpaces(List<SpaceBean> spaces)
  {
    for (SpaceBean space:spaces)
    {
      createSpace(space.getDisplayName(), space.getPrettyName(), space.getCreator());
      if (space.getMembers()!=null)
      {
        for (String member:space.getMembers())
        {
          Space spacet = spaceService_.getSpaceByDisplayName("Public Discussions");
          if (spacet!=null)
          {
            spaceService_.addMember(spacet, member);
          }
        }
      }
    }
  }

  public void addSpacesAvatars(List<SpaceBean> spaces)
  {
    for (SpaceBean space:spaces)
    {
      createSpaceAvatar(space.getDisplayName(), space.getCreator(), space.getAvatar());
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

package org.exoplatform.addons.populator.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("userService")
@ApplicationScoped
public class SpaceService {

  org.exoplatform.social.core.space.spi.SpaceService spaceService_;

  @Inject
  public SpaceService(org.exoplatform.social.core.space.spi.SpaceService spaceService)
  {
    spaceService_ = spaceService;
  }


}

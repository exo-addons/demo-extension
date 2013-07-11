package org.exoplatform.addons.populator.services;

import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.wiki.mow.api.DraftPage;
import org.exoplatform.wiki.mow.api.Page;
import org.exoplatform.wiki.resolver.TitleResolver;
import org.exoplatform.wiki.service.WikiPageParams;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

@Named("forumService")
@ApplicationScoped
public class ForumService {

  Logger log = Logger.getLogger("ForumService");

  @Inject
  public ForumService()
  {
  }

}

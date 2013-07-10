package org.exoplatform.addons.populator.services;

import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.wiki.resolver.TitleResolver;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

@Named("wikiService")
@ApplicationScoped
public class WikiService {

  org.exoplatform.wiki.service.WikiService wikiService_;
  Logger log = Logger.getLogger("WikiService");

  @Inject
  public WikiService(org.exoplatform.wiki.service.WikiService wikiService)
  {
    wikiService_ = wikiService;
  }

  public void createUserWiki()
  {
    try {
      if (!wikiService_.isExisting(PortalConfig.PORTAL_TYPE, "intranet", TitleResolver.getId("Intranet Usage", false)))
        wikiService_.createPage(PortalConfig.PORTAL_TYPE, "intranet", "Intranet Usage", "WikiHome");
      if (!wikiService_.isExisting(PortalConfig.PORTAL_TYPE, "intranet", TitleResolver.getId("Cloud Management Specification", false)))
        wikiService_.createPage(PortalConfig.PORTAL_TYPE, "intranet", "Cloud Management Specification", "WikiHome");
    } catch (Exception e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

  }

}
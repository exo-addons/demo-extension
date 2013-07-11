package org.exoplatform.addons.populator.services;

import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.wiki.mow.core.api.wiki.PageImpl;
import org.exoplatform.wiki.resolver.TitleResolver;
import org.xwiki.rendering.syntax.Syntax;

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
    createPage("Activity Stream Engagement", "activity-stream-engagement.txt");
    createPage("How-To Guide", "how-to-guide.txt");
    createPage("Leave Planning", "leave-planning.txt");
    editPage("WikiHome", "wiki-home.txt");
  }


  private void createPage(String title, String filename)
  {
    createPage(title, filename, "WikiHome");
  }

  private void createPage(String title, String filename, String parent)
  {
    try {
      if (wikiService_.isExisting(PortalConfig.PORTAL_TYPE, "intranet", TitleResolver.getId(title, false)))
        wikiService_.deletePage(PortalConfig.PORTAL_TYPE, "intranet", TitleResolver.getId(title, false));

      PageImpl page = (PageImpl) wikiService_.createPage(PortalConfig.PORTAL_TYPE, "intranet", title, parent);
      page.getContent().setText(Utils.getWikiPage(filename));
      page.setSyntax(Syntax.XWIKI_2_0.toIdString());
      page.checkin();
      page.checkout();

    } catch (Exception e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

  }

  private void editPage(String title, String filename)
  {
    try {

      PageImpl page = (PageImpl) wikiService_.getPageById(PortalConfig.PORTAL_TYPE, "intranet", TitleResolver.getId(title, false));
      page.getContent().setText(Utils.getWikiPage(filename));
      page.setSyntax(Syntax.XWIKI_2_0.toIdString());
      page.checkin();
      page.checkout();

    } catch (Exception e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

  }
}

package org.exoplatform.addons.populator.services;

import org.exoplatform.addons.populator.bean.WikiBean;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.wiki.mow.core.api.wiki.PageImpl;
import org.exoplatform.wiki.resolver.TitleResolver;
import org.xwiki.rendering.syntax.Syntax;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
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

  public void createUserWiki(List<WikiBean> wikis)
  {
    for (WikiBean wiki:wikis)
    {
      createOrEditPage(wiki, wiki.getParent());
    }
  }


  private void createOrEditPage(WikiBean wikiBean, String parentTitle)
  {
    boolean forceNew = wikiBean.getNew();
    String title= wikiBean.getTitle();
    String filename = wikiBean.getFilename();
    String parent = parentTitle;
    try
    {
      if (forceNew && !title.equals("Wiki Home"))
      {
        if (wikiService_.isExisting(PortalConfig.PORTAL_TYPE, "intranet", TitleResolver.getId(title, false)))
        {
          wikiService_.deletePage(PortalConfig.PORTAL_TYPE, "intranet", TitleResolver.getId(title, false));
        }
      }

      PageImpl page;
      if (wikiService_.isExisting(PortalConfig.PORTAL_TYPE, "intranet", TitleResolver.getId(title, false)))
      {
        page = (PageImpl) wikiService_.getPageById(PortalConfig.PORTAL_TYPE, "intranet", TitleResolver.getId(title, false));
      }
      else
      {
        page = (PageImpl) wikiService_.createPage(PortalConfig.PORTAL_TYPE, "intranet", title, TitleResolver.getId(parent, false));
      }

      String content = "= "+title+" =";
      if (filename!=null)
        content = Utils.getWikiPage(filename);
      page.getContent().setText(content);
      page.setSyntax(Syntax.XWIKI_2_0.toIdString());
      page.checkin();
      page.checkout();

      if (wikiBean.getWikis()!=null && wikiBean.getWikis().size()>0)
      {
        for (WikiBean wiki:wikiBean.getWikis())
        {
          createOrEditPage(wiki, wikiBean.getTitle());
        }
      }


    } catch (Exception e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

  }

}

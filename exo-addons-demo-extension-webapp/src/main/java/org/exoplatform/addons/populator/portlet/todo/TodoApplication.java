package org.exoplatform.addons.populator.portlet.todo;

import juzu.Path;
import juzu.Resource;
import juzu.View;
import juzu.request.RenderContext;
import juzu.template.Template;
import org.exoplatform.addons.populator.portlet.rss.model.Feed;
import org.exoplatform.addons.populator.portlet.rss.model.Item;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.portlet.PortletPreferences;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/** @author <a href="mailto:benjamin.paillereau@exoplatform.com">Benjamin Paillereau</a> */
public class TodoApplication
{

  /** . */
  @Inject
  @Path("index.gtmpl")
  Template indexTemplate;

  /** . */
  @Inject
  @Path("john.gtmpl")
  Template johnTemplate;

  @Inject
  @Path("robert.gtmpl")
  Template robertTemplate;

  @Inject
  @Path("james.gtmpl")
  Template jamesTemplate;

  @Inject
  @Path("mary.gtmpl")
  Template maryTemplate;

  @View
  public void index(RenderContext renderContext) throws IOException
  {
    String remoteUser = renderContext.getSecurityContext().getRemoteUser();

    if ("john".equals(remoteUser))
    {
      johnTemplate.render();
    }
    else if ("robert".equals(remoteUser))
    {
      robertTemplate.render();
    }
    else if ("james".equals(remoteUser))
    {
      jamesTemplate.render();
    }
    else if ("mary".equals(remoteUser))
    {
      maryTemplate.render();
    }
    else
    {
      indexTemplate.render();
    }
  }

}

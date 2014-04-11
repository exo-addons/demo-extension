package org.exoplatform.addons.populator.portlet.todo;

import juzu.Path;
import juzu.View;
import juzu.request.RenderContext;
import juzu.template.Template;

import javax.inject.Inject;
import java.io.IOException;

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

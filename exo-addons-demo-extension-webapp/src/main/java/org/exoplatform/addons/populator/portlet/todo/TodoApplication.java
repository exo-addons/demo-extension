package org.exoplatform.addons.populator.portlet.todo;

import juzu.Path;
import juzu.View;
import juzu.request.RenderContext;
import juzu.template.Template;
import org.exoplatform.addons.populator.services.Utils;

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

    if (Utils.JOHN.equals(remoteUser))
    {
      johnTemplate.render();
    }
    else if (Utils.ROBERT.equals(remoteUser))
    {
      robertTemplate.render();
    }
    else if (Utils.JAMES.equals(remoteUser))
    {
      jamesTemplate.render();
    }
    else if (Utils.MARY.equals(remoteUser))
    {
      maryTemplate.render();
    }
    else
    {
      indexTemplate.render();
    }
  }

}

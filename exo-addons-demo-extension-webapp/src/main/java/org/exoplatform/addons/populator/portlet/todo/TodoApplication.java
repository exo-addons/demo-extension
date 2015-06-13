package org.exoplatform.addons.populator.portlet.todo;

import juzu.Path;
import juzu.Response;
import juzu.View;
import juzu.template.Template;
import org.exoplatform.addons.populator.services.Utils;
import org.exoplatform.web.application.RequestContext;

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
  public Response index() throws IOException
  {
    String remoteUser = RequestContext.getCurrentInstance().getRemoteUser();

    if (Utils.JOHN.equals(remoteUser))
    {
      return johnTemplate.ok();
    }
    else if (Utils.ROBERT.equals(remoteUser))
    {
      return robertTemplate.ok();
    }
    else if (Utils.JAMES.equals(remoteUser))
    {
      return jamesTemplate.ok();
    }
    else if (Utils.MARY.equals(remoteUser))
    {
      return maryTemplate.ok();
    }
    else
    {
      return indexTemplate.ok();
    }
  }

}

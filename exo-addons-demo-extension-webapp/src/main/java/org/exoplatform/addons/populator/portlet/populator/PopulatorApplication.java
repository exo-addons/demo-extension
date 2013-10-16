package org.exoplatform.addons.populator.portlet.populator;

import juzu.*;
import juzu.plugin.ajax.Ajax;
import juzu.template.Template;
import org.exoplatform.addons.populator.services.PopulatorService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.portlet.PortletPreferences;

/** @author <a href="mailto:benjamin.paillereau@exoplatform.com">Benjamin Paillereau</a> */
@SessionScoped
public class PopulatorApplication
{
  /** . */
  @Inject
  @Path("index.gtmpl")
  Template indexTemplate;

  /** . */
  @Inject
  @Path("custom.gtmpl")
  Template customTemplate;


  @Inject
  PopulatorService populatorService_;

  @Inject
  Provider<PortletPreferences> providerPreferences;

  @View
  public Response.Content index(String category)
  {

    PortletPreferences portletPreferences = providerPreferences.get();
    if ("".equals(populatorService_.getUsername()))
    {
      populatorService_.setUsername(portletPreferences.getValue("username", "benjamin"));
      populatorService_.setFullname(portletPreferences.getValue("fullname", "Benjamin Paillereau"));
    }
    populatorService_.init();
    String data = populatorService_.getDataAsString();

    if (category==null) category = "Summary";

//    String size = portletPreferences.getValue("size", "128");
    String[] categories = {"Summary", "Custom"};
    Template target = indexTemplate;
    if ("Custom".equals(category)) {
      target = customTemplate;
    }

    return target.with().set("category", category)
            .set("categories", categories)
            .set("username", populatorService_.getUsername())
            .set("fullname", populatorService_.getFullname())
            .set("data", data)
            .ok();
  }

  @Ajax
  @Resource
  public Response.Content start(String filter)
  {
    if ("1".equals(filter))
      populatorService_.init();
    StringBuilder sb = new StringBuilder() ;
    sb.append("{\"status\": \"OK\"}");

    populatorService_.start(Integer.parseInt(filter));
//    populatorService_.getData();

    return Response.ok(sb.toString()).withMimeType("application/json; charset=UTF-8").withHeader("Cache-Control", "no-cache");
  }

  @Ajax
  @Resource
  public Response.Content save(String username, String fullname, String data)
  {
    populatorService_.setUsername(username);
    populatorService_.setFullname(fullname);
    populatorService_.setData(data);

    StringBuilder sb = new StringBuilder() ;
    sb.append("{\"status\": \"OK\"}");

    return Response.ok(sb.toString()).withMimeType("application/json; charset=UTF-8").withHeader("Cache-Control", "no-cache");
  }

  @Ajax
  @Resource
  public Response.Content elements()
  {
    return Response.ok(populatorService_.getCompletionAsJson()).withMimeType("application/json; charset=UTF-8").withHeader("Cache-Control", "no-cache");
  }


}

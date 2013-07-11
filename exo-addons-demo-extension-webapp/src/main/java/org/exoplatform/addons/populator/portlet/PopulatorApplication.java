package org.exoplatform.addons.populator.portlet;

import juzu.*;
import juzu.plugin.ajax.Ajax;
import juzu.template.Template;
import org.exoplatform.addons.populator.services.PopulatorService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.portlet.PortletPreferences;
import java.util.Random;

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
  @Path("users.gtmpl")
  Template usersTemplate;

  /** . */
  @Inject
  @Path("spaces.gtmpl")
  Template spacesTemplate;


  @Inject
  PopulatorService populatorService_;

  @Inject
  Provider<PortletPreferences> providerPreferences;

  String username, fullname;

  @View
  public Response.Content index(String category)
  {

    PortletPreferences portletPreferences = providerPreferences.get();
    populatorService_.setUsername(portletPreferences.getValue("username", "benjamin"));
    populatorService_.setFullname(portletPreferences.getValue("fullname", "Benjamin Paillereau"));


    if (category==null) category = "Summary";

//    String size = portletPreferences.getValue("size", "128");
    String[] categories = {"Summary", "Users", "Spaces"};
    Template target = indexTemplate;
    if ("Users".equals(category)) {
      target = usersTemplate;
    } else if ("Spaces".equals(category)) {
      target = spacesTemplate;
    }

    return target.with().set("category", category).set("categories", categories).ok();
  }

  @Ajax
  @Resource
  public Response.Content start()
  {
    StringBuilder sb = new StringBuilder() ;
    sb.append("{\"status\": \"OK\"}");

    populatorService_.start();

    return Response.ok(sb.toString()).withMimeType("application/json; charset=UTF-8").withHeader("Cache-Control", "no-cache");
  }

  @Ajax
  @Resource
  public Response.Content elements()
  {
    Random random = new Random();
    StringBuilder sb = new StringBuilder() ;
    sb.append("[");
    sb.append("{\"name\": \"Users\",");
    sb.append("\"percentage\": \""+random.nextInt(100)+"%\"},");
    sb.append("{\"name\": \"Spaces\",");
    sb.append("\"percentage\": \""+random.nextInt(100)+"%\"},");
    sb.append("{\"name\": \"Documents\",");
    sb.append("\"percentage\": \""+random.nextInt(100)+"%\"}");
    sb.append("]");

    return Response.ok(sb.toString()).withMimeType("application/json; charset=UTF-8").withHeader("Cache-Control", "no-cache");
  }


}

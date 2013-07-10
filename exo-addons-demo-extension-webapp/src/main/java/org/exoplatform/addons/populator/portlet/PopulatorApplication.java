package org.exoplatform.addons.populator.portlet;

import juzu.*;
import juzu.plugin.ajax.Ajax;
import juzu.template.Template;
import org.exoplatform.addons.populator.services.CalendarService;
import org.exoplatform.addons.populator.services.SpaceService;
import org.exoplatform.addons.populator.services.UserService;
import org.exoplatform.addons.populator.services.WikiService;

import javax.inject.Inject;
import java.util.Random;

/** @author <a href="mailto:benjamin.paillereau@exoplatform.com">Benjamin Paillereau</a> */
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
  UserService userService_;

  @Inject
  SpaceService spaceService_;

  @Inject
  CalendarService calendarService_;

  @Inject
  WikiService wikiService_;

  @View
  public Response.Content index(String category)
  {
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
    userService_.createUsers();
    userService_.attachAvatars();
    userService_.createRelations();

    spaceService_.createSpaces();
    spaceService_.addSpacesAvatars();

    calendarService_.setCalendarColors();
    calendarService_.createEvents();

//    wikiService_.createUserWiki();

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

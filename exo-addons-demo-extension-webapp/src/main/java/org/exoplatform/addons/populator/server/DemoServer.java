package org.exoplatform.addons.populator.server;

import juzu.*;
import juzu.template.Template;
import org.exoplatform.addons.populator.services.SpaceService;
import org.exoplatform.addons.populator.services.UserService;

import javax.inject.Inject;
import java.util.Random;

/** @author <a href="mailto:benjamin.paillereau@exoplatform.com">Benjamin Paillereau</a> */
public class DemoServer
{
  /** . */
  @Inject
  @Path("index.gtmpl")
  Template indexTemplate;

  /** . */
  @Inject
  @Path("users.gtmpl")
  Template usersTemplate;

  @Inject
  UserService userService_;

  @Inject
  SpaceService spaceService_;

  @View
  public Response.Content index()
  {
    return indexTemplate.ok();
  }

  @Resource
  @Route("/users")
  public Response.Content users()
  {
    return usersTemplate.ok();
  }

  @Resource
  @Route("/start")
  public Response.Content start()
  {
    StringBuilder sb = new StringBuilder() ;
    sb.append("{\"status\": \"OK\"}");
    userService_.createUsers();
    userService_.attachAvatars();

    return Response.ok(sb.toString()).withMimeType("application/json; charset=UTF-8").withHeader("Cache-Control", "no-cache");
  }

  @Resource
  @Route("/elements.json")
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

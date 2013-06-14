package org.exoplatform.addons.populator.server;

import juzu.*;
import juzu.template.Template;
import org.exoplatform.addons.populator.services.UserService;

import javax.inject.Inject;

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
  @Route("/phones.json")
  public Response.Content phones()
  {
    StringBuilder sb = new StringBuilder() ;
    sb.append("[");
    sb.append("{\"name\": \"Nexus S\",");
    sb.append("\"snippet\": \"Fast just got faster with Nexus S.\"},");
    sb.append("{\"name\": \"Motorola XOOM with Wi-Fi\",");
    sb.append("\"snippet\": \"The Next, Next Generation tablet.\"},");
    sb.append("{\"name\": \"MOTOROLA XOOM\",");
    sb.append("\"snippet\": \"The Next, Next Generation tablet.\"}");
    sb.append("]");

    return Response.ok(sb.toString()).withMimeType("application/json; charset=UTF-8").withHeader("Cache-Control", "no-cache");
  }

}

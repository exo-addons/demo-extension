package org.exoplatform.addons.populator.portlet;

import juzu.Path;
import juzu.Response;
import juzu.View;
import juzu.template.Template;
import org.exoplatform.addons.populator.services.UserService;

import javax.inject.Inject;

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

//  @Inject
//  PortletPreferences portletPreferences;

  @Inject
  UserService userService_;

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


}

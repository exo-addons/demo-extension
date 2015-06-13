
@Scripts(
        {
                @Script(value = "js/Chart.min.js", id = "chart",location = AssetLocation.SERVER),
                @Script(value = "js/todo.js", id = "todo", depends = "chart",location = AssetLocation.SERVER)
        }
)
@Stylesheets(
        {
                @Stylesheet(value = "/org/exoplatform/addons/populator/portlet/todo/assets/todo.css", location = AssetLocation.APPLICATION)
        }
)
@Less(value = "todo.less", minify = true)


@Application(defaultController = TodoApplication.class)
@Portlet(name="TodoPortlet")
@Assets("*")
package org.exoplatform.addons.populator.portlet.todo;

import juzu.Application;
import juzu.asset.AssetLocation;
import juzu.plugin.asset.Stylesheets;
import juzu.plugin.asset.Assets;
import juzu.plugin.asset.Script;
import juzu.plugin.asset.Scripts;
import juzu.plugin.asset.Stylesheet;
import juzu.plugin.less.Less;
import juzu.plugin.portlet.Portlet;

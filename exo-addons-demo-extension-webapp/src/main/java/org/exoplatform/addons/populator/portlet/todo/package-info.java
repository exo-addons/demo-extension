@Assets(
        location = AssetLocation.SERVER,
        scripts = {
                @Script(src = "js/Chart.min.js", id = "chart"),
                @Script(src = "js/todo.js", id = "todo", depends = "chart")
        },
        stylesheets = {
                @Stylesheet(src = "/org/exoplatform/addons/populator/portlet/todo/assets/todo.css", location = AssetLocation.APPLICATION)
        }
)
@Less(value = "todo.less", minify = true)


@Application(defaultController = TodoApplication.class)
@Portlet(name="TodoPortlet") package org.exoplatform.addons.populator.portlet.todo;

import juzu.Application;
import juzu.asset.AssetLocation;
import juzu.plugin.asset.Assets;
import juzu.plugin.asset.Script;
import juzu.plugin.asset.Stylesheet;
import juzu.plugin.less.Less;
import juzu.plugin.portlet.Portlet;

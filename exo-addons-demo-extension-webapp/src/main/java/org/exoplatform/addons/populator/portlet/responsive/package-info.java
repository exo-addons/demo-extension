@Assets(
        location = AssetLocation.SERVER,
        scripts = {
                @Script(src = "js/responsive.js", id = "responsive")
        },
        stylesheets = {
                @Stylesheet(src = "/org/exoplatform/addons/populator/portlet/responsive/assets/responsive.css", location = AssetLocation.APPLICATION)
        }
)
@Less(value = "responsive.less", minify = true)


@Application(defaultController = ResponsiveApplication.class)
@Portlet(name="ResponsivePortlet") package org.exoplatform.addons.populator.portlet.responsive;

import juzu.Application;
import juzu.asset.AssetLocation;
import juzu.plugin.asset.Assets;
import juzu.plugin.asset.Script;
import juzu.plugin.asset.Stylesheet;
import juzu.plugin.less.Less;
import juzu.plugin.portlet.Portlet;
import org.exoplatform.addons.populator.portlet.todo.TodoApplication;

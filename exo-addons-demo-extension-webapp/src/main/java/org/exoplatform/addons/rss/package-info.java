@Assets(
        location = AssetLocation.SERVER,
        scripts = {
                @Script(id = "jquery-utils", src = "js/jquery-juzu-utils-0.1.0.js")
        },
        stylesheets = {
                @Stylesheet(src = "/org/exoplatform/addons/rss/assets/rss.css", location = AssetLocation.APPLICATION)
        }
)
@Less(value = "rss.less", minify = true)

@Application
@Portlet package org.exoplatform.addons.rss;

import juzu.Application;
import juzu.asset.AssetLocation;
import juzu.plugin.asset.Assets;
import juzu.plugin.asset.Script;
import juzu.plugin.asset.Stylesheet;
import juzu.plugin.less.Less;
import juzu.plugin.portlet.Portlet;

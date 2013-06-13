@Assets(
        location = AssetLocation.SERVER,
        scripts = {
                @Script(src = "js/jquery-1.8.3.min.js", id = "jquery"),
                @Script(src = "js/angular-1.0.7.min.js", id = "angular", depends = "jquery")
        }
)

@Application(defaultController = Controller.class)
@Portlet(name="PopulatorPortlet") package org.exoplatform.addons.populator;

import juzu.Application;
import juzu.asset.AssetLocation;
import juzu.plugin.asset.Assets;
import juzu.plugin.asset.Script;
import juzu.plugin.portlet.Portlet;


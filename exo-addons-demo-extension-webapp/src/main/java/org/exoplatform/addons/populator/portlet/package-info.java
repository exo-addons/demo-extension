@Assets(
        location = AssetLocation.SERVER,
        scripts = {
                @Script(src = "js/angular-1.0.7.min.js", id = "angular"),
                @Script(src = "js/demo-extension.js", id = "demo", depends = "angular")
        }
)

@Application(defaultController = PopulatorApplication.class)
@Portlet(name="PopulatorPortlet") package org.exoplatform.addons.populator.portlet;

import juzu.Application;
import juzu.asset.AssetLocation;
import juzu.plugin.asset.Assets;
import juzu.plugin.asset.Script;
import juzu.plugin.portlet.Portlet;


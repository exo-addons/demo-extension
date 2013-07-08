@Application(defaultController = DemoServer.class)

@Bindings(
        {
                @Binding(value = org.exoplatform.services.organization.OrganizationService.class),
                @Binding(value = org.exoplatform.social.core.space.spi.SpaceService.class)
        }
)

package org.exoplatform.addons.populator.server;

import juzu.Application;
import juzu.plugin.binding.Binding;
import juzu.plugin.binding.Bindings;


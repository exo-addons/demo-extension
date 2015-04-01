package org.exoplatform.addons.uiexplorer.extention.webui.component;

import org.exoplatform.addons.uiexplorer.extention.webui.form.UIShareSettings;
import org.exoplatform.ecm.webui.component.explorer.UIJCRExplorer;
import org.exoplatform.ecm.webui.component.explorer.control.filter.*;
import org.exoplatform.ecm.webui.component.explorer.control.listener.UIActionBarActionListener;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIPopupContainer;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.ext.filter.UIExtensionFilter;
import org.exoplatform.webui.ext.filter.UIExtensionFilters;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kmenzli on 18/03/15.
 */
@ComponentConfig(
        events = {
                @EventConfig(listeners = SharePublicLinkActionComponent.SharePublicLinkActionListener.class)
        }
)
public class SharePublicLinkActionComponent extends UIComponent {
    private static final Log LOG  = ExoLogger.getLogger(SharePublicLinkActionComponent.class.getName());

    private static final List<UIExtensionFilter> FILTERS = Arrays.asList(new UIExtensionFilter[]{
            new IsNotRootNodeFilter("UIActionBar.msg.cannot-enable-version-rootnode"),
            new CanSetPropertyFilter(), new IsNotLockedFilter(), new CanEnableVersionFilter(),
            new IsNotEditingDocumentFilter(), new IsNotFolderFilter(), new IsNotIgnoreVersionNodeFilter()
    });

    @UIExtensionFilters
    public List<UIExtensionFilter> getFilters() {
        return FILTERS;
    }

    public static class SharePublicLinkActionListener extends UIActionBarActionListener<SharePublicLinkActionComponent> {
        protected void processEvent(Event<SharePublicLinkActionComponent> event) throws Exception {

            UIJCRExplorer uiExplorer = event.getSource().getAncestorOfType(UIJCRExplorer.class);
            UIPopupContainer UIPopupContainer = uiExplorer.getChild(UIPopupContainer.class);
            UIPopupContainer.activate(UIShareSettings.class, 700);
            event.getRequestContext().addUIComponentToUpdateByAjax(UIPopupContainer);

        }
    }
}

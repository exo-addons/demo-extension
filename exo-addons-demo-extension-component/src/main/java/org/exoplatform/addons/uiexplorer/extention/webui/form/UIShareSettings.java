package org.exoplatform.addons.uiexplorer.extention.webui.form;

import org.exoplatform.ecm.webui.component.explorer.UIJCRExplorer;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by kmenzli on 19/03/15.
 */
@ComponentConfig(
        type = UIShareSettings.class,
        template = "classpath:groovy/addons/explorer/extension/webui/popup/UIShareSettings.gtmpl",
        events = {
                @EventConfig(listeners = UIShareSettings.CloseActionListener.class)
        }
)
public class UIShareSettings extends UIContainer implements UIPopupComponent {

    private Node currentNode;

    private String publicURL;

    public String getPublicURL() {
        return publicURL;
    }

    private void init () throws Exception {
        HttpServletRequest request = Util.getPortalRequestContext().getRequest();
        String baseURI = request.getScheme() + "://" + request.getServerName() + ":"+ String.format("%s", request.getServerPort());
        publicURL = baseURI+ "/portal/share/file/" +Util.getPortalRequestContext().getRemoteUser()+"/"+currentNode.getUUID()+"/"+currentNode.getName();
    }


    public void activate() {

        UIJCRExplorer uiExplorer = getAncestorOfType(UIJCRExplorer.class);
        try {
            currentNode = uiExplorer.getCurrentNode();
            init();
        } catch (Exception E) {
            return ;

        }

    }
    public void deActivate() {}

    static  public class CloseActionListener extends EventListener<UIShareSettings> {
        public void execute(Event<UIShareSettings> event) throws Exception {
            UIJCRExplorer uiExplorer = event.getSource().getAncestorOfType(UIJCRExplorer.class) ;
            uiExplorer.cancelAction() ;
        }
    }

}

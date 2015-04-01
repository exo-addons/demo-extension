package org.exoplatform.addons.uiexplorer.extention.webui.form;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.ecm.webui.component.explorer.UIJCRExplorer;
import org.exoplatform.ecm.webui.component.explorer.UIJCRExplorerPortlet;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.wcm.utils.WCMCoreUtils;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.UIPopupContainer;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormInputBase;
import org.exoplatform.webui.form.input.UICheckBoxInput;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by kmenzli on 27/03/15.
 */
@ComponentConfig(lifecycle = UIFormLifecycle.class,
        template = "classpath:groovy/addons/explorer/extension/webui/popup/UISpaceListForm.gtmpl",
        events = {
                @EventConfig(listeners = UISpaceListForm.SaveActionListener.class),
                @EventConfig(phase= Event.Phase.DECODE, listeners = UISpaceListForm.CancelActionListener.class) })

public class UISpaceListForm extends UIForm implements UIPopupComponent {

    private static final Log LOG  = ExoLogger.getLogger(UISpaceListForm.class.getName());

    private SpaceService spaceService = null;

    private ListAccess<Space> spaceListAccess;

    private LinkedList<Space> spaceList = new LinkedList<Space>();

    private String remoteUser = null;

    public void activate() {

        WebuiRequestContext requestContext = WebuiRequestContext.getCurrentInstance();
        remoteUser = requestContext.getRemoteUser();


        try {
            spaceService = ((SpaceService)getApplicationComponent(SpaceService.class));

            if (spaceService != null) {
                this.spaceListAccess = spaceService.getLastAccessedSpace(remoteUser, null);
            }
            if (spaceListAccess != null) {
                this.spaceList.addAll(Arrays.asList(spaceListAccess.load(0,5)));
            }

        } catch (Exception exception) {
            LOG.error("SpaceService could be 'null' when the Social profile isn't activated ", exception);
        }


        for (Space space : spaceList) {
            //--- Get the space URL using reouter API
            String spaceURL = Util.getPortalRequestContext().getRequest().getRequestURL().toString();
            if((spaceURL!=null)&&(!spaceURL.contains(space.getPrettyName()))) {
                addUIFormInput(new UICheckBoxInput(space.getDisplayName(), space.getShortName(), null));
            }

        }
        setActions(new String[]{"Save", "Cancel"});
    }

    public void deActivate() {
    }

    public static class SaveActionListener extends EventListener<UISpaceListForm> {

        public void execute(Event<UISpaceListForm> event) throws Exception {

            UISpaceListForm uiForm = event.getSource();
            //--- Get Repository Service
            RepositoryService repositoryService = (RepositoryService)uiForm.getApplicationComponent(RepositoryService.class);

            ManageableRepository manageableRepository = repositoryService.getCurrentRepository();
            Session targetSession = WCMCoreUtils.getSystemSessionProvider().getSession("collaboration", manageableRepository);


            //--- The current Node
            Node currentNode = null;


            UIJCRExplorerPortlet uiExplorerPorltet = uiForm.getAncestorOfType(UIJCRExplorerPortlet.class);
            UIJCRExplorer uiExplorer = uiExplorerPorltet.findFirstComponentOfType(UIJCRExplorer.class);
            LinkedList<String> selectedSpace = new LinkedList<String>();

            for (UIComponent checkBox : uiForm.getChildren()) {
                if (checkBox instanceof UIFormInputBase) {
                    if ((Boolean)((UIFormInputBase) checkBox).getValue()){
                        selectedSpace.add(((UIFormInputBase) checkBox).getBindingField());
                    }

                }
            }

            //--- Get The current node
            currentNode = uiExplorer.getCurrentNode();

            for (String spacePrettyName : selectedSpace) {

                if (targetSession.getWorkspace().getName().equals(currentNode.getSession().getWorkspace().getName())) {


                    //--- Use .move cut/past documents
                    //targetSession.getWorkspace().move(currentNode.getPath(),String.format("/Groups/spaces/%s/Documents/%s",spacePrettyName,currentNode.getName()));

                    //--- Use move to copy/past documents
                    targetSession.getWorkspace().copy(currentNode.getPath(), String.format("/Groups/spaces/%s/Documents/%s", spacePrettyName, currentNode.getName()));

                    targetSession.save();

                } else {
                    try {

                        targetSession.getWorkspace().copy("collaboration", currentNode.getPath(), String.format("/Groups/spaces/%s/Documents/%s", spacePrettyName, currentNode.getName()));
                        // workspace.clone(srcWorkspaceName, srcPath, destPath, true);
                    } catch (Exception e) {

                        try {
                            targetSession.getWorkspace().clone("collaboration", currentNode.getPath(), String.format("/Groups/spaces/%s/Documents/%s", spacePrettyName, currentNode.getName()), false);
                        } catch (Exception f) {
                            if (LOG.isErrorEnabled()) {
                                LOG.error("an unexpected error occurs while pasting the node", f);
                            }
                        }
                    }
                }

            }

            uiExplorer.refreshExplorer();
            uiExplorerPorltet.setRenderedChild(UIJCRExplorer.class);

        }
    }

    public static class CancelActionListener extends EventListener<UISpaceListForm> {
        public void execute(Event<UISpaceListForm> event) throws Exception {
            event.getSource().getAncestorOfType(UIPopupContainer.class).cancelPopupAction() ;
        }
    }
}

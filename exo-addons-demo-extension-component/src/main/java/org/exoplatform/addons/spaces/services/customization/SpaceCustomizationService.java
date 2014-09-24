package org.exoplatform.addons.spaces.services.customization;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.portal.config.DataStorage;
import org.exoplatform.portal.config.UserACL;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.*;
import org.exoplatform.portal.pom.spi.portlet.Portlet;
import org.exoplatform.portal.pom.spi.portlet.Preference;
import org.exoplatform.services.cms.impl.DMSConfiguration;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.space.SpaceException;
import org.exoplatform.social.core.space.SpaceUtils;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

import java.util.List;

/**
 * Created by kmenzli on 12/09/2014.
 */
public class SpaceCustomizationService {

    private static final Log LOG = ExoLogger.getExoLogger(SpaceCustomizationService.class);

    final static private String GROUPS_PATH = "groupsPath";
    private static final String SPACE_GROUP_ID_PREFERENCE = "{spaceGroupId}";
    private static final String SPACE_NEW_HOME_PAGE_TEMPLATE = "demo space";
    private static final String SCV_PORTLEt_NAME = "SingleContentViewer";

    private NodeHierarchyCreator nodeHierarchyCreator = null;
    private DMSConfiguration dmsConfiguration = null;
    private RepositoryService repositoryService = null;
    private ConfigurationManager configurationManager = null;
    private DataStorage dataStorageService = null;
    private UserPortalConfigService userPortalConfigService = null;
    private SpaceService spaceService = null;
    private UserACL userACL = null;
    private String groupsPath;

    public SpaceCustomizationService(DataStorage dataStorageService_,
                                     UserPortalConfigService userPortalConfigService_,
                                     NodeHierarchyCreator nodeHierarchyCreator_,
                                     DMSConfiguration dmsConfiguration_,
                                     RepositoryService repositoryService_,
                                     ConfigurationManager configurationManager_, UserACL userACL_) {

        this.nodeHierarchyCreator = nodeHierarchyCreator_;
        this.dmsConfiguration = dmsConfiguration_;
        this.repositoryService = repositoryService_;
        this.userACL = userACL_;
        this.configurationManager = configurationManager_;
        this.dataStorageService = dataStorageService_;
        this.userPortalConfigService = userPortalConfigService_;
        groupsPath = nodeHierarchyCreator.getJcrPath(GROUPS_PATH);

        if (groupsPath.lastIndexOf("/") == groupsPath.length() - 1) {
            groupsPath = groupsPath.substring(0, groupsPath.lastIndexOf("/"));
        }
    }

    public void createSpaceHomePage(String spacePrettyName, String spaceGroupId) {

        RequestLifeCycle.begin(PortalContainer.getInstance());
        try {
            LOG.info("Updating '" + spaceGroupId + "' Space Home Page");
            // creates the new home page

            Page oldSpaceHomePage = dataStorageService.getPage(PortalConfig.GROUP_TYPE + "::" + spaceGroupId + "::"+ getSpaceService().getSpaceApplicationConfigPlugin().getHomeApplication().getPortletName());

            Page customSpaceHomePage = userPortalConfigService.createPageTemplate(SPACE_NEW_HOME_PAGE_TEMPLATE,PortalConfig.GROUP_TYPE, spaceGroupId);
            customSpaceHomePage.setTitle(oldSpaceHomePage.getTitle());
            customSpaceHomePage.setName(oldSpaceHomePage.getName());

            //TODO I don't know why old access and edit permissions are empty !?!?
            customSpaceHomePage.setAccessPermissions(new String[]{"*:" + spaceGroupId});
            customSpaceHomePage.setEditPermission("manager:" + spaceGroupId);
            customSpaceHomePage.setOwnerType(PortalConfig.GROUP_TYPE);
            customSpaceHomePage.setOwnerId(spaceGroupId);
            // needs to populate the accessPermissions list to all children:
            // containers and applications
            editChildrenAccesPermisions(customSpaceHomePage.getChildren(), new String[]{"*:" + spaceGroupId});
            editSpaceURLPreference(customSpaceHomePage.getChildren(), spacePrettyName);
            dataStorageService.save(customSpaceHomePage);

        } catch (Exception e) {
            LOG.error("Error while customizing the Space home page for space: " + spaceGroupId, e);
        } finally {
            try {
                RequestLifeCycle.end();
            } catch (Exception e) {
                LOG.warn("An exception has occurred while proceed RequestLifeCycle.end() : " + e.getMessage());
            }
        }
    }

    public void createSpaceApplication (Space targetSpace,String targetSpaceName, String targetAppName) {

        //Fake to add MyCRM application to
        if (targetSpaceName.equalsIgnoreCase(targetSpace.getPrettyName())) {

            if (!SpaceUtils.isInstalledApp(targetSpace,targetAppName)) {

                try {

                    spaceService.installApplication(targetSpace.getId(), targetAppName);

                    spaceService.activateApplication(targetSpace.getId(), targetAppName);

                } catch (SpaceException E) {
                    LOG.error("####### Error to install My CRM Application",E);
                }

            }

        }

    }

    private void editSpaceURLPreference(List<ModelObject> children, String prefValue) throws Exception {
        if (children == null || children.size() == 0) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Can not get a portlet application from children.\nChildren == null or have no items");
            }
        }
        // parses the children list
        for (ModelObject modelObject : children) {
            // if a container, check for its children
            if (modelObject instanceof Container) {
                editSpaceURLPreference(((Container) modelObject).getChildren(), prefValue);
            } else {
                // if a portlet application, set the preference value
                if (modelObject instanceof Application && ((Application<?>) modelObject).getType().equals(ApplicationType.PORTLET)) {
                    Application<Portlet> application = (Application<Portlet>) modelObject;
                    Portlet portletPreference = dataStorageService.load(application.getState(), ApplicationType.PORTLET);
                    if (portletPreference == null) {
                        portletPreference = new Portlet();
                    }
                    portletPreference.putPreference(new Preference(SpaceUtils.SPACE_URL, prefValue, false));

                }
            }
        }
    }

    private void editChildrenAccesPermisions(List<ModelObject> children, String[] accessPermissions) {
        if (children != null && children.size() > 0) {
            for (ModelObject modelObject : children) {
                if (modelObject instanceof Container) {
                    ((Container) modelObject).setAccessPermissions(accessPermissions);
                    editChildrenAccesPermisions(((Container) modelObject).getChildren(), accessPermissions);
                } else {
                    if (modelObject instanceof Application && ((Application<?>) modelObject).getType().equals(ApplicationType.PORTLET)) {
                        Application<Portlet> application = (Application<Portlet>) modelObject;
                        application.setAccessPermissions(accessPermissions);
                    }
                }
            }
        }
    }

    public SpaceService getSpaceService() {
        if (this.spaceService == null) {
            this.spaceService = (SpaceService) PortalContainer.getInstance().getComponentInstanceOfType(SpaceService.class);
        }
        return this.spaceService;
    }


}

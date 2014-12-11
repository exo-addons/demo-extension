package org.exoplatform.addons.spaces.services.customization.listener;

import org.exoplatform.addons.spaces.services.customization.SpaceCustomizationService;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.space.SpaceListenerPlugin;
import org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent;
import org.exoplatform.social.core.space.spi.SpaceService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kmenzli on 12/09/2014.
 */
public class CustomizeSpaceHomePageListener extends SpaceListenerPlugin {

    private static final Log LOG = ExoLogger.getExoLogger(CustomizeSpaceHomePageListener.class);

    private SpaceCustomizationService spaceCustomizationService = null;
    private SpaceService spaceService = null;
    private Map<String, Boolean> spaceIds = new HashMap<String, Boolean>();

    private static final String TARGET_SPACE_APPLIACTION_NAME = "MyCrmApplication";
    private static final String TARGET_SPACE__NAME = "sales";

    public CustomizeSpaceHomePageListener(SpaceCustomizationService spaceCustomizationService_, InitParams params, SpaceService spaceService_) {

        this.spaceCustomizationService = spaceCustomizationService_;
        this.spaceService = spaceService_;
    }

    @Override
    public void spaceCreated(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent){

        spaceIds.put(spaceLifeCycleEvent.getSpace().getGroupId(), true);
        String spaceGroupId = spaceLifeCycleEvent.getSpace().getGroupId();
        Boolean spaceCreated = spaceIds.get(spaceGroupId);
        if (spaceCreated == null || !spaceCreated) {
            return;
        }

        spaceIds.put(spaceGroupId, false);
        String spacePrettyName = spaceLifeCycleEvent.getSpace().getPrettyName();
        // Create the space HomePage
        spaceCustomizationService.createSpaceHomePage(spacePrettyName, spaceGroupId);

    }

    @Override
    public void spaceRemoved(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent) {

    }

    @Override
    public void applicationActivated(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent){
    }

    @Override
    public void applicationAdded(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent){


    }

    @Override
    public  void applicationDeactivated(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent){

    }

    @Override
    public  void applicationRemoved(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent) {

    }

    @Override
    public  void grantedLead(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent) {

    }

    @Override
    public  void joined(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent) {

        spaceCustomizationService.createSpaceApplication(spaceLifeCycleEvent.getSpace(),TARGET_SPACE__NAME,TARGET_SPACE_APPLIACTION_NAME);



    }

    @Override
    public  void left(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent) {

    }

    @Override
    public  void revokedLead(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent) {

    }

    @Override
    public  void spaceRenamed(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent) {

    }

    @Override
    public  void spaceDescriptionEdited(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent) {

    }

    @Override
    public  void spaceAvatarEdited(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent) {

    }
    @Override
    public void spaceAccessEdited(SpaceLifeCycleEvent event) {

    }
    @Override
    public void addInvitedUser(SpaceLifeCycleEvent var1){

    }

    @Override
    public void addPendingUser(SpaceLifeCycleEvent var1) {

    }
}

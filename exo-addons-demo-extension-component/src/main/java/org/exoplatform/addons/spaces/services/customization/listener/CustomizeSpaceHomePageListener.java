package org.exoplatform.addons.spaces.services.customization.listener;

import org.exoplatform.addons.spaces.services.customization.SpaceCustomizationService;
import org.exoplatform.container.xml.InitParams;

import org.exoplatform.social.core.space.SpaceListenerPlugin;
import org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kmenzli on 12/09/2014.
 */
public class CustomizeSpaceHomePageListener extends SpaceListenerPlugin {

    private SpaceCustomizationService spaceCustomizationService = null;
    private Map<String, Boolean> spaceIds = new HashMap<String, Boolean>();

    public CustomizeSpaceHomePageListener(SpaceCustomizationService spaceCustomizationService_, InitParams params) {
        this.spaceCustomizationService = spaceCustomizationService_;
    }

    @Override
    public void spaceCreated(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent){

        spaceIds.put(spaceLifeCycleEvent.getSpace().getGroupId(), true);

    }

    @Override
    public void spaceRemoved(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent) {

    }

    @Override
    public void applicationActivated(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent){

    }

    @Override
    public void applicationAdded(org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent spaceLifeCycleEvent){
        String spaceGroupId = spaceLifeCycleEvent.getSpace().getGroupId();
        Boolean spaceCreated = spaceIds.get(spaceGroupId);
        if (spaceCreated == null || !spaceCreated) {
            return;
        }

        spaceIds.put(spaceGroupId, false);
        String spacePrettyName = spaceLifeCycleEvent.getSpace().getPrettyName();
        spaceCustomizationService.createSpaceHomePage(spacePrettyName, spaceGroupId);

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


}

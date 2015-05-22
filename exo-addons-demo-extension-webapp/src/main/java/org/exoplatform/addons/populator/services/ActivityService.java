package org.exoplatform.addons.populator.services;

import juzu.SessionScoped;
import org.exoplatform.addons.populator.bean.ActivityBean;
import org.exoplatform.social.common.RealtimeListAccess;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.activity.model.ExoSocialActivityImpl;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@Named("activityService")
@SessionScoped
public class ActivityService {

  Logger log = Logger.getLogger("ActivityService");

  ActivityManager activityManager_;
  IdentityManager identityManager_;

  Random random = new Random();

  @Inject
  public ActivityService(ActivityManager activityManager, IdentityManager identityManager)
  {
    activityManager_ = activityManager;
    identityManager_ = identityManager;
  }

  public void pushActivities(List<ActivityBean> activities) throws Exception
  {
    for (ActivityBean activity:activities)
    {
      pushActivity(activity);
    }

    likeRandomActivities(Utils.MARY);
    likeRandomActivities(Utils.JAMES);
  }

  private void pushActivity(ActivityBean activityBean) throws Exception
  {
    String from = activityBean.getFrom();
//    String provider = SpaceIdentityProvider.NAME;
    Identity identity = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, from, false);
    ExoSocialActivity activity = new ExoSocialActivityImpl();
    activity.setBody(activityBean.getBody());
    activity.setTitle(activityBean.getBody());
    activity.setUserId(identity.getId());
    activity.setType("DEFAULT_ACTIVITY");
    activity = activityManager_.saveActivity(identity, activity);
/*
    Thread.sleep(500);
    for (String like:activityBean.getLikes())
    {
      Identity identityLike = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, like, false);
      try {
        activityManager_.saveLike(activity, identityLike);
      } catch (Exception e) {
        log.info("Error when liking an activity with "+like);
      }
    }
*/
    for (ActivityBean commentBean:activityBean.getComments())
    {
      Thread.sleep(1000);
      Identity identityComment = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, commentBean.getFrom(), false);
      ExoSocialActivity comment = new ExoSocialActivityImpl();
      comment.setTitle(commentBean.getBody());
      comment.setUserId(identityComment.getId());
      activityManager_.saveComment(activity, comment);
    }

  }

  private void likeRandomActivities (String username)
  {
    Identity identity = identityManager_.getOrCreateIdentity(OrganizationIdentityProvider.NAME, username, false);
    RealtimeListAccess rtla = activityManager_.getActivitiesWithListAccess(identity);
    ExoSocialActivity[] la = (ExoSocialActivity[])rtla.load(0, rtla.getSize());
    for (int iam = 0; iam<la.length ; iam++)
    {
      ExoSocialActivity activityMary = la[iam];
      boolean like = random.nextBoolean();
      if (like)
      {
        activityManager_.saveLike(activityMary, identity);
      }
    }
    rtla = activityManager_.getActivitiesOfUserSpacesWithListAccess(identity);
    la = (ExoSocialActivity[])rtla.load(0, rtla.getSize());
    for (int iam = 0; iam<la.length ; iam++)
    {
      ExoSocialActivity activityMary = la[iam];
      boolean like = random.nextBoolean();
      if (like)
      {
        activityManager_.saveLike(activityMary, identity);
      }
    }

  }

}

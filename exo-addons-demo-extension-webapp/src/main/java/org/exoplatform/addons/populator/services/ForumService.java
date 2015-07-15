package org.exoplatform.addons.populator.services;

import org.exoplatform.forum.common.CommonUtils;
import org.exoplatform.forum.common.jcr.KSDataLocation;
import org.exoplatform.forum.common.jcr.PropertyReader;
import org.exoplatform.forum.service.*;
import org.exoplatform.forum.service.Utils;
import org.exoplatform.poll.service.Poll;
import org.exoplatform.poll.service.PollService;

import juzu.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Named("forumService")
@SessionScoped
public class ForumService {

  Logger log = Logger.getLogger("ForumService");
  org.exoplatform.forum.service.ForumService forumService_;
  PollService pollService_;
  KSDataLocation locator_;

  @Inject
  public ForumService(org.exoplatform.forum.service.ForumService forumService, PollService pollService, KSDataLocation locator)
  {
    forumService_ = forumService;
    pollService_ = pollService;
    locator_ = locator;
  }

  public void createCategoriesAndForum()
  {
    List<Category> categories = forumService_.getCategories();
    for (Category category:categories)
    {
//      log.info("CATEGORY::"+category.getCategoryName()+" : "+category.getId());
      try
      {
        List<Forum> forums = forumService_.getForums(category.getId(), "");
        for (Forum forum:forums)
        {
//          log.info("FORUM::"+forum.getForumName()+" : "+forum.getId());
        }
      } catch (Exception e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
    }
  }

  public void createPosts(String username)
  {
    String forumName = "Public Discussions";
    try {
      Forum forum = getForumByName(forumName);
      Category cat = getCategoryByForumName(forumName);

      List<Topic> topics = forumService_.getTopics(cat.getId(), forum.getId());
      if (topics.size()>0) return;

      Topic topicNew = new Topic();
      topicNew.setOwner(username);
      topicNew.setTopicName("General");
      topicNew.setCreatedDate(new Date());
      topicNew.setModifiedBy(username);
      topicNew.setModifiedDate(new Date());
      topicNew.setLastPostBy(username);
      topicNew.setLastPostDate(new Date());
      topicNew.setDescription("General Topic");
      topicNew.setPostCount(0);
      topicNew.setViewCount(0);
      topicNew.setIsNotifyWhenAddPost("");
      topicNew.setIsModeratePost(false);
      topicNew.setIsClosed(false);
      topicNew.setIsLock(false);
      topicNew.setIsWaiting(false);
      topicNew.setIsActive(true);
      topicNew.setIcon("classNameIcon");
      topicNew.setIsApproved(true);
      topicNew.setCanView(new String[] {});
      topicNew.setCanPost(new String[] {});

      forumService_.saveTopic(cat.getId(), forum.getId(), topicNew, true, false, new MessageBuilder());
    } catch (Exception e) {}
  }

  public void createPollAndVote()
  {
    String forumName = "Public Discussions";
    try {
      List<Poll> polls = pollService_.getPagePoll();
      for (Poll poll:polls)
      {
        pollService_.removePoll(poll.getId());
      }

      Forum forum = getForumByName(forumName);
      Category cat = getCategoryByForumName(forumName);

      List<Topic> topics = forumService_.getTopics(cat.getId(), forum.getId());
      if (topics.size()>0) {
        Topic topic = topics.get(0);

        String[] options = {"It's amazing", "I love it", "I like it", "No opinion"};
        String[] votes = {"50.0", "33.333336", "16.666668", "0.0"};
        String[] userVotes = {org.exoplatform.addons.populator.services.Utils.JAMES+":2:0", org.exoplatform.addons.populator.services.Utils.JOHN+":1:0", org.exoplatform.addons.populator.services.Utils.MARY+":1:0"};
        Poll poll = new Poll();
        String pollPath = forum.getPath() + CommonUtils.SLASH + topic.getId();
        String pollId = topic.getId().replace(Utils.TOPIC, Utils.POLL);
        poll.setId(pollId);
        poll.setParentPath(pollPath);
        poll.setInTopic(true);
        poll.setQuestion("Do you like our new Intranet?");
        poll.setOption(options);
        poll.setOwner(org.exoplatform.addons.populator.services.Utils.MARY);
        poll.setIsMultiCheck(true);
        poll.setShowVote(true);
        poll.setIsAgainVote(true);
        poll.setIsClosed(false);
        poll.setTimeOut(0);

        pollService_.savePoll(poll, true, false);

        poll.setVote(votes);
        poll.setUserVote(userVotes);
        poll.setModifiedBy(org.exoplatform.addons.populator.services.Utils.MARY);
        pollService_.savePoll(poll, true, true);
      }


      } catch (Exception e) {}
  }

  private Forum getForumByName(String forumName) throws Exception {
    StringBuffer sb = new StringBuffer(Utils.JCR_ROOT);
    sb.append("/").append(locator_.getForumCategoriesLocation()).append("//element(*,");
    sb.append(Utils.EXO_FORUM).append(")[jcr:like(exo:name, '%").append(forumName).append("%')]");

    NodeIterator iter =  forumService_.search(sb.toString());
    if (iter.hasNext()) {
      Node forumNode = (Node)iter.next();

      Forum forum = new Forum();
      PropertyReader reader = new PropertyReader(forumNode);
      forum.setId(forumNode.getName());
      forum.setPath(forumNode.getPath());
      forum.setOwner(reader.string(Utils.EXO_OWNER));
      forum.setForumName(reader.string(Utils.EXO_NAME));
      forum.setViewer(reader.strings(Utils.EXO_VIEWER));

      return forum;
    }


    return null;
  }

  private Category getCategoryByForumName(String forumName) throws Exception {
    StringBuffer sb = new StringBuffer(Utils.JCR_ROOT);
    sb.append("/").append(locator_.getForumCategoriesLocation()).append("//element(*,");
    sb.append(Utils.EXO_FORUM).append(")[jcr:like(exo:name, '%").append(forumName).append("%')]");

    NodeIterator iter =  forumService_.search(sb.toString());
    if (iter.hasNext()) {
      Node forumNode = (Node)iter.next();
      if (forumNode.getParent() != null) {
        Node cateNode =  forumNode.getParent();
        Category cat = new Category(cateNode.getName());
        cat.setPath(cateNode.getPath());
        PropertyReader reader = new PropertyReader(cateNode);
        cat.setOwner(reader.string(Utils.EXO_OWNER));
        cat.setCategoryName(reader.string(Utils.EXO_NAME));
        return cat;
      }
    }

    return null;
  }
}

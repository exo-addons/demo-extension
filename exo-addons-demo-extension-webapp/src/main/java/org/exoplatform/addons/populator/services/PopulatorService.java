package org.exoplatform.addons.populator.services;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PopulatorService {

  Logger log = Logger.getLogger("PopulatorService");

  @Inject
  UserService userService_;

  @Inject
  SpaceService spaceService_;

  @Inject
  CalendarService calendarService_;

  @Inject
  WikiService wikiService_;

  @Inject
  ForumService forumService_;

  @Inject
  DocumentService documentService_;

  @Inject
  ActivityService activityService_;

  String currentState_;

  String username, fullname;

  Map<String, Integer> completion = new HashMap<String, Integer>();

  private final String USERS = "Users";
  private final String SPACES = "Spaces";
  private final String CALENDAR = "Calendar";
  private final String WIKI = "Wiki";
  private final String DOCUMENTS = "Documents";
  private final String FORUM = "Forum";
  private final String ACTIVITIES = "Activities";

  public PopulatorService()
  {
    init();
  }

  public void init()
  {
    completion.put(USERS, 0);
    completion.put(SPACES, 0);
    completion.put(CALENDAR, 0);
    completion.put(WIKI, 0);
    completion.put(DOCUMENTS, 0);
    completion.put(FORUM, 0);
    completion.put(ACTIVITIES, 0);
  }

  public String getCompletionAsJson()
  {
    StringBuilder sb = new StringBuilder() ;
    sb.append("[");
    boolean first = true;
    for (String key:completion.keySet())
    {
      if (!first) sb.append(",");
      sb.append("{\"name\": \""+key+"\",");
      sb.append("\"percentage\": \""+completion.get(key)+"%\"}");
      first = false;
    }
    sb.append("]");

    return sb.toString();
  }

  public void start()
  {
    setSate("Users : Create Users, Avatars, Relations");
    userService_.createUsers(username, fullname);
    completion.put(USERS, 33);
    userService_.attachAvatars(username);
    completion.put(USERS, 80);
    userService_.createRelations(username);
    completion.put(USERS, 100);

    setSate("Spaces : Create Spaces, Avatars, Members");
    spaceService_.createSpaces(username);
    completion.put(SPACES, 50);
    spaceService_.addSpacesAvatars(username);
    completion.put(SPACES, 90);
    spaceService_.joinSpaces(username);
    completion.put(SPACES, 100);

    setSate("Calendar : Create Calendars and set Colors");
    calendarService_.setCalendarColors(username, fullname);
    completion.put(CALENDAR, 20);
    calendarService_.createEvents(username, fullname);
    completion.put(CALENDAR, 100);

    setSate("Wiki : Create Wikis");
    wikiService_.createUserWiki();
    completion.put(WIKI, 100);

    setSate("Documents : Content Templates");
    documentService_.updateTemplates();
    completion.put(DOCUMENTS, 10);

    setSate("Documents : Upload Personal Documents, Space Documents");
    documentService_.uploadDocuments(username);
    completion.put(DOCUMENTS, 60);

    setSate("Forum : Create Categories, Discussions, Posts and Poll");
    forumService_.createCategoriesAndForum();
    completion.put(FORUM, 20);
    forumService_.createPosts();
    completion.put(FORUM, 60);
    forumService_.createPollAndVote();
    completion.put(FORUM, 100);

    setSate("Activities : Push new activities, Comments and Likes");
    activityService_.pushActivities(username);
    completion.put(ACTIVITIES, 100);

    documentService_.uploadDocuments2(username);
    completion.put(DOCUMENTS, 100);

    setSate("Populate Completed");
  }

  private void setSate(String state)
  {
    currentState_ = state;
    log.info(currentState_);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }
}

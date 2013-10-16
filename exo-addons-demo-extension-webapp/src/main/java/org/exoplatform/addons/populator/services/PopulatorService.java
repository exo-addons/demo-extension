package org.exoplatform.addons.populator.services;

import org.exoplatform.addons.populator.bean.*;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
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

  String username="", fullname="", data="";

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

  public void start(int filter)
  {
    PopulatorBean populatorBean = getData();

    switch (filter)
    {
      case 1:
        setSate("Users : Create Users, Avatars, Relations");
        userService_.createUsers(populatorBean.getUsers());
        completion.put(USERS, 33);
        userService_.attachAvatars(populatorBean.getUsers());
        completion.put(USERS, 80);
        userService_.createRelations(populatorBean.getRelations());
        completion.put(USERS, 100);
        break;

      case 2:
        setSate("Spaces : Create Spaces, Avatars, Members");
        spaceService_.createSpaces(populatorBean.getSpaces());
        completion.put(SPACES, 60);
        spaceService_.addSpacesAvatars(populatorBean.getSpaces());
        completion.put(SPACES, 100);
        break;

      case 3:
        setSate("Calendar : Create Calendars and set Colors");
        calendarService_.setCalendarColors(populatorBean.getCalendars());
        completion.put(CALENDAR, 20);
        calendarService_.createEvents(populatorBean.getCalendars());
        completion.put(CALENDAR, 100);
        break;

      case 4:
        setSate("Wiki : Create Wikis");
        wikiService_.createUserWiki(populatorBean.getWikis());
        completion.put(WIKI, 100);
        break;

      case 5:
        setSate("Documents : Content Templates");
        documentService_.updateTemplates();
        completion.put(DOCUMENTS, 10);
        break;

      case 6:
        setSate("Documents : Upload Personal Documents, Space Documents");
        documentService_.uploadDocuments(username);
        completion.put(DOCUMENTS, 60);
        break;

      case 7:
        setSate("Forum : Create Categories, Discussions, Posts");
        forumService_.createCategoriesAndForum();
        completion.put(FORUM, 20);
        forumService_.createPosts(username);
        completion.put(FORUM, 60);
        break;

      case 8:
        setSate("Activities : Push new activities, Comments and Likes");
        activityService_.pushActivities(populatorBean.getActivities());
        completion.put(ACTIVITIES, 100);
        break;

      case 9:
        setSate("Documents : Upload More Documents");
        documentService_.uploadDocuments2(username);
        completion.put(DOCUMENTS, 90);
        setSate("Documents : Upload News");
        documentService_.uploadNews();
        completion.put(DOCUMENTS, 100);
        break;

      case 10:
        setSate("Forum : Create Poll");
        forumService_.createPollAndVote();
        completion.put(FORUM, 100);
        setSate("Populate Completed");
        break;

    }

  }

  public String getDataAsString()
  {
    if ("".equals(data))
    {
      data = Utils.getData("default.yml");
    }
    return data;
  }

  public PopulatorBean getData()
  {
    Constructor constructor = new Constructor(PopulatorBean.class);
    TypeDescription populatorDescription = new TypeDescription(PopulatorBean.class);
    populatorDescription.putListPropertyType("users", UserBean.class);
    populatorDescription.putListPropertyType("relations", RelationBean.class);
    populatorDescription.putListPropertyType("spaces", SpaceBean.class);
    populatorDescription.putListPropertyType("calendars", CalendarBean.class);
    populatorDescription.putListPropertyType("wikis", WikiBean.class);
    populatorDescription.putListPropertyType("activities", ActivityBean.class);
    constructor.addTypeDescription(populatorDescription);
    Yaml yaml = new Yaml(constructor);
    String data = getDataAsString();
    String[] fn = fullname.split(" ");

    data = data.replaceAll("<USERNAME>", username)
                .replaceAll("<FIRSTNAME>", fn[0])
                .replaceAll("<LASTNAME>", fn[1])
                .replaceAll("<FULLNAME>", fullname);
    PopulatorBean populatorBean = (PopulatorBean)yaml.load(data);
    return populatorBean;
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

  public void setData(String data) {
    this.data = data;
  }
}

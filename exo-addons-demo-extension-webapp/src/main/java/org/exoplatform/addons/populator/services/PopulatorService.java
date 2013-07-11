package org.exoplatform.addons.populator.services;

import javax.inject.Inject;
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

  String currentState_;

  String username, fullname;

  public void start()
  {
    setSate("Users : Create Users, Avatars, Relations");
    userService_.createUsers(username, fullname);
    userService_.attachAvatars(username);
    userService_.createRelations(username);

    setSate("Spaces : Create Spaces, Avatars, Members");
    spaceService_.createSpaces(username);
    spaceService_.addSpacesAvatars(username);
    spaceService_.joinSpaces(username);

    setSate("Calendar : Create Calendars and set Colors");
    calendarService_.setCalendarColors(username, fullname);
    calendarService_.createEvents(username, fullname);

    setSate("Wiki : Create Wikis");
    wikiService_.createUserWiki();

    setSate("Documents : Upload Personal Documents, Space Documents");
    documentService_.uploadDocuments(username);

    setSate("Forum : Create Categories, Discussions, Posts and Poll");
    forumService_.createCategoriesAndForum();
    forumService_.createPosts();
    forumService_.createPollAndVote();
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

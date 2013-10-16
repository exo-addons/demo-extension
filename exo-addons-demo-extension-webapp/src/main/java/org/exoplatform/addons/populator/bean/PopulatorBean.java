package org.exoplatform.addons.populator.bean;

import java.util.List;

public class PopulatorBean {
  List<UserBean> users;
  List<RelationBean> relations;
  List<SpaceBean> spaces;
  List<CalendarBean> calendars;
  List<WikiBean> wikis;
  List<ActivityBean> activities;

  public List<UserBean> getUsers() {
    return users;
  }

  public void setUsers(List<UserBean> users) {
    this.users = users;
  }

  public List<RelationBean> getRelations() {
    return relations;
  }

  public void setRelations(List<RelationBean> relations) {
    this.relations = relations;
  }

  public List<SpaceBean> getSpaces() {
    return spaces;
  }

  public void setSpaces(List<SpaceBean> spaces) {
    this.spaces = spaces;
  }

  public List<CalendarBean> getCalendars() {
    return calendars;
  }

  public void setCalendars(List<CalendarBean> calendars) {
    this.calendars = calendars;
  }

  public List<WikiBean> getWikis() {
    return wikis;
  }

  public void setWikis(List<WikiBean> wikis) {
    this.wikis = wikis;
  }

  public List<ActivityBean> getActivities() {
    return activities;
  }

  public void setActivities(List<ActivityBean> activities) {
    this.activities = activities;
  }
}

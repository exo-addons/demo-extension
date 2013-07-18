package org.exoplatform.addons.populator.bean;

import java.util.List;

public class CalendarBean {
  String user;
  Boolean clearAll=true;

  List<InternalCalendarBean> calendars;

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public List<InternalCalendarBean> getCalendars() {
    return calendars;
  }

  public void setCalendars(List<InternalCalendarBean> calendars) {
    this.calendars = calendars;
  }

  public Boolean getClearAll() {
    return clearAll;
  }

  public void setClearAll(Boolean clearAll) {
    this.clearAll = clearAll;
  }
}

package org.exoplatform.addons.populator.services;

import org.exoplatform.calendar.service.*;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.Group;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Named("calendarService")
@ApplicationScoped
public class CalendarService {

  org.exoplatform.calendar.service.CalendarService calendarService_;
  OrganizationService organizationService_;
  Logger log = Logger.getLogger("CalendarService");

  @Inject
  public CalendarService(org.exoplatform.calendar.service.CalendarService calendarService, OrganizationService organizationService)
  {
    calendarService_ = calendarService;
    organizationService_ = organizationService;
  }

  public void setCalendarColors()
  {
    String username = "benjamin";
    try {
      List<Calendar> calendars = calendarService_.getUserCalendars(username, true);
      Calendar benCalendar = calendars.get(0);
      benCalendar.setCalendarColor(Calendar.N_SKY_BLUE);
      calendarService_.saveUserCalendar(username, benCalendar, true);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void createEvents()
  {
    try {
      String username = "benjamin";
      Calendar benCal = calendarService_.getUserCalendars(username, true).get(0);

      removeAllEvents(username);

      String calId = benCal.getId();
      CalendarEvent event = new CalendarEvent();
      event.setCalendarId(calId);
      event.setSummary("Event Test de Benjamin");
      event.setEventType(CalendarEvent.TYPE_EVENT);
      java.util.Calendar calendar = java.util.Calendar.getInstance();
      calendar.setTimeInMillis(calendar.getTime().getTime());
      event.setFromDateTime(calendar.getTime());
      calendar.setTimeInMillis(calendar.getTime().getTime()+1000*60*60);
      event.setToDateTime(calendar.getTime());

      calendarService_.saveUserEvent(username, calId, event, true);
    } catch (Exception e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  private void removeAllEvents(String username) throws Exception
  {
    List<CalendarEvent> events = getEvents(username);
    for (CalendarEvent event:events)
    {
      if (event.isPrivate())
      {
        calendarService_.removeUserEvent(username, event.getCalendarId(), event.getId());
      }
    }
  }

  private String[] getCalendarsIdList(String username) {


    StringBuilder sb = new StringBuilder();
    List<GroupCalendarData> listgroupCalendar = null;
    List<org.exoplatform.calendar.service.Calendar> listUserCalendar = null;
    try {
      listgroupCalendar = calendarService_.getGroupCalendars(getUserGroups(username), true, username);
      listUserCalendar = calendarService_.getUserCalendars(username, true);
    } catch (Exception e) {
      log.info("Error while checking User Calendar :" + e.getMessage());
    }
    for (GroupCalendarData g : listgroupCalendar) {
      for (org.exoplatform.calendar.service.Calendar c : g.getCalendars()) {
        sb.append(c.getId()).append(",");
      }
    }
    for (org.exoplatform.calendar.service.Calendar c : listUserCalendar) {
      sb.append(c.getId()).append(",");
    }
    String[] list = sb.toString().split(",");
    return list;
  }


  private List<CalendarEvent> getEvents(String username) {
    String[] calList = getCalendarsIdList(username);

    EventQuery eventQuery = new EventQuery();

    eventQuery.setOrderBy(new String[]{Utils.EXO_FROM_DATE_TIME});

    eventQuery.setCalendarId(calList);
    List<CalendarEvent> userEvents = null;
    try {
      userEvents = calendarService_.getEvents(username, eventQuery, calList);

    } catch (Exception e) {
      log.info("Error while checking User Events:" + e.getMessage());
    }
    return userEvents;
  }

  private String[] getUserGroups(String username) throws Exception {

    Object[] objs = organizationService_.getGroupHandler().findGroupsOfUser(username).toArray();
    String[] groups = new String[objs.length];
    for (int i = 0; i < objs.length; i++) {
      groups[i] = ((Group) objs[i]).getId();
    }
    return groups;
  }

  private List getAllCal(String username) throws Exception {
    List<org.exoplatform.calendar.service.Calendar> calList = calendarService_.getUserCalendars(username, true);
    List<GroupCalendarData> lgcd = calendarService_.getGroupCalendars(getUserGroups(username), true, username);
    List<String> calIds = new ArrayList<String>();
    for (GroupCalendarData g : lgcd) {
      for (org.exoplatform.calendar.service.Calendar c : g.getCalendars()) {

        if (!calIds.contains(c.getId())) {
          calIds.add(c.getId());
          calList.add(c);
        }
      }
    }
    return calList;
  }

}

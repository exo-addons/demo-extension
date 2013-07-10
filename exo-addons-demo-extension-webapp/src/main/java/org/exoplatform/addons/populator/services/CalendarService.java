package org.exoplatform.addons.populator.services;

import org.exoplatform.calendar.service.*;
import org.exoplatform.calendar.service.Calendar;
import org.exoplatform.calendar.service.Utils;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.Group;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
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
    Map map = new HashMap();
    map.put("Benjamin Paillereau", Calendar.N_POWDER_BLUE);
    map.put("Public Discussions", Calendar.N_ORANGE);
    map.put("Bank Project", Calendar.N_MOSS_GREEN);
    map.put("Human Resources", Calendar.N_GRAY);
    map.put("Marketing Analytics", Calendar.N_PINK);
    String filtered=null;
    try {
      String[] calendarIdList = getCalendarsIdList(username);
      for (String calId:calendarIdList)
      {
        Calendar calendar = calendarService_.getCalendarById(calId);
        String calName = calendar.getName();
        if (map.containsKey(calName))
        {
          calendar.setCalendarColor((String)map.get(calName));
          if ("Benjamin Paillereau".equals(calName))
            calendarService_.saveUserCalendar(username, calendar, true);
          else
            calendarService_.savePublicCalendar(calendar, false);
        }
        else
        {
          filtered = calendar.getId();
        }
      }
      if (filtered!=null) {
        CalendarSetting setting = calendarService_.getCalendarSetting(username);
        setting.setFilterPublicCalendars(new String[]{filtered});
        calendarService_.saveCalendarSetting(username, setting);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void createEvents()
  {
    try {
      String username = "benjamin";
      Map<String, String> map = getCalendarsMap(username);

//      Calendar benCal = calendarService_.getUserCalendars(username, true).get(0);

      removeAllEvents(username);

      String calId = map.get("Benjamin Paillereau");
      CalendarEvent event = new CalendarEvent();
      event.setCalendarId(calId);
      event.setSummary("Spec Review");
      event.setEventType(CalendarEvent.TYPE_EVENT);
      java.util.Calendar calendar = java.util.Calendar.getInstance();
      calendar.setTimeInMillis(calendar.getTime().getTime());
      calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY);
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 17);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setFromDateTime(calendar.getTime());
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 19);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setToDateTime(calendar.getTime());
      calendarService_.saveUserEvent(username, calId, event, true);

      event = new CalendarEvent();
      event.setCalendarId(calId);
      event.setSummary("Team Work");
      event.setEventType(CalendarEvent.TYPE_EVENT);
      calendar = java.util.Calendar.getInstance();
      calendar.setTimeInMillis(calendar.getTime().getTime());
      calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.TUESDAY);
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 14);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setFromDateTime(calendar.getTime());
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 18);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setToDateTime(calendar.getTime());
      calendarService_.saveUserEvent(username, calId, event, true);

      event = new CalendarEvent();
      event.setCalendarId(calId);
      event.setSummary("EOW Team Meeting");
      event.setEventType(CalendarEvent.TYPE_EVENT);
      calendar = java.util.Calendar.getInstance();
      calendar.setTimeInMillis(calendar.getTime().getTime());
      calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.FRIDAY);
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 13);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setFromDateTime(calendar.getTime());
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 15);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setToDateTime(calendar.getTime());
      calendarService_.saveUserEvent(username, calId, event, true);

      calId = map.get("Marketing Analytics");
      event = new CalendarEvent();
      event.setCalendarId(calId);
      event.setSummary("Lead Gen Study");
      event.setEventType(CalendarEvent.TYPE_EVENT);
      calendar = java.util.Calendar.getInstance();
      calendar.setTimeInMillis(calendar.getTime().getTime());
      calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.TUESDAY);
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 9);
      calendar.set(java.util.Calendar.MINUTE, 30);
      event.setFromDateTime(calendar.getTime());
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 12);
      calendar.set(java.util.Calendar.MINUTE, 30);
      event.setToDateTime(calendar.getTime());
      calendarService_.savePublicEvent(calId, event, true);

      event = new CalendarEvent();
      event.setCalendarId(calId);
      event.setSummary("Analytics Update");
      event.setEventType(CalendarEvent.TYPE_EVENT);
      calendar = java.util.Calendar.getInstance();
      calendar.setTimeInMillis(calendar.getTime().getTime());
      calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.WEDNESDAY);
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 17);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setFromDateTime(calendar.getTime());
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 19);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setToDateTime(calendar.getTime());
      calendarService_.savePublicEvent(calId, event, true);

      calId = map.get("Public Discussions");
      event = new CalendarEvent();
      event.setCalendarId(calId);
      event.setSummary("Intranet Migration Process");
      event.setEventType(CalendarEvent.TYPE_EVENT);
      calendar = java.util.Calendar.getInstance();
      calendar.setTimeInMillis(calendar.getTime().getTime());
      calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.WEDNESDAY);
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 10);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setFromDateTime(calendar.getTime());
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 14);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setToDateTime(calendar.getTime());
      calendarService_.savePublicEvent(calId, event, true);

      event = new CalendarEvent();
      event.setCalendarId(calId);
      event.setSummary("Company Dinner");
      event.setEventType(CalendarEvent.TYPE_EVENT);
      calendar = java.util.Calendar.getInstance();
      calendar.setTimeInMillis(calendar.getTime().getTime());
      calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.THURSDAY);
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 17);
      calendar.set(java.util.Calendar.MINUTE, 30);
      event.setFromDateTime(calendar.getTime());
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 20);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setToDateTime(calendar.getTime());
      calendarService_.savePublicEvent(calId, event, true);

      calId = map.get("Bank Project");
      event = new CalendarEvent();
      event.setCalendarId(calId);
      event.setSummary("Intranet Demo");
      event.setEventType(CalendarEvent.TYPE_EVENT);
      calendar = java.util.Calendar.getInstance();
      calendar.setTimeInMillis(calendar.getTime().getTime());
      calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.THURSDAY);
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 13);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setFromDateTime(calendar.getTime());
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 17);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setToDateTime(calendar.getTime());
      calendarService_.savePublicEvent(calId, event, true);

      event = new CalendarEvent();
      event.setCalendarId(calId);
      event.setSummary("Customer Q&R");
      event.setEventType(CalendarEvent.TYPE_EVENT);
      calendar = java.util.Calendar.getInstance();
      calendar.setTimeInMillis(calendar.getTime().getTime());
      calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.FRIDAY);
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 16);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setFromDateTime(calendar.getTime());
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 17);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setToDateTime(calendar.getTime());
      calendarService_.savePublicEvent(calId, event, true);

      calId = map.get("Human Resources");
      event = new CalendarEvent();
      event.setCalendarId(calId);
      event.setSummary("Weekly HR Meeting");
      event.setEventType(CalendarEvent.TYPE_EVENT);
      calendar = java.util.Calendar.getInstance();
      calendar.setTimeInMillis(calendar.getTime().getTime());
      calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.FRIDAY);
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 10);
      calendar.set(java.util.Calendar.MINUTE, 0);
      event.setFromDateTime(calendar.getTime());
      calendar.set(java.util.Calendar.HOUR_OF_DAY, 11);
      calendar.set(java.util.Calendar.MINUTE, 30);
      event.setToDateTime(calendar.getTime());
      calendarService_.savePublicEvent(calId, event, true);




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

  private Map<String, String> getCalendarsMap(String username)
  {
    Map<String, String> map = new HashMap<String, String>();
    String[] calendarIdList = getCalendarsIdList(username);
    for (String calId:calendarIdList)
    {
      Calendar calendar = null;
      try {
        calendar = calendarService_.getCalendarById(calId);
        String calName = calendar.getName();
        map.put(calName, calId);
      } catch (Exception e) {
      }
    }
    return map;
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

/*
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
*/

}

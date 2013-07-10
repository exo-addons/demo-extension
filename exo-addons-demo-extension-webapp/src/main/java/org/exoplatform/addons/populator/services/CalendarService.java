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

      removeAllEvents(username);

      saveEvent(username, true, map.get("Benjamin Paillereau"), "Spec Review", java.util.Calendar.MONDAY, 17, 0, 19, 0);
      saveEvent(username, true, map.get("Benjamin Paillereau"), "Team Work", java.util.Calendar.TUESDAY, 14, 0, 18, 0);
      saveEvent(username, true, map.get("Benjamin Paillereau"), "EOW Team Meeting", java.util.Calendar.FRIDAY, 13, 0, 15, 0);
      saveEvent(username, false, map.get("Marketing Analytics"), "Lead Gen Study", java.util.Calendar.TUESDAY, 9, 30, 12, 30);
      saveEvent(username, false, map.get("Marketing Analytics"), "Analytics Update", java.util.Calendar.WEDNESDAY, 17, 0, 19, 0);
      saveEvent(username, false, map.get("Public Discussions"), "Intranet Migration Process", java.util.Calendar.WEDNESDAY, 10, 0, 14, 0);
      saveEvent(username, false, map.get("Public Discussions"), "Company Dinner", java.util.Calendar.THURSDAY, 17, 30, 20, 0);
      saveEvent(username, false, map.get("Bank Project"), "Intranet Demo", java.util.Calendar.THURSDAY, 13, 0, 17, 0);
      saveEvent(username, false, map.get("Bank Project"), "Customer Q&R", java.util.Calendar.FRIDAY, 16, 0, 17, 0);
      saveEvent(username, false, map.get("Human Resources"), "Weekly HR Meeting", java.util.Calendar.FRIDAY, 10, 0, 11, 30);

    } catch (Exception e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  private void saveEvent(String username, boolean isUserEvent, String calId, String summary,
                         int day, int fromHour, int fromMin, int toHour, int toMin) throws Exception
  {
    CalendarEvent event = new CalendarEvent();
    event.setCalendarId(calId);
    event.setSummary(summary);
    event.setEventType(CalendarEvent.TYPE_EVENT);
    event.setPrivate(isUserEvent);
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    calendar.setTimeInMillis(calendar.getTime().getTime());
    calendar.set(java.util.Calendar.DAY_OF_WEEK, day);
    calendar.set(java.util.Calendar.HOUR_OF_DAY, fromHour);
    calendar.set(java.util.Calendar.MINUTE, fromMin);
    event.setFromDateTime(calendar.getTime());
    calendar.set(java.util.Calendar.HOUR_OF_DAY, toHour);
    calendar.set(java.util.Calendar.MINUTE, toMin);
    event.setToDateTime(calendar.getTime());
    if (isUserEvent)
      calendarService_.saveUserEvent(username, calId, event, true);
    else
      calendarService_.savePublicEvent(calId, event, true);
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
      else
      {
        calendarService_.removePublicEvent(event.getCalendarId(), event.getId());
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

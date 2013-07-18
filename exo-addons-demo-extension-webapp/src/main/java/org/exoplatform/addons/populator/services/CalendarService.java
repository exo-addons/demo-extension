package org.exoplatform.addons.populator.services;

import org.exoplatform.addons.populator.bean.CalendarBean;
import org.exoplatform.addons.populator.bean.EventBean;
import org.exoplatform.addons.populator.bean.InternalCalendarBean;
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

  public void setCalendarColors(List<CalendarBean> calendarBeans)
  {
    for (CalendarBean calendarBean:calendarBeans)
    {
      String username = calendarBean.getUser();
      List<InternalCalendarBean> calendars = calendarBean.getCalendars();
      Map<String,InternalCalendarBean> map = new HashMap();
      for (InternalCalendarBean calendar:calendars)
      {
        map.put(calendar.getName(), calendar);
      }

      String filtered=null;
      try {
        String[] calendarIdList = getCalendarsIdList(username);
        for (String calId:calendarIdList)
        {
          Calendar calendar = calendarService_.getCalendarById(calId);
          String calName = calendar.getName();
          if (map.containsKey(calName))
          {
            InternalCalendarBean calTemp = map.get(calName);
            calendar.setCalendarColor(calTemp.getColor());
            if (calTemp.isUserCalendar())
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



  }

  public void createEvents(List<CalendarBean> calendarBeans)
  {

    try {

      for (CalendarBean calendarBean:calendarBeans)
      {
        String username = calendarBean.getUser();
        Map<String, String> map = getCalendarsMap(username);

        if (calendarBean.getClearAll()) removeAllEvents(username);

        List<InternalCalendarBean> calendars = calendarBean.getCalendars();
        for (InternalCalendarBean calendar:calendars)
        {
          for (EventBean event:calendar.getEvents())
          {
            saveEvent(username, calendar.isUserCalendar(), map.get(calendar.getName()),
                    event.getTitle(), event.getDayAsInt(),
                    event.getStartHour(), event.getStartMinute(), event.getEndHour(), event.getEndMinute());
          }
        }
      }

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
    event.setRepeatType(CalendarEvent.RP_NOREPEAT);
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

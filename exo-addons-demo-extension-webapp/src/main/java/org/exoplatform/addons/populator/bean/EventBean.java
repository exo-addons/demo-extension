package org.exoplatform.addons.populator.bean;

import java.util.Calendar;

public class EventBean {
  String title;
  String day;
  String start;
  String end;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public int getDayAsInt() {
    if ("monday".equals(day))
      return Calendar.MONDAY;
    else if ("tuesday".equals(day))
      return Calendar.TUESDAY;
    else if ("wednesday".equals(day))
      return Calendar.WEDNESDAY;
    else if ("thursday".equals(day))
      return Calendar.THURSDAY;
    else if ("friday".equals(day))
      return Calendar.FRIDAY;
    else if ("saturday".equals(day))
      return Calendar.SATURDAY;
    else if ("sunday".equals(day))
      return Calendar.SUNDAY;

    return Calendar.MONDAY;
  }

  public int getStartHour() {
    String[] start = this.start.split(":");
    Integer hour = Integer.parseInt(start[0]);
    return hour;
  }
  public int getStartMinute() {
    String[] start = this.start.split(":");
    Integer hour = Integer.parseInt(start[1]);
    return hour;
  }
  public int getEndHour() {
    String[] end = this.end.split(":");
    Integer hour = Integer.parseInt(end[0]);
    return hour;
  }
  public int getEndMinute() {
    String[] end = this.end.split(":");
    Integer hour = Integer.parseInt(end[1]);
    return hour;
  }
}

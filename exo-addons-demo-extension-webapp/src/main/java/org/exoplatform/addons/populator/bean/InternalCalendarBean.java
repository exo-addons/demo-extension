package org.exoplatform.addons.populator.bean;

import java.util.List;

public class InternalCalendarBean {
  String color;
  String name;
  String type = "group";
  List<EventBean> events;

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isUserCalendar() {
    return "user".equals(type);
  }

  public List<EventBean> getEvents() {
    return events;
  }

  public void setEvents(List<EventBean> events) {
    this.events = events;
  }
}

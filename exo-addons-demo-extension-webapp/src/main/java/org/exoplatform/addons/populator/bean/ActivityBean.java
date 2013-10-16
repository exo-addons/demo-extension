package org.exoplatform.addons.populator.bean;

import java.util.List;

public class ActivityBean {

  String from;
  String body;
  List<String> likes;
  List<ActivityBean> comments;

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public List<String> getLikes() {
    return likes;
  }

  public void setLikes(List<String> likes) {
    this.likes = likes;
  }

  public List<ActivityBean> getComments() {
    return comments;
  }

  public void setComments(List<ActivityBean> comments) {
    this.comments = comments;
  }
}

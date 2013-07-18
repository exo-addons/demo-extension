package org.exoplatform.addons.populator.bean;

import java.util.List;

public class SpaceBean {
  String displayName, prettyName, creator, avatar;
  List<String> members;

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getPrettyName() {
    return prettyName;
  }

  public void setPrettyName(String prettyName) {
    this.prettyName = prettyName;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public List<String> getMembers() {
    return members;
  }

  public void setMembers(List<String> members) {
    this.members = members;
  }
}

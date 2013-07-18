package org.exoplatform.addons.populator.bean;

public class RelationBean {
  String inviting, invited;
  Boolean confirm=true;

  public String getInviting() {
    return inviting;
  }

  public void setInviting(String inviting) {
    this.inviting = inviting;
  }

  public String getInvited() {
    return invited;
  }

  public void setInvited(String invited) {
    this.invited = invited;
  }

  public Boolean getConfirm() {
    return confirm;
  }

  public void setConfirm(Boolean confirm) {
    this.confirm = confirm;
  }
}

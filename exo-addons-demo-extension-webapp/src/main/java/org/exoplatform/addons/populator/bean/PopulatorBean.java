package org.exoplatform.addons.populator.bean;

import java.util.List;

public class PopulatorBean {
  List<UserBean> users;
  List<RelationBean> relations;

  public List<UserBean> getUsers() {
    return users;
  }

  public void setUsers(List<UserBean> users) {
    this.users = users;
  }

  public List<RelationBean> getRelations() {
    return relations;
  }

  public void setRelations(List<RelationBean> relations) {
    this.relations = relations;
  }
}

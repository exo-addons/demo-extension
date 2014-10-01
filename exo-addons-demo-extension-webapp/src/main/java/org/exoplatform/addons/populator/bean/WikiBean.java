package org.exoplatform.addons.populator.bean;

import java.util.List;

public class WikiBean {
  String title;
  String type = "portal";
  String owner = "intranet";
  String filename = null;
  String parent = "Wiki Home";
  Boolean isNew = false;
  List<WikiBean> wikis;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public Boolean getNew() {
    return isNew;
  }

  public void setNew(Boolean aNew) {
    isNew = aNew;
  }

  public List<WikiBean> getWikis() {
    return wikis;
  }

  public void setWikis(List<WikiBean> wikis) {
    this.wikis = wikis;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }
}

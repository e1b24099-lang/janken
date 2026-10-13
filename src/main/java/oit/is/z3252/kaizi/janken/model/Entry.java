package oit.is.z3252.kaizi.janken.model;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

// @Componentを付けるとSpringがEntryを1つだけ作り，@Autowiredした全員で共有する
@Component
public class Entry {
  ArrayList<String> users = new ArrayList<>();

  public void addUser(String name) {
    // 同名のユーザが居たら何もせずにreturn
    for (String s : this.users) {
      if (s.equals(name)) {
        return;
      }
    }
    this.users.add(name);
  }

  // getterがないとThymeleafで値を取得できない
  public ArrayList<String> getUsers() {
    return users;
  }

  public void setUsers(ArrayList<String> users) {
    this.users = users;
  }

}

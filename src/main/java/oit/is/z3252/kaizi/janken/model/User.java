package oit.is.z3252.kaizi.janken.model;

// users テーブルの1行分（id, name）を入れるクラス
public class User {
  int id;
  String name;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

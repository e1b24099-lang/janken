package oit.is.z3252.kaizi.janken.model;

// matchinfo テーブルの1行分（相手の手を待っている試合）を入れるクラス
public class MatchInfo {
  int id;
  int user1; // 先に手を出したユーザのID
  int user2; // 対戦相手のユーザのID
  String user1Hand; // 先に出した手
  boolean isActive; // true なら相手の手を待っている

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUser1() {
    return user1;
  }

  public void setUser1(int user1) {
    this.user1 = user1;
  }

  public int getUser2() {
    return user2;
  }

  public void setUser2(int user2) {
    this.user2 = user2;
  }

  public String getUser1Hand() {
    return user1Hand;
  }

  public void setUser1Hand(String user1Hand) {
    this.user1Hand = user1Hand;
  }

  public boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }
}

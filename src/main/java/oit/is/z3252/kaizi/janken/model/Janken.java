package oit.is.z3252.kaizi.janken.model;

public class Janken {
  String myHand;
  String cpuHand = "Gu";

  public Janken(String myHand) {
    this.myHand = myHand;
  }

  public String getCpuHand() {
    return this.cpuHand;
  }

  public String judge() {
    if (myHand.equals(cpuHand)) {
      return "Draw";
    }
    if ((myHand.equals("Gu") && cpuHand.equals("Choki"))
        || (myHand.equals("Choki") && cpuHand.equals("Pa"))
        || (myHand.equals("Pa") && cpuHand.equals("Gu"))) {
      return "You Win!";
    }
    return "You Lose!";
  }
}

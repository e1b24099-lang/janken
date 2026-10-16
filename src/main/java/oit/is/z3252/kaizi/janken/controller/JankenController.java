package oit.is.z3252.kaizi.janken.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import oit.is.z3252.kaizi.janken.model.Entry;
import oit.is.z3252.kaizi.janken.model.Janken;

@Controller
public class JankenController {

  // DIで注入されたEntryは全ユーザで共有される
  @Autowired
  private Entry entry;

  // ログインしたユーザをエントリーに追加し，ユーザ名とエントリー一覧を表示する
  @GetMapping("/janken")
  public String janken(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    this.entry.addUser(loginUser);
    model.addAttribute("username", loginUser);
    model.addAttribute("entry", this.entry);
    return "janken.html";
  }

  @GetMapping("/jankengame")
  public String jankenGame(@RequestParam String hand, Principal prin, ModelMap model) {
    Janken janken = new Janken(hand);
    model.addAttribute("username", prin.getName());
    model.addAttribute("entry", this.entry);
    model.addAttribute("myHand", hand);
    model.addAttribute("cpuHand", janken.getCpuHand());
    model.addAttribute("result", janken.judge());

    return "janken.html";
  }
}

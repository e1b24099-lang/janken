package oit.is.z3252.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z3252.kaizi.janken.model.Entry;
import oit.is.z3252.kaizi.janken.model.Janken;
import oit.is.z3252.kaizi.janken.model.Match;
import oit.is.z3252.kaizi.janken.model.MatchMapper;
import oit.is.z3252.kaizi.janken.model.User;
import oit.is.z3252.kaizi.janken.model.UserMapper;

@Controller
public class JankenController {

  // DIで注入されたEntryは全ユーザで共有される
  @Autowired
  private Entry entry;

  // DB の users テーブルを操作する Mapper（DI で受け取る）
  @Autowired
  UserMapper userMapper;

  // DB の matches テーブルを操作する Mapper（DI で受け取る）
  @Autowired
  MatchMapper matchMapper;

  // ログインユーザ名と，DB に登録されているユーザ一覧・試合結果一覧を表示する
  @GetMapping("/janken")
  public String janken(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    this.entry.addUser(loginUser);
    model.addAttribute("username", loginUser);
    model.addAttribute("entry", this.entry);

    // DB からユーザ一覧を取得して画面に渡す
    ArrayList<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);

    // DB から試合結果一覧を取得して画面に渡す
    ArrayList<Match> matches = matchMapper.selectAllMatches();
    model.addAttribute("matches", matches);
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

    // じゃんけん後の画面にもユーザ一覧と試合結果一覧を渡す
    ArrayList<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);
    ArrayList<Match> matches = matchMapper.selectAllMatches();
    model.addAttribute("matches", matches);
    return "janken.html";
  }
}

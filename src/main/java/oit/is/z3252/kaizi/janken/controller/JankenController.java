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
import oit.is.z3252.kaizi.janken.model.MatchInfo;
import oit.is.z3252.kaizi.janken.model.MatchInfoMapper;
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

  // DB の matchinfo テーブルを操作する Mapper（DI で受け取る）
  @Autowired
  MatchInfoMapper matchInfoMapper;

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

    // アクティブな試合（相手の手を待っている試合）を画面に渡す
    ArrayList<MatchInfo> activeMatches = matchInfoMapper.selectActiveMatchInfo();
    model.addAttribute("activeMatches", activeMatches);
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

  // エントリーのユーザ名をクリックしたときに，対戦相手を表示する
  @GetMapping("/match")
  public String match(@RequestParam int id, Principal prin, ModelMap model) {
    User cpu = userMapper.selectById(id); // URL の ?id= の値で相手を取得
    model.addAttribute("loginUser", prin.getName());
    model.addAttribute("cpu", cpu);
    return "match.html";
  }

  // じゃんけんの手をクリックしたとき
  // 相手が CPU ならその場で試合を行い，人間なら matchinfo に登録して相手の手を待つ
  @GetMapping("/fight")
  public String fight(@RequestParam int id, @RequestParam String hand, Principal prin, ModelMap model) {
    User me = userMapper.selectByName(prin.getName()); // 自分（ログインユーザ）
    User cpu = userMapper.selectById(id); // 選んだ相手

    // 相手が人間のときは matchinfo に登録して wait.html で相手の手を待つ
    if (!cpu.getName().equals("CPU")) {
      MatchInfo info = new MatchInfo();
      info.setUser1(me.getId());
      info.setUser2(cpu.getId());
      info.setUser1Hand(hand);
      info.setIsActive(true);
      matchInfoMapper.insertMatchInfo(info);

      model.addAttribute("username", prin.getName());
      return "wait.html";
    }

    // 以下は相手が CPU のとき（今までどおり）
    Janken janken = new Janken(hand); // じゃんけんの判定（CPU の手は固定）

    // 試合結果を matches テーブルに登録する
    Match match = new Match();
    match.setUser1(me.getId());
    match.setUser2(cpu.getId());
    match.setUser1Hand(hand);
    match.setUser2Hand(janken.getCpuHand());
    matchMapper.insertMatch(match);

    // 画面に表示する値を渡す
    model.addAttribute("loginUser", prin.getName());
    model.addAttribute("cpu", cpu);
    model.addAttribute("myHand", hand);
    model.addAttribute("cpuHand", janken.getCpuHand());
    model.addAttribute("result", janken.judge());
    return "match.html";
  }
}

package oit.is.z3252.kaizi.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import oit.is.z3252.kaizi.janken.model.Janken;

@Controller
public class JankenController {

  // ブラウザから直接 /janken を開いたとき(GET)は名前を登録しないので「Hi ???」は表示されない
  @GetMapping("/janken")
  public String janken() {
    return "janken.html";
  }

  // index.htmlのフォームから送られたとき(POST)はユーザ名を受け取ってmodelに登録する
  @PostMapping("/janken")
  public String jankenEntry(@RequestParam String username, ModelMap model) {
    model.addAttribute("username", username);
    return "janken.html";
  }

  @GetMapping("/jankengame")
  public String jankenGame(@RequestParam String hand, ModelMap model) {
    Janken janken = new Janken(hand);
    model.addAttribute("myHand", hand);
    model.addAttribute("cpuHand", janken.getCpuHand());
    model.addAttribute("result", janken.judge());

    return "janken.html";
  }
}

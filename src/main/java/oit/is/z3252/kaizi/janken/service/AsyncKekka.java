package oit.is.z3252.kaizi.janken.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z3252.kaizi.janken.model.Match;
import oit.is.z3252.kaizi.janken.model.MatchInfoMapper;
import oit.is.z3252.kaizi.janken.model.MatchMapper;

@Service
public class AsyncKekka {
  // DB に新しい試合結果が保存されたら true にする
  private boolean dbUpdated = false;

  @Autowired
  MatchMapper matchMapper;

  @Autowired
  MatchInfoMapper matchInfoMapper;

  /**
   * 試合成立時の DB 処理
   * matches に結果を保存し，matchinfo の待機状態を解除して，非同期処理に DB の更新を知らせる
   */
  @Transactional
  public void syncSaveMatch(Match match, int matchInfoId) {
    matchMapper.insertMatch(match);
    matchInfoMapper.updateInactive(matchInfoId);
    this.dbUpdated = true;
  }

  /**
   * wait.html の EventSource から呼ばれ，別スレッドで動き続ける
   * DB が更新されたら isActive = TRUE の試合結果をブラウザに送り，送った結果は isActive = FALSE にする
   */
  @Async
  public void asyncShowResult(SseEmitter emitter) {
    try {
      while (true) {
        // DB が更新されていなければ少し待ってから見直す
        if (!this.dbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        Match match = matchMapper.selectActiveMatch();
        if (match == null) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        emitter.send(match); // Match を JSON にしてブラウザに送る
        this.dbUpdated = false;
        matchMapper.updateInactive(match.getId()); // 同じ結果を何度も表示しないように FALSE にする
        TimeUnit.MILLISECONDS.sleep(500);
      }
    } catch (Exception e) {
      // ブラウザが閉じられた・タイムアウトなどで送信できなくなったら終了する
      System.out.println("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
  }
}

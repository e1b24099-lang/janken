package oit.is.z3252.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchMapper {

  // matches テーブルの全レコードを取得する
  @Select("SELECT * FROM matches")
  ArrayList<Match> selectAllMatches();

  // 試合結果を matches テーブルに登録する（id は自動で振られる）
  @Insert("INSERT INTO matches (user1, user2, user1Hand, user2Hand, isActive) VALUES (#{user1}, #{user2}, #{user1Hand}, #{user2Hand}, #{isActive})")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatch(Match match);

  // まだ先手に表示していない試合結果（isActive = TRUE）を1件取得する
  @Select("SELECT * FROM matches WHERE isActive = TRUE LIMIT 1")
  Match selectActiveMatch();

  // 試合結果を表示したので isActive を FALSE にする
  @Update("UPDATE matches SET isActive = FALSE WHERE id = #{id}")
  void updateInactive(int id);
}

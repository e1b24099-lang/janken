package oit.is.z3252.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchInfoMapper {

  // 相手の手を待っている試合を matchinfo テーブルに登録する（id は自動で振られる）
  @Insert("INSERT INTO matchinfo (user1, user2, user1Hand, isActive) VALUES (#{user1}, #{user2}, #{user1Hand}, #{isActive})")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatchInfo(MatchInfo matchInfo);

  // isActive が TRUE（アクティブな試合）をすべて取得する
  @Select("SELECT * FROM matchinfo WHERE isActive = TRUE")
  ArrayList<MatchInfo> selectActiveMatchInfo();

  // 相手(user1)が自分(user2)を対戦相手に選んで待っているアクティブな試合を1件取得する
  @Select("SELECT * FROM matchinfo WHERE isActive = TRUE AND user1 = #{user1} AND user2 = #{user2} LIMIT 1")
  MatchInfo selectActiveByUsers(@Param("user1") int user1, @Param("user2") int user2);

  // 試合が成立したので待機状態を解除する（isActive を FALSE にする）
  @Update("UPDATE matchinfo SET isActive = FALSE WHERE id = #{id}")
  void updateInactive(int id);
}

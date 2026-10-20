package oit.is.z3252.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchMapper {

  // matches テーブルの全レコードを取得する
  @Select("SELECT * FROM matches")
  ArrayList<Match> selectAllMatches();
}

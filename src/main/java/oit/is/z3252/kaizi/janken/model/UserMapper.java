package oit.is.z3252.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

  // users テーブルの全レコードを取得する
  @Select("SELECT * FROM users")
  ArrayList<User> selectAllUsers();

  // id を指定して users テーブルから1人取得する
  @Select("SELECT * FROM users WHERE id = #{id}")
  User selectById(int id);

  // 名前を指定して users テーブルから1人取得する
  @Select("SELECT * FROM users WHERE name = #{name}")
  User selectByName(String name);
}

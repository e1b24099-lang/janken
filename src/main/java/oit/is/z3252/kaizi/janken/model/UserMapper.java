package oit.is.z3252.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper
public interface UserMapper {

  // users テーブルの全レコードを取得する
  @Select("SELECT * FROM users")
  ArrayList<User> selectAllUsers();
}

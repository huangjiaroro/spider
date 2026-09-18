package com.hexin.cbas.spider.dal.mapper.metacollection;


import com.hexin.cbas.spider.dal.po.metacollection.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxiang
 * @创建时间 2020/4/14 8:42
 * @描述
 */
@Repository
public interface UserMapper {
    @Select("SELECT `code`, `name`, email, cert, allow_ips, chinese_name FROM `user` WHERE cert = #{cert} limit 1")
    @Results(id = "USER", value = {
            @Result(property = "code", column = "code"),
            @Result(property = "allowIps", column = "allow_ips"),
            @Result(property = "chineseName", column = "chinese_name"),
            @Result(property = "policy", column = "code", javaType = List.class,
                    many = @Many(select = "getPolicyByUserCode"))
    })
    User getByCert(@Param("cert") String cert);

    @Select("SELECT `code`, `name`, email, cert, allow_ips, chinese_name FROM `user` WHERE email = #{email} limit 1")
    @ResultMap("USER")
    User getByEmail(@Param("email") String email);

    @Select("select policy_code from user_to_policy where user_code = #{userCode}")
    List<String> getPolicyByUserCode(@Param("userCode") String userCode);

    @Select("SELECT `code`, `name`, email, cert, allow_ips, chinese_name FROM `user` " +
            "WHERE " +
            "(`name` like concat('%', #{name}, '%'))" +
            " or (email like concat('%', #{name}, '%'))" +
            " or (chinese_name like concat('%', #{name}, '%'))")
    @Results(id = "ONLY_USER", value = {
            @Result(property = "code", column = "code"),
            @Result(property = "allowIps", column = "allow_ips"),
            @Result(property = "chineseName", column = "chinese_name")
    })
    List<User> listByName(@Param("name") String name);


    @Select("SELECT `code`, `name`, email, cert, allow_ips, chinese_name FROM `user`")
    @ResultMap(value = "ONLY_USER")
    List<User> list();
}

package com.hexin.cbas.spider.domain.metacollection.repository;

import com.hexin.cbas.spider.dal.po.metacollection.User;

import java.util.List;

/**
 * @author xuxiang
 * @创建时间 2020/4/10 17:40
 * @描述
 */
public interface IUserRepository {

    User getByCert(String cert);

    User getByEmail(String email);

    List<User> listByName(String name);

    List<User> list();
}

package com.hexin.cbas.spider.domain.metacollection.repository.impl;


import com.hexin.cbas.spider.dal.mapper.metacollection.UserMapper;
import com.hexin.cbas.spider.dal.po.metacollection.User;
import com.hexin.cbas.spider.domain.metacollection.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxiang
 * @创建时间 2020/4/10 17:43
 * @描述
 */
@Repository
public class UserRepositoryImpl implements IUserRepository {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public User getByCert(String cert) {
        return userMapper.getByCert(cert);
    }
    
    @Override
    public User getByEmail(String email) {
        return userMapper.getByEmail(email);
    }
    
    @Override
    public List<User> listByName(String name) {
        return userMapper.listByName(name);
    }

    @Override
    public List<User> list() {
        return userMapper.list();
    }
}

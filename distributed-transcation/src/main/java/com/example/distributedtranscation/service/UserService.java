package com.example.distributedtranscation.service;

import com.example.distributedtranscation.db1mapper.DB1UserMapper;
import com.example.distributedtranscation.db2mapper.DB2UserMapper;
import com.example.distributedtranscation.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-04-11 17:26
 **/
@Service
public class UserService {
    @Autowired
    DB1UserMapper db1userMapper;
    @Autowired
    DB2UserMapper db2userMapper;

    @Transactional(value = "jtaManager",rollbackFor = Exception.class)
    public void add_user() {
        User user = new User("张三");
        db1userMapper.insert(user);
        db2userMapper.insert(user);
        throw new RuntimeException("Test");

    }



}

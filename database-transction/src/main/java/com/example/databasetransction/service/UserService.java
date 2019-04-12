package com.example.databasetransction.service;

import com.example.databasetransction.entiy.User;
import com.example.databasetransction.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    UserMapper userMapper;

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public void add_user_with_mandatory() {
        User user = new User("张三");
        userMapper.insert(user);
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void add_user_with_requerd() {
        User user = new User("李四");
        userMapper.insert(user);
        throw new RuntimeException();
    }


}

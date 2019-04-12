package com.example.databasetransction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-04-11 17:59
 **/
@Service
public class TestService {
    @Autowired
    UserService userService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void test() {
        userService.add_user_with_mandatory();
        try {

            userService.add_user_with_requerd();
        }catch (Exception e){
        }
    }
}

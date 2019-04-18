package com.example.distributedtranscation.controller;

import com.example.distributedtranscation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jianganwei
 * @program study-v2
 * @description test
 * @date 2019-04-16 10:17
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/add")
    public String add(){
        userService.add_user();
        return "成功";
    }
}

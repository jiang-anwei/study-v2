package com.example.cacheredis.controller;

import com.example.cacheredis.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-15 15:27
 **/
@RestController
public class DemoController {
    @Autowired
    DemoService demoService;

    @GetMapping("/hash")
    public String hash(@RequestParam("id") String id) {
        return demoService.demo(id);
    }
}

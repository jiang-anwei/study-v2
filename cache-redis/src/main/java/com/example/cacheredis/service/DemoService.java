package com.example.cacheredis.service;

import com.google.common.hash.HashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-15 15:27
 **/
@Service
@Slf4j
public class DemoService {
    @Cacheable(cacheNames = {"demo"}, key = "#id")
    public String demo(String id) {
        log.info("没有走缓存");
        return HashCode.fromString(id).toString();
    }
}

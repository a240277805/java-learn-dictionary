package com.zmk.github.controller;

import com.alicp.jetcache.anno.Cached;
import com.zmk.github.VO.CacheTestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zmk
 * @Date: 2020/12/24/ 17:07
 * @Description
 */
@RestController
@RequestMapping("/cache")
public class CacheController {
    static Logger logger = LoggerFactory.getLogger(CacheController.class);


    @PostMapping(value = "testCache")
    @Cached(name = "test:")
    public Object test1(@RequestBody CacheTestVO test) {
        return "success";
    }
}
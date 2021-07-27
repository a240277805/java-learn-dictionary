package com.zmk.github.controller;

import com.zmk.github.service.Impl.TestService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zmk
 * @Date: 2021/01/14/ 16:35
 * @Description
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    TestService1 testService1;

    @GetMapping(value = "/test")
    public Object test1() {
        testService1.test();
        return "success";
    }
}

package com.zmk.github.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zmk
 * @Date: 2021/01/14/ 16:32
 * @Description
 */
@Service
public class TestService4 {
    @Autowired
    TestService5 testService5;

    public void test() {
        System.out.println(this.getClass().toString());
        testService5.test();
    }
}

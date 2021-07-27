package com.zmk.github.service.Impl;

import org.springframework.stereotype.Service;

/**
 * @Author zmk
 * @Date: 2021/01/14/ 16:39
 * @Description
 */
@Service
public class TestConstService1 {
    final TestConstService2 testConstService2;

    public TestConstService1(TestConstService2 testConstService2) {
        this.testConstService2 = testConstService2;
    }
}

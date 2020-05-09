package com.zmk.github.test.spring_boot_test.ServiceTest;

import com.alibaba.fastjson.JSON;
import com.zmk.github.DO.UserDO;
import com.zmk.github.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName UserServiceTest
 * @Description TODO
 * @Author zmk
 * @Date 2020/5/6下午5:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void selectOneTest() {
        UserDO userDO = userService.selectOne();
//        System.out.println(JSON.toJSON(userDO));
//
//        System.out.println("success");
        log.info("userDO: {}", JSON.toJSON(userDO));
    }


}

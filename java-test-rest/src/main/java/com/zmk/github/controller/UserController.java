package com.zmk.github.controller;

import com.zmk.github.DO.UserDO;
import com.zmk.github.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author zmk
 * @Date 2020/5/6下午3:41
 */
@RestController
@RequestMapping("/user")
public class UserController {
    static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping(value = "selectOne", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object selectOne() {
        UserDO user = userService.selectOne();
        return user;
    }
}

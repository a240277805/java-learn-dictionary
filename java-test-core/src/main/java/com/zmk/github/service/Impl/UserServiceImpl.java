package com.zmk.github.service.Impl;

import com.zmk.github.DO.UserDO;
import com.zmk.github.mappers.UserMapper;
import com.zmk.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author zmk
 * @Date 2020/5/6下午3:38
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    public UserDO selectOne() {
        return userMapper.select();
    }
}

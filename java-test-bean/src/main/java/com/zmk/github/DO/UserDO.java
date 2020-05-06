package com.zmk.github.DO;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName UserDO
 * @Description TODO
 * @Author zmk
 * @Date 2020/5/6下午3:27
 */
@Data
public class UserDO {
    private Long id;
    private String account;
    private Date updateTime;
    private Date createTime;
    private Integer deleteFlag;
}

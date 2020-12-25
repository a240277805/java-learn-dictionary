package com.zmk.github.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author zmk
 * @Date: 2020/12/24/ 10:54
 * @Description
 */
@Data
public class CacheTestVO implements Serializable {
    private String name;
    private String key;
}

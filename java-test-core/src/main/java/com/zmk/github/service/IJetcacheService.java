package com.zmk.github.service;

/**
 * @Author zmk
 * @Date: 2020/12/16/ 9:31
 * @Description
 */
public interface IJetcacheService {
    Boolean putKafkaCacheTraceIdCache(String key, String value);

    String getKafkaCacheTraceIdCache(String key);

    void deleteKeyStr(String key);
}

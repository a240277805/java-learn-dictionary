package com.zmk.github.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.zmk.github.service.IJetcacheService;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author zmk
 * @Date: 2020/12/16/ 9:31
 * @Description
 */
@Service
@Slf4j
public class JetcacheServiceImpl implements IJetcacheService {
    static Logger logger = LoggerFactory.getLogger(JetcacheServiceImpl.class);

    @CreateCache(name = ":cacehTraceId", expire = 12 * 60 * 60)
    private Cache<String, String> messageKeyCache;

    @CreateCache(name = ":kafkaCacheTraceId", expire = 1, timeUnit = TimeUnit.DAYS)
    private Cache<String, String> kafkaCacheTraceIdCache;
    @Autowired
    private RedisClient redisClient;

    @Value("${jetcache.remote.default.keyPrefix}")
    private String redisKeyPrefix;

    /**
     * 添加kafka 缓存处理 业务幂等traceId
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Boolean putKafkaCacheTraceIdCache(String key, String value) {
        log.info(">>>start putKafkaCacheTraceIdCache [key: {}, value: {}]>>>", key, value);
        kafkaCacheTraceIdCache.put(key, value);
        return true;
    }

    /**
     * 获取kafka 缓存处理 业务幂等traceId
     *
     * @param key
     * @return
     */
    @Override
    public String getKafkaCacheTraceIdCache(String key) {
        log.info(">>>start getKafkaCacheTraceIdCache [key: {}]>>>", key);
        return kafkaCacheTraceIdCache.get(key);
    }

    @Override
    public void deleteKeyStr(String keyParam) {
        keyParam = redisKeyPrefix + ":" + keyParam;
        RedisCommands<String, String> connect = redisClient.connect().sync();
        logger.info("[JetcacheServiceImpl] deleteKeyStr start:key: {}", keyParam);
        List<String> keyList = connect.keys(keyParam);
        if (CollectionUtils.isEmpty(keyList)) {
            return;
        }
        List<JSONObject> results = new ArrayList<JSONObject>();
        for (String key : keyList) {
            Long result = connect.del(key);
            JSONObject item = new JSONObject();
            item.put(key, result);
            results.add(item);
        }
        logger.info("[JetcacheServiceImpl] deleteKeyStr end:key: {},result: {}", JSON.toJSONString(results));
    }
}

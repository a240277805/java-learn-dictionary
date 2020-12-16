package com.zmk.github.service.Impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CreateCache;
import com.zmk.github.info.kafka.KafkaKeysConstants;
import com.zmk.github.service.IJetcacheService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    @CreateCache(name = "ctfo:devplatform:cacehTraceId", expire = 12 * 60 * 60)
    private Cache<String, String> messageKeyCache;

    @CreateCache(name = "ctfo:devplatform:kafkaCacheTraceId", expire = 1, timeUnit = TimeUnit.DAYS)
    private Cache<String, String> kafkaCacheTraceIdCache;

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
    @CacheInvalidate(name = KafkaKeysConstants.QF_PROJECT + ":" + KafkaKeysConstants.SERVICE + ":", key = "args[0]")
    public void deleteKeyStr(String key) {
        logger.info("[JetcacheServiceImpl] deleteKeyStr: {}", key);
    }
}

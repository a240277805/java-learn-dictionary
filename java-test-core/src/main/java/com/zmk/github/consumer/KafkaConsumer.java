package com.zmk.github.consumer;

import com.alibaba.fastjson.JSON;
import com.zmk.github.info.kafka.KafkaKeysConstants;
import com.zmk.github.info.kafka.events.AffairEvent;
import com.zmk.github.info.kafka.events.GitlabBaseEvent;
import com.zmk.github.info.kafka.events.KafkaBodyAbstract;
import com.zmk.github.info.kafka.events.ProjectEvent;
import com.zmk.github.service.IJetcacheService;
import com.zmk.github.utils.MyStringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author zmk
 * @Date: 2020/12/09/ 18:05
 * @Description
 */
@Component
public class KafkaConsumer {
    static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    //    private ApplicationContext applicationContext;
    private IJetcacheService jetcacheService;
    /**
     * 消费 处理缓存 key 的列表
     */
    private HashSet<String> cacheAllKeysSet;

    public KafkaConsumer(IJetcacheService iJetCacheService, ApplicationContext applicationContext) {

        this.jetcacheService = iJetCacheService;
//        this.applicationContext = applicationContext;

        this.cacheAllKeysSet = new HashSet<>();
        //订阅事务事件
        cacheAllKeysSet.addAll(KafkaKeysConstants.getAffairEventSets());
        //订阅代码库事件
        cacheAllKeysSet.addAll(KafkaKeysConstants.getGitlabEventSets());
        //订阅项目事件
        cacheAllKeysSet.addAll(KafkaKeysConstants.getProjectEventSets());

    }


    /**
     * 消费者 消费消息
     *
     * @param consumerRecord
     */
    @KafkaListener(topics = {KafkaKeysConstants.DEVOPS_PLATFORM_EVENT_TOPIC}, groupId = "KafkaKeysConstants.DEVOPS_PLATFORM_CACHE_GROUP11")
    @Transactional(rollbackFor = Exception.class)
    public void consume(ConsumerRecord<String, String> consumerRecord) {
        try {

            ////        内部订阅发布
////        ac.publishEvent(foos);
            Optional<?> msgOptional = Optional.ofNullable(consumerRecord);
            if (msgOptional.isPresent()) {
                String consumerKey = consumerRecord.key();
                String consumerValue = consumerRecord.value();
                logger.info("enter KafkaConsumer consume: key: {},value: {}", consumerKey, consumerValue);
                // key 过滤
                if (!cacheAllKeysSet.contains(consumerKey)) {
                    return;
                }
                //转换kafka 统一封装结构
                KafkaBodyAbstract info = MyStringUtils.getTValueElseNull(consumerValue, KafkaBodyAbstract.class);
                if (Objects.isNull(info)) {
                    logger.error("[KafkaCacheEventListener] error 结构转换异常! : {}", JSON.toJSONString(consumerRecord.value()));
                    return;
                }
                // 业务幂等， redis 虽不安全，挂了重复消费也无太大影响
                String cacheTraceId = jetcacheService.getKafkaCacheTraceIdCache(info.getTraceId());

                if (!StringUtils.isEmpty(cacheTraceId)) {
                    logger.info("[KafkaCacheEventListener]  traceId 重复消费 跳过! : {}", info.getTraceId());
                    return;
                }
                logger.info("[KafkaCacheEventListener] start handler kafka msg key: {}, body: {}", consumerKey, JSON.toJSONString(info));
                // TODO: 2020/12/11 下边代码可以用设计模式 优化
                //如果是事务
                if (KafkaKeysConstants.getAffairEventSets().contains(consumerKey)) {
                    AffairEvent affairEvent = MyStringUtils.getTValueElseNull(consumerValue, AffairEvent.class);

                    // 拼装 key task:method:*
                    String deleteCacheKey = String.format("%s:%s*", KafkaKeysConstants.SERVICE_CACHE_TYPE_METHOD, affairEvent.getType());
                    //失效 key
                    jetcacheService.deleteKeyStr(deleteCacheKey);
                }
                //如果是代码库相关
                if (KafkaKeysConstants.getGitlabEventSets().contains(consumerKey)) {

                    GitlabBaseEvent gitlabBaseEvent = MyStringUtils.getTValueElseNull(consumerValue, GitlabBaseEvent.class);
                    // 拼装 key gitlab:project:method:*
                    String deleteCacheKey = String.format("gitlab:%s:%s*", gitlabBaseEvent.getType(), KafkaKeysConstants.SERVICE_CACHE_TYPE_METHOD);
                    //失效 key
                    jetcacheService.deleteKeyStr(deleteCacheKey);
                }
                //如果是项目相关
                if (KafkaKeysConstants.getProjectEventSets().contains(consumerKey)) {
                    ProjectEvent projectEvent = (ProjectEvent) info;
                    // 拼装 key project:method:*
                    String deleteCacheKey = String.format("project:%s*", KafkaKeysConstants.SERVICE_CACHE_TYPE_METHOD);
                    //失效 key
                    jetcacheService.deleteKeyStr(deleteCacheKey);
                }
                jetcacheService.putKafkaCacheTraceIdCache(info.getTraceId(), "1");
            } else {
                logger.error("consumerRecord is null");
            }
        } finally {
//            ack.acknowledge();
        }

    }

}

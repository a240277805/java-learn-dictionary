package com.zmk.github.service.Impl;

import com.alibaba.fastjson.JSON;
import com.zmk.github.info.kafka.KafkaKeysConstants;
import com.zmk.github.info.kafka.events.KafkaBodyAbstract;
import com.zmk.github.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

/**
 * @Author zmk
 * @Date: 2020/12/10/ 9:39
 * @Description
 */
@Service
public class KafkaServiceImpl implements KafkaService {
    static Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);
    @Autowired
    KafkaTemplate kafkaTemplate;

    @Override
    public Boolean send(String msgKey, KafkaBodyAbstract msgData) {
        logger.info("调用service发送kafka消息，消息key:{}, 内容：{}", msgKey, JSON.toJSONString(msgData));
        ListenableFuture<SendResult<String, String>> isDone = this.kafkaTemplate.send(KafkaKeysConstants.DEVOPS_PLATFORM_EVENT_TOPIC, msgKey, JSON.toJSONString(msgData));
        CompletableFuture<SendResult<String, String>> res = isDone.completable();

        return ((CompletableFuture<SendResult<String, String>>) res).isDone();
    }

}

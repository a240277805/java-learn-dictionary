package com.zmk.github.service;

import com.zmk.github.info.kafka.events.KafkaBodyAbstract;

/**
 * @Author zmk
 * @Date: 2020/12/10/ 9:38
 * @Description
 */
public interface KafkaService {
    Boolean send(String msgKey, KafkaBodyAbstract msgData);
}

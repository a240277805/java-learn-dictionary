package com.zmk.github.service;

/**
 * @Author zmk
 * @Date: 2020/12/10/ 9:38
 * @Description
 */
public interface KafkaService {
    void send(String topic, Object info);
}

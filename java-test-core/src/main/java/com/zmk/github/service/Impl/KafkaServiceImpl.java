package com.zmk.github.service.Impl;

import com.zmk.github.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
    public void send(String topic, Object info) {
        kafkaTemplate.send(topic, info.toString());
        logger.info("Messages sent, hit Enter to commit tx");
    }

}

package com.zmk.github.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;

/**
 * @Author zmk
 * @Date: 2020/12/09/ 18:05
 * @Description
 */
@Configuration
public class KafkaConsumer {
    static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(id = "devops-group", topics = "affair_event_topic")
    public void listen(Object foos) throws IOException {
        logger.info("Received: " + foos);
    }
}

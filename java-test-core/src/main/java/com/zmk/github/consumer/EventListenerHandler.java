package com.zmk.github.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Author zmk
 * @Date: 2020/12/10/ 16:02
 * @Description
 */
@Component
public class EventListenerHandler {
    static Logger logger = LoggerFactory.getLogger(EventListenerHandler.class);

    @EventListener
    public void listenHello(Object event) {
        logger.info("listen {} say hello from listenHello method!!!", event.toString());
    }

    @EventListener
    public void listenHello1(Object event) {
        logger.info("listen {} say hello from listenHello method1111111111111!!!", event.toString());
    }
}

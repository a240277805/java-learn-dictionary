package com.zmk.github.publish;

/**
 * @Author zmk
 * @Date: 2020/12/10/ 15:48
 * @Description
 */

public interface IPublisher<T> {
    public void publish(SubscribePublish subscribePublish, T message, boolean isInstantMsg);
}
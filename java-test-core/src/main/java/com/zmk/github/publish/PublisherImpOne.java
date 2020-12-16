package com.zmk.github.publish;

/**
 * @Author zmk
 * @Date: 2020/12/10/ 15:49
 * @Description
 */
public class PublisherImpOne<T> implements IPublisher<T> {
    private String name;

    public PublisherImpOne(String name) {
        super();
        this.name = name;
    }

    public void publish(SubscribePublish subscribePublish, T message, boolean isInstantMsg) {
        subscribePublish.publish(this.name, message, isInstantMsg);
    }
}

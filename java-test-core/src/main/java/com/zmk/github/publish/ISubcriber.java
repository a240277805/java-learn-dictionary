package com.zmk.github.publish;

/**
 * @Author zmk
 * @Date: 2020/12/10/ 15:48
 * @Description
 */
public interface ISubcriber<T> {
    public void subcribe(SubscribePublish subscribePublish);

    public void unSubcribe(SubscribePublish subscribePublish);

    public void update(String publisher, T message);
}

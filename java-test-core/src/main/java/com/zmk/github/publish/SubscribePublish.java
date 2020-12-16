package com.zmk.github.publish;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Author zmk
 * @Date: 2020/12/10/ 15:49
 * @Description
 */
public class SubscribePublish<T> {
    //订阅器名称
    private String name;
    //订阅器队列容量
    final int QUEUE_CAPACITY = 20;
    //订阅器存储队列
    private BlockingQueue<Msg> queue = new ArrayBlockingQueue<Msg>(QUEUE_CAPACITY);
    //订阅者
    private List<ISubcriber> subcribers = new ArrayList<ISubcriber>();

    public SubscribePublish(String name) {
        this.name = name;
    }

    public void publish(String publisher, T message, boolean isInstantMsg) {
        if (isInstantMsg) {
            update(publisher, message);
            return;
        }
        Msg<T> m = new Msg<T>(publisher, message);
        if (!queue.offer(m)) {
            update();
        }
    }

    public void subcribe(ISubcriber subcriber) {
        subcribers.add(subcriber);
    }

    public void unSubcribe(ISubcriber subcriber) {
        subcribers.remove(subcriber);
    }

    public void update() {
        Msg m = null;
        while ((m = queue.peek()) != null) {
            this.update(m.getPublisher(), (T) m.getMsg());
        }
    }

    public void update(String publisher, T Msg) {
        for (ISubcriber subcriber : subcribers) {
            subcriber.update(publisher, Msg);
        }
    }
}

package com.zmk.github.publish;

/**
 * @Author zmk
 * @Date: 2020/12/10/ 15:51
 * @Description
 */
class Msg<T> {
    private String publisher;
    private T m;

    public Msg(String publisher, T m) {
        this.publisher = publisher;
        this.m = m;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public T getMsg() {
        return m;
    }

    public void setMsg(T m) {
        this.m = m;
    }
}

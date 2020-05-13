package com.zmk.github.threads;

import javax.servlet.AsyncContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @ClassName MyThread
 * @Description TODO
 * @Author zmk
 * @Date 2020/5/13上午11:26
 */
public class MyThread extends Thread {
    private AsyncContext asyncContext;

    public MyThread(AsyncContext as) {
        this.asyncContext = as;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            PrintWriter pw = asyncContext.getResponse().getWriter();
            pw.println("hello walker:" + new Date() + "<br />");
            asyncContext.complete();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

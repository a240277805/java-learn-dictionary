package com.zmk.github.controller;

import com.zmk.github.threads.MyThread;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @ClassName ServletController
 * @Description TODO
 * @Author zmk
 * @Date 2020/5/13上午11:23
 */
@WebServlet(name = "myservlet",urlPatterns = "/servlet/index", loadOnStartup = 1,asyncSupported = true)
public class ServletController extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        System.out.println("servlet初始化...");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.println("servlet 开始：" + new Date() + "<br />");
        writer.flush();

        AsyncContext asy = req.startAsync();
        asy.setTimeout(4000);
        asy.start(new MyThread(asy));

        writer.println("servlet 结束：" + new Date() + "<br />");
        writer.flush();

    }
}

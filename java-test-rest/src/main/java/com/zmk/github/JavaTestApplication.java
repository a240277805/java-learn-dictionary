package com.zmk.github;


import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.zmk.github.netty.NettyServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.net.InetSocketAddress;

@SpringBootApplication(scanBasePackages = {"com"})
@MapperScan(basePackages = {"com.zmk.github.mappers"})
@ServletComponentScan(basePackages = "com.zmk.github.controller")
@EnableMethodCache(basePackages = "com.ctfo.platform.devplatformserver")
@EnableCreateCacheAnnotation
public class JavaTestApplication {

    public static void main(String[] args) {

        SpringApplication.run(JavaTestApplication.class, args);
//        //启动服务端
//        NettyServer nettyServer = new NettyServer();
//        nettyServer.start(new InetSocketAddress("127.0.0.1", 8090));
    }
}

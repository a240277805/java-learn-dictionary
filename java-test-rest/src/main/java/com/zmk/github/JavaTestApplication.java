package com.zmk.github;


import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages = {"com.netflix.client.config.IClientConfig","com.zmk.github"})
@MapperScan(basePackages = {"com.zmk.github.mappers"})
@ServletComponentScan(basePackages = {"com.zmk.github.controller"})
@EnableMethodCache(basePackages = {"com.zmk.github"})
@EnableCreateCacheAnnotation
public class JavaTestApplication {

    public static void main(String[] args) {

        SpringApplication.run(JavaTestApplication.class, args);
//        //启动服务端
//        NettyServer nettyServer = new NettyServer();
//        nettyServer.start(new InetSocketAddress("127.0.0.1", 8090));
    }
}

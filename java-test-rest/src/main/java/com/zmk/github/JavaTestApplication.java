package com.zmk.github;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com"})
@MapperScan(basePackages = {"com.zmk.github.mappers"})
public class JavaTestApplication {

    public static void main(String[] args) {
        System.out.println("zmk first spring boot");

        SpringApplication.run(JavaTestApplication.class, args);
    }
}

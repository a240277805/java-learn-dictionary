package com.zmk.github;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com"})
@RestController
@MapperScan(basePackages = {"com.zmk.github.mappers"})
public class JavaTestApplication {
    @RequestMapping("/")
    public String home() {
        return "Hello, Zbook!";
    }

    public static void main(String[] args) {
        System.out.println("zmk first spring boot");

        SpringApplication.run(JavaTestApplication.class, args);
    }
}

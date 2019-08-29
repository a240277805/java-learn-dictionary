package com.zmk.github;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com"})
@RestController
public class JavaTestApplication {
    @RequestMapping("/")
    public String home(){
        return "Hello, Zbook!";
    }
    public static void main(String[] args) {
        System.out.println("zmk first spring boot");

        SpringApplication.run(JavaTestApplication.class, args);
    }
}

package com.zmk.github;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
public class JavaTestApplication {


//    @RequestMapping(PATH_ROOT)
//    public String welcome() {
//        return "Welcome!";
//    }

    public static void main(String[] args) {
        System.out.println("zmk first spring boot");
        SpringApplication.run(JavaTestApplication.class, args);
    }
}

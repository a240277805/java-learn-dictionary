package com.zmk.github.controller;

import com.zmk.github.JavaTestApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName FirstController
 * @Description TODO
 * @Author zmk
 * @Date 2019/8/28下午4:02
 */
@RestController
@RequestMapping("/first")
public class FirstController {
    static Logger logger = LoggerFactory.getLogger(FirstController.class);
    @GetMapping(value = "index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object index() {
        logger.info("hahahah");
        logger.error("hahahah error");
        logger.debug("hahahah debug");
        return "success";
    }
}

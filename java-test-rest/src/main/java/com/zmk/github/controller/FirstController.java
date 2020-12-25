package com.zmk.github.controller;

import com.alicp.jetcache.anno.Cached;
import com.zmk.github.VO.CacheTestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    @Cached(name = "test")
    public Object index() {
        logger.info("hahahah");
        logger.error("hahahah error");
        logger.debug("hahahah debug");
        return "success";
    }

    @PostMapping(value = "testCache")
    @Cached(name = "test:")
    public Object test1(@RequestBody CacheTestVO test) {
        return "success";
    }
}

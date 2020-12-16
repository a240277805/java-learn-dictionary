package com.zmk.github.controller;

import com.zmk.github.AO.KafkaSendAO;
import com.zmk.github.info.kafka.events.GitlabEvent;
import com.zmk.github.service.KafkaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zmk
 * @Date: 2020/12/10/ 9:38
 * @Description
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    private KafkaService kafkaService;

    @PostMapping(value = "send", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object send(@RequestBody KafkaSendAO request) {

        GitlabEvent affairEvent = new GitlabEvent();
        BeanUtils.copyProperties(request.getInfo(), affairEvent);
        Boolean result = kafkaService.send(request.getEvent(), affairEvent);
        return result;
    }
}

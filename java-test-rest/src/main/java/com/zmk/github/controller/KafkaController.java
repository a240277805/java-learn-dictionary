package com.zmk.github.controller;

import com.zmk.github.AO.KafkaSendAO;
import com.zmk.github.info.kafka.events.AffairEvent;
import com.zmk.github.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

        AffairEvent affairEvent = new AffairEvent();
        affairEvent.setId(1l);
        affairEvent.setType("task");
        kafkaService.send(request.getTopic(), affairEvent);
        return "success";
    }
}

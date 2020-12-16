package com.zmk.github.AO;

import com.zmk.github.info.kafka.events.GitlabEvent;
import lombok.Data;

/**
 * @Author zmk
 * @Date: 2020/12/10/ 9:44
 * @Description
 */
@Data
public class KafkaSendAO {
    private String event;
    private GitlabEvent info;
}

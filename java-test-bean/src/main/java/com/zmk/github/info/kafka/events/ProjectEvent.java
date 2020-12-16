package com.zmk.github.info.kafka.events;

import lombok.Data;

/**
 * @Author zmk
 * @Date: 2020/12/11/ 17:59
 * @Description
 */
@Data
public class ProjectEvent extends KafkaBodyAbstract {
    /**
     * 事务id
     */
    private Long id;
}

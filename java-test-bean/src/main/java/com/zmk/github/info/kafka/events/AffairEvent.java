package com.zmk.github.info.kafka.events;

import lombok.Data;

/**
 * @Author zmk
 * @Date: 2020/12/10/ 16:48
 * @Description
 */
@Data
public class AffairEvent extends KafkaBodyAbstract {
    /**
     * 事务id
     */
    private Long id;
    /**
     * 事务类型
     */
    private String type;
}

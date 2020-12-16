package com.zmk.github.info.kafka.events;

import lombok.Data;

/**
 * @Author zmk
 * @Date: 2020/12/11/ 17:42
 * @Description
 */
@Data
public class GitlabBaseEvent extends KafkaBodyAbstract {
    /**
     * code- 代码库 group-分组
     */
    private String type;
}

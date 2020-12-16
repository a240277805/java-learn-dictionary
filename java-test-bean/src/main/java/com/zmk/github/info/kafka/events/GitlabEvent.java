package com.zmk.github.info.kafka.events;

import lombok.Data;

/**
 * @Author zmk
 * @Date: 2020/12/11/ 17:42
 * @Description
 */
@Data
public class GitlabEvent extends GitlabBaseEvent {
    /**
     * （代码库/分组）关联关系ID
     */
    private Long id;
    /**
     * gitlab  (代码库/分组)主键ID
     */
    private Long gitlabId;
}

package com.zmk.github.info.kafka.events;


import com.zmk.github.utils.SnowflakeUtils;
import lombok.Data;

/**
 * @Author zmk
 * @Date: 2020/12/11/ 17:24
 * @Description
 */
@Data
public abstract class KafkaBodyAbstract {
    /**
     * 业务唯一幂等ID
     */
    private String traceId = SnowflakeUtils.next64Id();
    /**
     * 消息创建时间
     */
    private Long msgCreateTime = System.currentTimeMillis();

}

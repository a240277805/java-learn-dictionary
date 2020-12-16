package com.zmk.github.info.kafka.events;


import com.zmk.github.utils.SnowflakeUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author zmk
 * @Date: 2020/12/11/ 17:24
 * @Description
 */
@Data
public class KafkaBodyAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 业务唯一幂等ID
     */
    private String traceId = SnowflakeUtils.next64Id();
    /**
     * 消息创建时间
     */
    private Long msgCreateTime = System.currentTimeMillis();

}

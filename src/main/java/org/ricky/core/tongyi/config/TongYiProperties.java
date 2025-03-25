package org.ricky.core.tongyi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className TongYiProperties
 * @desc 通义大模型配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "my.tongyi")
public class TongYiProperties {

    /**
     * api key
     */
    private String apiKey;

    /**
     * 模型
     */
    private String model = "wanx2.1-t2i-turbo";

    /**
     * 生成的图片数量
     */
    private Integer n = 1;

    /**
     * 图片大小
     */
    private String picSize = "1024*1024";

}

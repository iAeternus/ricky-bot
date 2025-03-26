package org.ricky.core.utilitytool.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className AmapProperties
 * @desc 高德地图配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "my.amap")
public class AmapProperties {

    private String apiKey;

}

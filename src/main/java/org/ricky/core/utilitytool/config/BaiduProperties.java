package org.ricky.core.utilitytool.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/26
 * @className BaiduProperties
 * @desc
 */
@Data
@Component
@ConfigurationProperties(prefix = "my.baidu")
public class BaiduProperties {

    private String appId;
    private String secretKey;

}

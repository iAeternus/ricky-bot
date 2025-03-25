package org.ricky.core.pixiv.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className PixivProperties
 * @desc
 */
@Data
@Component
@ConfigurationProperties(prefix = "my.pixiv")
public class PixivProperties {

    private boolean enable;
    private String secretKey;

}

package org.ricky.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/20
 * @className SystemProperties
 * @desc
 */
@Data
@Component
@Validated
@ConfigurationProperties("my")
public class SystemProperties {

    /**
     * 版本号
     */
    private String version;

    /**
     * 作者
     */
    private String author;

}

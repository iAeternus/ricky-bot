package org.ricky.common.ricky;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/20
 * @className RickyBotProperties
 * @desc
 */
@Data
@Component
@Validated
@ConfigurationProperties("my")
public class RickyBotProperties {

    private String version;
    private String author;

}

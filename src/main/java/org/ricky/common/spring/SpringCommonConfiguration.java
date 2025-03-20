package org.ricky.common.spring;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static java.time.Duration.ofSeconds;

/**
 * @author Ricky
 * @version 1.0
 * @date 2024/9/10
 * @className SpringCommonConfiguration
 * @desc Spring通用配置类
 */
@Configuration
public class SpringCommonConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(ofSeconds(10))
                .setReadTimeout(ofSeconds(10))
                .build();
    }

}

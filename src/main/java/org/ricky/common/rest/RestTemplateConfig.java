package org.ricky.common.rest;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/5/2
 * @className RestTemplateConfig
 * @desc
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate noRedirectRestTemplate() {
        // 创建不跟随重定向的HttpClient
        HttpClient httpClient = HttpClientBuilder.create()
                .disableRedirectHandling() // 禁用自动重定向
                .build();

        // 使用自定义HttpClient创建RestTemplate
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }

}

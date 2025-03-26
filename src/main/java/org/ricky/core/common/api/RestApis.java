package org.ricky.core.common.api;

import lombok.RequiredArgsConstructor;
import org.ricky.core.utilitytool.config.AmapProperties;
import org.ricky.core.utilitytool.domain.WeatherResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/20
 * @className RestApis
 * @desc
 */
@Component
@RequiredArgsConstructor
public class RestApis {

    private final RestTemplate restTemplate;
    private final AmapProperties amapProperties;

    public WeatherResponse getCurrentWeather(String city) {
        String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=" + amapProperties.getApiKey() + "&city=" + city;
        return restTemplate.getForObject(url, WeatherResponse.class);
    }

    public String getRandomPicByKeyword(String keyword) {
        String url = "https://image.anosu.top/pixiv/json?keyword=" + keyword;
        return restTemplate.getForObject(url, String.class);
    }

    public String getRandomPicByKeyword2(String keyword) {
        String url = "https://image.anosu.top/pixiv/json?r18=1&keyword=" + keyword;
        return restTemplate.getForObject(url, String.class);
    }

}

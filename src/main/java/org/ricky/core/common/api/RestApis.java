package org.ricky.core.common.api;

import lombok.RequiredArgsConstructor;
import org.ricky.common.properties.SystemProperties;
import org.ricky.core.weather.domain.WeatherResponse;
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
    private final SystemProperties systemProperties;

    public WeatherResponse getCurrentWeather(String city) {
        String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=" + systemProperties.getAmapApiKey() + "&city=" + city;
        return restTemplate.getForObject(url, WeatherResponse.class);
    }

}

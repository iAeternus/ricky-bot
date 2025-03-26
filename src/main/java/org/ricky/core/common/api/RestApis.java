package org.ricky.core.common.api;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.ricky.core.utilitytool.config.AmapProperties;
import org.ricky.core.utilitytool.config.BaiduProperties;
import org.ricky.core.utilitytool.domain.WeatherResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

import static cn.hutool.crypto.digest.DigestUtil.md5Hex;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.ricky.core.common.utils.ValidationUtil.isBlank;

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
    private final BaiduProperties baiduProperties;

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

    public JSONObject getTranslation(String q, String from, String to) {
        if(isBlank(from)) {
            from = "auto";
        }
        String salt = randomNumeric(10);
        String sign = md5Hex(baiduProperties.getAppId() + q + salt + baiduProperties.getSecretKey());
        String url = String.format("https://fanyi-api.baidu.com/api/trans/vip/translate?q=%s&from=%s&to=%s&appid=%s&salt=%s&sign=%s",
                q, from, to, baiduProperties.getAppId(), salt, sign);
        return restTemplate.getForObject(url, JSONObject.class);
    }

}

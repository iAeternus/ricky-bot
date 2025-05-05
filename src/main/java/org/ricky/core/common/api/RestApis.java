package org.ricky.core.common.api;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.ricky.common.exception.MyException;
import org.ricky.core.utilitytool.config.AmapProperties;
import org.ricky.core.utilitytool.config.BaiduProperties;
import org.ricky.core.utilitytool.domain.WeatherResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static cn.hutool.crypto.digest.DigestUtil.md5Hex;
import static java.lang.String.format;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.ricky.common.exception.ErrorCodeEnum.INVALID_CMD_ARGS;
import static org.ricky.common.exception.ErrorCodeEnum.PIC_NOT_FOUND;
import static org.ricky.core.common.utils.ValidationUtil.isBlank;
import static org.springframework.http.HttpMethod.GET;

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
    private final RestTemplate noRedirectRestTemplate;
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
        if (isBlank(from)) {
            from = "auto";
        }
        String salt = randomNumeric(10);
        String sign = md5Hex(baiduProperties.getAppId() + q + salt + baiduProperties.getSecretKey());
        String url = format("https://fanyi-api.baidu.com/api/trans/vip/translate?q=%s&from=%s&to=%s&appid=%s&salt=%s&sign=%s",
                q, from, to, baiduProperties.getAppId(), salt, sign);
        return restTemplate.getForObject(url, JSONObject.class);
    }

    public String pidSearch(String pid, String idx) {
        String url = String.format("https://h.pixiv.ddns-ip.net/%s-%s", pid, idx);
        try {
            // 发送GET请求并获取响应
            ResponseEntity<String> response = noRedirectRestTemplate.exchange(
                    url,
                    GET,
                    null,
                    String.class
            );

            // 检查是否为重定向响应
            if (response.getStatusCode().is3xxRedirection()) {
                HttpHeaders headers = response.getHeaders();
                URI location = headers.getLocation();

                if (location != null) {
                    // 解析相对路径为绝对URL
                    URI originalUri = new URI(url);
                    URI resolvedUri = originalUri.resolve(location);
                    return resolvedUri.toString();
                }
            }

            // 非重定向或缺少Location头时返回原URL
            return url;
        } catch (URISyntaxException e) {
            throw new MyException(INVALID_CMD_ARGS, "URL格式错误");
        } catch (HttpClientErrorException e) {
            // if ("超过该id图片数量上限".equals(e.getMessage())) {
            //     throw new MyException(PID_IDX_OUT_OF_RANGE, "超过该id图片数量上限");
            // } else if ("该图片不存在，或者缓存未刷新".equals(e.getMessage())) {
            //     throw new MyException(PIC_NOT_FOUND, "该图片不存在，或者缓存未刷新");
            // } else {
            //     throw e;
            // }
            throw new MyException(PIC_NOT_FOUND, "该图片不存在，或者缓存未刷新");
        }
    }

    public JSONObject hitokoto() {
        final String url = "https://v1.hitokoto.cn/";
        return restTemplate.getForObject(url, JSONObject.class);
    }

}

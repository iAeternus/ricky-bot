package org.ricky.core.weather.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.core.common.api.RestApis;
import org.ricky.core.weather.domain.LiveWeather;
import org.ricky.core.weather.domain.WeatherResponse;
import org.ricky.core.weather.service.WeatherService;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.ricky.common.constants.MsgConstants.INCORRECT_ARGS;
import static org.ricky.core.common.utils.ValidationUtil.isEmpty;
import static org.ricky.core.common.utils.ValidationUtil.isNull;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/20
 * @className WeatherServiceImpl
 * @desc
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final RestApis restApis;

    @Override
    public String getCurrentWeather(String city) {
        if(isNull(city)) {
            return INCORRECT_ARGS;
        }

        List<LiveWeather> liveWeathers = callApi(city);
        log.info("查询结果：{}", liveWeathers.toString());

        return buildMessage(liveWeathers);
    }

    private List<LiveWeather> callApi(String city) {
        WeatherResponse weatherResponse = restApis.getCurrentWeather(city);
        if(!weatherResponse.isValid()) {
            log.error("获取天气信息失败，状态码: {}, 信息: {}", weatherResponse.getStatus(), weatherResponse.getInfo());
            return List.of();
        }
        return weatherResponse.getLives();
    }

    private String buildMessage(List<LiveWeather> liveWeathers) {
        if(isEmpty(liveWeathers)) {
            return "查不到这个城市哦！";
        }
        LiveWeather liveWeather = liveWeathers.get(0);
        return String.format("%s当前天气%s，温度%s℃，%s风%s级，湿度%s%%，报告时间%s。",
                liveWeather.getCity(),
                liveWeather.getWeather(),
                liveWeather.getTemperature(),
                liveWeather.getWinddirection(),
                liveWeather.getWindpower(),
                liveWeather.getHumidity(),
                liveWeather.getReporttime());
    }
}

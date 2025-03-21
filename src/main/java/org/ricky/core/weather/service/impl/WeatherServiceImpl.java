package org.ricky.core.weather.service.impl;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.exception.MyException;
import org.ricky.core.common.api.RestApis;
import org.ricky.core.weather.domain.LiveWeather;
import org.ricky.core.weather.domain.WeatherResponse;
import org.ricky.core.weather.service.WeatherService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.ricky.common.constants.ErrorMsgConstants.*;
import static org.ricky.common.constants.SuccessMsgConstants.WEATHER_MSG;
import static org.ricky.common.exception.ErrorCodeEnum.*;
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
        List<LiveWeather> liveWeathers = callApi(city);
        log.info("查询结果：{}", liveWeathers);

        return buildMessage(liveWeathers);
    }

    private List<LiveWeather> callApi(String city) {
        WeatherResponse weatherResponse = restApis.getCurrentWeather(city);
        if (!weatherResponse.isValid()) {
            log.error("获取天气信息失败，状态码: {}, 信息: {}", weatherResponse.getStatus(), weatherResponse.getInfo());
            throw new MyException(RPC_FAILED, GET_WEATHER_FAILED_MSG);
        }
        return weatherResponse.getLives();
    }

    private String buildMessage(List<LiveWeather> liveWeathers) {
        if (isEmpty(liveWeathers)) {
            throw new MyException(CITY_NOT_FOUND, CITY_NOT_FOUND_MSG);
        }

        return liveWeathers.stream()
                .map(liveWeather -> String.format(WEATHER_MSG,
                        liveWeather.getProvince(),
                        liveWeather.getCity(),
                        liveWeather.getWeather(),
                        liveWeather.getTemperature(),
                        liveWeather.getWinddirection(),
                        liveWeather.getWindpower(),
                        liveWeather.getHumidity(),
                        liveWeather.getReporttime()))
                .collect(joining("\n", "匹配到的结果有：\n", ""));
    }
}

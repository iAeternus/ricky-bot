package org.ricky.core.weather.service.impl;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.exception.BotException;
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
    public String getCurrentWeather(String city, Bot bot, GroupMessageEvent evt) {
        if (isNull(city)) {
            throw new BotException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG, bot, evt);
        }

        List<LiveWeather> liveWeathers = callApi(city, bot, evt);
        log.info("查询结果：{}", liveWeathers);

        return buildMessage(liveWeathers, bot, evt);
    }

    private List<LiveWeather> callApi(String city, Bot bot, GroupMessageEvent evt) {
        WeatherResponse weatherResponse = restApis.getCurrentWeather(city);
        if (!weatherResponse.isValid()) {
            log.error("获取天气信息失败，状态码: {}, 信息: {}", weatherResponse.getStatus(), weatherResponse.getInfo());
            throw new BotException(RPC_FAILED, GET_WEATHER_FAILED_MSG, bot, evt);
        }
        return weatherResponse.getLives();
    }

    private String buildMessage(List<LiveWeather> liveWeathers, Bot bot, GroupMessageEvent evt) {
        if (isEmpty(liveWeathers)) {
            throw new BotException(CITY_NOT_FOUND, CITY_NOT_FOUND_MSG, bot, evt);
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

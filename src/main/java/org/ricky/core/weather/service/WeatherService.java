package org.ricky.core.weather.service;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/20
 * @className WeatherService
 * @desc
 */
public interface WeatherService {

    String getCurrentWeather(String city, Bot bot, GroupMessageEvent evt);

}

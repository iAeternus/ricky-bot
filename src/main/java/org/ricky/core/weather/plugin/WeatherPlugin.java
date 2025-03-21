package org.ricky.core.weather.plugin;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.exception.handler.HandleException;
import org.ricky.common.exception.handler.impl.BotExceptionHandler;
import org.ricky.core.weather.service.WeatherService;
import org.springframework.stereotype.Component;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.common.constants.CmdConstants.WEATHER_CMD;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/20
 * @className WeatherPlugin
 * @desc
 */
@Slf4j
@Shiro
@Component
@RequiredArgsConstructor
public class WeatherPlugin {

    private final WeatherService weatherService;

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = WEATHER_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int handleWeather(Bot bot, GroupMessageEvent evt) {
        String city = parseArgs(evt.getMessage());
        log.info("查询城市：{}", city);

        String msg = weatherService.getCurrentWeather(city, bot, evt);

        String sendMsg = MsgUtils.builder()
                .at(evt.getUserId())
                .text("\n" + msg)
                .build();
        bot.sendGroupMsg(evt.getGroupId(), sendMsg, false);

        return MESSAGE_IGNORE;
    }

    private String parseArgs(String message) {
        String[] split = message.split(" ");
        if (split.length != 2) {
            return null;
        }
        return split[1];
    }

}

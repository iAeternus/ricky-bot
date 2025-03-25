package org.ricky.core.weather.plugin;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.exception.MyException;
import org.ricky.common.exception.handler.HandleException;
import org.ricky.common.exception.handler.impl.BotExceptionHandler;
import org.ricky.core.weather.service.WeatherService;
import org.springframework.stereotype.Component;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.common.constants.CmdConstants.WEATHER_CMD;
import static org.ricky.common.constants.ErrorMsgConstants.INVALID_CMD_ARGS_MSG;
import static org.ricky.common.exception.ErrorCodeEnum.INVALID_CMD_ARGS;
import static org.ricky.core.common.utils.BotUtil.parseArgs;
import static org.ricky.core.common.utils.ValidationUtil.isBlank;

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
        String city = parseArgs(WEATHER_CMD, evt.getMessage());
        if (isBlank(city)) {
            throw new MyException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG);
        }
        log.info("查询城市：{}", city);

        String msg = weatherService.getCurrentWeather(city);

        String sendMsg = MsgUtils.builder()
                .at(evt.getUserId())
                .text("\n" + msg)
                .build();
        bot.sendGroupMsg(evt.getGroupId(), sendMsg, false);

        return MESSAGE_IGNORE;
    }

}

package org.ricky.core.deepseek.plugin;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.exception.MyException;
import org.ricky.common.exception.handler.HandleException;
import org.ricky.common.exception.handler.impl.BotExceptionHandler;
import org.ricky.common.redis.PluginCallCounter;
import org.ricky.core.deepseek.service.DeepseekService;
import org.springframework.stereotype.Component;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.common.constants.CmdConstants.DEEP_SEEK_CMD;
import static org.ricky.common.constants.ErrorMsgConstants.INVALID_CMD_ARGS_MSG;
import static org.ricky.common.exception.ErrorCodeEnum.INVALID_CMD_ARGS;
import static org.ricky.core.common.utils.BotUtil.parseArgs;
import static org.ricky.core.common.utils.BotUtil.sendTextGroupMsg;
import static org.ricky.core.common.utils.ValidationUtil.isBlank;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className DeepseekPlugin
 * @desc
 */
@Shiro
@Slf4j
@Component
@RequiredArgsConstructor
public class DeepseekPlugin {

    private final DeepseekService deepseekService;
    private final PluginCallCounter pluginCallCounter;

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = DEEP_SEEK_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int handleWeather(Bot bot, GroupMessageEvent evt) {
        String message = parseArgs(DEEP_SEEK_CMD, evt.getMessage());
        if (isBlank(message)) {
            throw new MyException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG);
        }

        String ans = deepseekService.call(message);
        sendTextGroupMsg(ans);
        pluginCallCounter.decrementCnt();

        return MESSAGE_IGNORE;
    }

}

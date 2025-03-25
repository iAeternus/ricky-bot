package org.ricky.core.tongyi.plugin;

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
import org.ricky.core.tongyi.service.TongYiService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.common.constants.CmdConstants.TONG_YI_CMD;
import static org.ricky.common.constants.ErrorMsgConstants.INVALID_CMD_ARGS_MSG;
import static org.ricky.common.exception.ErrorCodeEnum.INVALID_CMD_ARGS;
import static org.ricky.core.common.utils.BotUtil.parseArgs;
import static org.ricky.core.common.utils.BotUtil.sendImgGroupMsg;
import static org.ricky.core.common.utils.ValidationUtil.isBlank;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className TongYiPlugin
 * @desc 通义万相模型插件
 */
@Shiro
@Slf4j
@Component
@RequiredArgsConstructor
public class TongYiPlugin {

    private final TongYiService tongYiService;
    private final PluginCallCounter pluginCallCounter;

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = TONG_YI_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int text2image(Bot bot, GroupMessageEvent evt) {
        String prompt = parseArgs(TONG_YI_CMD, evt.getMessage());
        if (isBlank(prompt)) {
            throw new MyException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG);
        }

        List<String> urls = tongYiService.text2image(prompt);
        log.info("urls: {}", urls);

        sendImgGroupMsg(urls);
        pluginCallCounter.decrementCnt();

        return MESSAGE_IGNORE;
    }

}

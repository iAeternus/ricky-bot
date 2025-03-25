package org.ricky.core.tongyi.plugin;

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
import org.ricky.core.tongyi.service.TongYiService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.common.constants.CmdConstants.*;
import static org.ricky.common.constants.SuccessMsgConstants.PLEASE_WAIT_MSG;
import static org.ricky.core.common.utils.BotUtil.sendImgGroupMsg;
import static org.ricky.core.common.utils.BotUtil.sendTextGroupMsg;

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

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = TONG_YI_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int text2image(Bot bot, GroupMessageEvent evt) {
        String prompt = parseArgs(evt.getMessage());

        List<String> urls = tongYiService.text2image(prompt);
        sendImgGroupMsg(urls);

        return MESSAGE_IGNORE;
    }

    private String parseArgs(String message) {
        return message.substring(TONG_YI_CMD.length());
    }

}

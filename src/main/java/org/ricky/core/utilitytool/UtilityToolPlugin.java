package org.ricky.core.utilitytool;

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
import org.ricky.core.utilitytool.domain.TranslationInfo;
import org.ricky.core.utilitytool.service.UtilityToolService;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.hutool.http.HtmlUtil.unescape;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static java.util.Arrays.stream;
import static org.ricky.common.exception.ErrorCodeEnum.INVALID_CMD_ARGS;
import static org.ricky.core.common.constants.CmdConstants.*;
import static org.ricky.core.common.constants.ErrorMsgConstants.INVALID_CMD_ARGS_MSG;
import static org.ricky.core.common.constants.SuccessMsgConstants.LANGUAGE_LIST;
import static org.ricky.core.common.utils.BotUtil.*;
import static org.ricky.core.common.utils.ValidationUtil.isBlank;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/26
 * @className UtilityToolPlugin
 * @desc 实用工具
 */
@Slf4j
@Shiro
@Component
@RequiredArgsConstructor
public class UtilityToolPlugin {

    private final UtilityToolService utilityToolService;

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = WEATHER_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int weather(Bot bot, GroupMessageEvent evt) {
        String city = parseArgs(WEATHER_CMD, evt.getMessage());
        log.info("查询城市：{}", city);

        if (isBlank(city)) {
            throw new MyException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG);
        }

        String msg = utilityToolService.weather(city);
        sendTextGroupMsg(msg);

        return MESSAGE_IGNORE;
    }

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = DEEP_SEEK_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int deepseekChat(Bot bot, GroupMessageEvent evt) {
        String message = parseArgs(DEEP_SEEK_CMD, evt.getMessage());
        if (isBlank(message)) {
            throw new MyException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG);
        }

        String ans = utilityToolService.deepseekChat(message);
        sendTextGroupMsg(ans);

        return MESSAGE_IGNORE;
    }

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = TONG_YI_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int text2image(Bot bot, GroupMessageEvent evt) {
        String prompt = parseArgs(TONG_YI_CMD, evt.getMessage());
        if (isBlank(prompt)) {
            throw new MyException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG);
        }

        List<String> urls = utilityToolService.text2image(prompt);
        sendImgGroupMsg(urls);

        return MESSAGE_IGNORE;
    }

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = TRANSLATION_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int translation(Bot bot, GroupMessageEvent evt) {
        String msg = parseArgs(TRANSLATION_CMD, evt.getMessage());
        if (isBlank(msg)) {
            sendTextGroupMsg(LANGUAGE_LIST);
            return MESSAGE_IGNORE;
        }

        List<String> args = stream(msg.split(ROOT_CMD))
                .map(String::trim)
                .filter(arg -> !arg.isEmpty())
                .collect(toImmutableList());

        String res = utilityToolService.translation(TranslationInfo.of(args));
        sendTextGroupMsg(res);

        return MESSAGE_IGNORE;
    }

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = R_SENTENCE_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int randomSentence(Bot bot, GroupMessageEvent evt) {
        utilityToolService.randomSentence();
        return MESSAGE_IGNORE;
    }

}

package org.ricky.core.calctool;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.exception.handler.HandleException;
import org.ricky.common.exception.handler.impl.BotExceptionHandler;
import org.ricky.core.calctool.domain.DerivativeInfo;
import org.ricky.core.calctool.domain.IntegralInfo;
import org.ricky.core.calctool.domain.LimitInfo;
import org.ricky.core.calctool.service.CalcToolService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.core.common.constants.CmdConstants.*;
import static org.ricky.core.common.constants.SuccessMsgConstants.FUNCTION_HELP;
import static org.ricky.core.common.utils.BotUtil.parseArgs;
import static org.ricky.core.common.utils.BotUtil.sendTextGroupMsg;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className CalcToolPlugin
 * @desc
 */
@Slf4j
@Shiro
@Component
@RequiredArgsConstructor
public class CalcToolPlugin {

    private final CalcToolService calcToolService;

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = CALC_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int calcExpr(Bot bot, GroupMessageEvent evt) {
        String expr = parseArgs(CALC_CMD, evt.getMessage());

        String msg = calcToolService.eval(expr);
        sendTextGroupMsg(msg);

        return MESSAGE_IGNORE;
    }

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = INTEGRAL_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int calcIntegral(Bot bot, GroupMessageEvent evt) {
        String msg = parseArgs(INTEGRAL_CMD, evt.getMessage());
        List<String> args = Arrays.stream(msg.split(ROOT_CMD))
                .map(String::trim)
                .filter(arg -> !arg.isEmpty())
                .collect(toImmutableList());

        String res = calcToolService.calcIntegral(IntegralInfo.of(args));
        sendTextGroupMsg(res);

        return MESSAGE_IGNORE;
    }

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = DERIVATIVE_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int calcDerivative(Bot bot, GroupMessageEvent evt) {
        String msg = parseArgs(DERIVATIVE_CMD, evt.getMessage());
        List<String> args = Arrays.stream(msg.split(ROOT_CMD))
                .map(String::trim)
                .filter(arg -> !arg.isEmpty())
                .collect(toImmutableList());

        String res = calcToolService.calcDerivative(DerivativeInfo.of(args));
        sendTextGroupMsg(res);

        return MESSAGE_IGNORE;
    }

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = LIMIT_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int calcLimit(Bot bot, GroupMessageEvent evt) {
        String msg = parseArgs(LIMIT_CMD, evt.getMessage());
        List<String> args = Arrays.stream(msg.split(ROOT_CMD))
                .map(String::trim)
                .filter(arg -> !arg.isEmpty())
                .collect(toImmutableList());

        String res = calcToolService.calcLimit(LimitInfo.of(args));
        sendTextGroupMsg(res);

        return MESSAGE_IGNORE;
    }

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = FUNCTION_HELP_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int functionHelp(Bot bot, GroupMessageEvent evt) {
        sendTextGroupMsg(FUNCTION_HELP);
        return MESSAGE_IGNORE;
    }

}

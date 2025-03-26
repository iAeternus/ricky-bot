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
import org.ricky.core.calctool.service.CalcToolService;
import org.springframework.stereotype.Component;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.core.common.constants.CmdConstants.CALC_CMD;
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

}

package org.ricky.core.calc.plugin;

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
import org.ricky.core.calc.service.CalcExprService;
import org.springframework.stereotype.Component;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.common.constants.CmdConstants.CALC_CMD;
import static org.ricky.core.common.utils.ValidationUtil.isBlank;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className CalcExprPlugin
 * @desc
 */
@Slf4j
@Shiro
@Component
@RequiredArgsConstructor
public class CalcExprPlugin {

    private final CalcExprService calcExprService;

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = CALC_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int calcExpr(Bot bot, GroupMessageEvent evt) {
        String expr = parseArgs(evt.getMessage());
        if(isBlank(expr)) {
            return MESSAGE_IGNORE;
        }

        String msg = calcExprService.eval(expr);

        String sendMsg = MsgUtils.builder()
                .at(evt.getUserId())
                .text("\n" + msg)
                .build();
        bot.sendGroupMsg(evt.getGroupId(), sendMsg, false);

        return MESSAGE_IGNORE;
    }

    private String parseArgs(String message) {
        return message.substring(CALC_CMD.length());
    }

}

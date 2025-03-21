package org.ricky.common.exception.handler.impl;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.ricky.common.exception.BotException;
import org.ricky.common.exception.handler.ExceptionHandler;
import org.springframework.stereotype.Component;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className BotExceptionHandler
 * @desc
 */
@Component
public class BotExceptionHandler implements ExceptionHandler {
    @Override
    public int handle(BotException ex) {
        Bot bot = ex.getBot();
        GroupMessageEvent evt = (GroupMessageEvent) ex.getMessageEvent();

        String sendMsg = MsgUtils.builder()
                .at(evt.getUserId())
                .text("\n" + ex.getUserMessage())
                .build();
        bot.sendGroupMsg(evt.getGroupId(), sendMsg, false);
        return MESSAGE_IGNORE;
    }
}

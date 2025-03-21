package org.ricky.common.exception.handler.impl;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.ricky.common.context.ThreadLocalContext;
import org.ricky.common.exception.MyException;
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
    public int handle(MyException exception) {
        Bot bot = ThreadLocalContext.getContext().getBot();
        GroupMessageEvent evt = ThreadLocalContext.getContext().getEvt();

        String sendMsg = MsgUtils.builder()
                .at(evt.getUserId())
                .text("\n" + exception.getUserMessage())
                .build();
        bot.sendGroupMsg(evt.getGroupId(), sendMsg, false);
        return MESSAGE_IGNORE;
    }
}

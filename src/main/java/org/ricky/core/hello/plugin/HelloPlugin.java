package org.ricky.core.hello.plugin;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.ricky.common.exception.handler.HandleException;
import org.ricky.common.exception.handler.impl.BotExceptionHandler;
import org.springframework.stereotype.Component;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.common.constants.CmdConstants.HELLO_CMD;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/20
 * @className HelloPlugin
 * @desc
 */
@Shiro
@Component
public class HelloPlugin {

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = HELLO_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int sayHello(Bot bot, GroupMessageEvent evt) {
        String sendMsg = MsgUtils.builder()
                .at(evt.getUserId())
                .text("\nHello! This is ricky-bot.")
                .build();
        bot.sendGroupMsg(evt.getGroupId(), sendMsg, false);
        return MESSAGE_IGNORE;
    }

}

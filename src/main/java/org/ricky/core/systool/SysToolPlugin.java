package org.ricky.core.systool;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import org.ricky.common.exception.handler.HandleException;
import org.ricky.common.exception.handler.impl.BotExceptionHandler;
import org.ricky.common.properties.SystemProperties;
import org.springframework.stereotype.Component;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static java.lang.String.format;
import static org.ricky.core.common.constants.CmdConstants.HELLO_CMD;
import static org.ricky.core.common.constants.CmdConstants.MENU_CMD;
import static org.ricky.core.common.constants.SuccessMsgConstants.HELLO_MSG;
import static org.ricky.core.common.constants.SuccessMsgConstants.MENU_MSG;
import static org.ricky.core.common.utils.BotUtil.sendTextGroupMsg;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/26
 * @className SysToolPlugin
 * @desc 系统功能
 */
@Shiro
@Component
@RequiredArgsConstructor
public class SysToolPlugin {

    private final SystemProperties systemProperties;

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = HELLO_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int sayHello(Bot bot, GroupMessageEvent evt) {
        sendTextGroupMsg(HELLO_MSG);
        return MESSAGE_IGNORE;
    }

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = MENU_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int menu(Bot bot, GroupMessageEvent evt) {
        sendTextGroupMsg(format(MENU_MSG, systemProperties.getVersion(), systemProperties.getAuthor()));
        return MESSAGE_IGNORE;
    }
}

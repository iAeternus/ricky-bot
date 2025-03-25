package org.ricky.core.checkcmd.plugin;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import org.ricky.common.exception.MyException;
import org.ricky.common.exception.handler.HandleException;
import org.ricky.common.exception.handler.impl.BotExceptionHandler;
import org.springframework.stereotype.Component;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.common.constants.CmdConstants.ROOT_CMD;
import static org.ricky.common.constants.ErrorMsgConstants.CMD_NOT_FOUND_MSG;
import static org.ricky.common.exception.ErrorCodeEnum.CMD_NOT_FOUND;
import static org.ricky.core.common.utils.BotUtil.isValidCmd;
import static org.ricky.core.common.utils.BotUtil.parseArgs;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className CheckCmdPlugin
 * @desc
 */
@Shiro
@Component
@RequiredArgsConstructor
public class CheckCmdPlugin {

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = ROOT_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int checkCmd(Bot bot, GroupMessageEvent evt) {
        if(!isValidCmd(evt.getMessage())) {
            throw new MyException(CMD_NOT_FOUND, CMD_NOT_FOUND_MSG);
        }
        return MESSAGE_IGNORE;
    }

}

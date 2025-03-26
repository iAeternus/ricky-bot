package org.ricky.core.funny;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.exception.handler.HandleException;
import org.ricky.common.exception.handler.impl.BotExceptionHandler;
import org.ricky.core.funny.service.FunnyService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.core.common.constants.CmdConstants.RANDOM_PIC_CMD;
import static org.ricky.core.common.constants.CmdConstants.RANDOM_PIC_CMD2;
import static org.ricky.core.common.utils.BotUtil.*;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/26
 * @className FunnyPlugin
 * @desc
 */
@Slf4j
@Shiro
@Component
@RequiredArgsConstructor
public class FunnyPlugin {

    private final FunnyService funnyService;

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = RANDOM_PIC_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int randomPic(Bot bot, GroupMessageEvent evt) {
        String keyword = parseArgs(RANDOM_PIC_CMD, evt.getMessage());

        List<String> urls = funnyService.randomPic(keyword);
        sendImgGroupMsg(urls);

        return MESSAGE_IGNORE;
    }

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = RANDOM_PIC_CMD2)
    @HandleException(handler = BotExceptionHandler.class)
    public int randomPic2(Bot bot, GroupMessageEvent evt) {
        String keyword = parseArgs(RANDOM_PIC_CMD2, evt.getMessage());

        List<String> urls = funnyService.randomPic2(keyword);
        sendTextGroupMsg(urls.get(0));

        return MESSAGE_IGNORE;
    }

}

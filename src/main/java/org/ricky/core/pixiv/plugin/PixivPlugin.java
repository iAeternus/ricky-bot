package org.ricky.core.pixiv.plugin;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.exception.handler.HandleException;
import org.ricky.common.exception.handler.impl.BotExceptionHandler;
import org.ricky.common.redis.PluginCallCounter;
import org.ricky.core.pixiv.service.PixivService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.common.constants.CmdConstants.RANDOM_PIC_CMD;
import static org.ricky.core.common.utils.BotUtil.parseArgs;
import static org.ricky.core.common.utils.BotUtil.sendImgGroupMsg;
import static org.ricky.core.common.utils.ValidationUtil.isEmpty;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className PixivPlugin
 * @desc
 */
@Shiro
@Slf4j
@Component
@RequiredArgsConstructor
public class PixivPlugin {

    private final PixivService pixivService;
    private final PluginCallCounter pluginCallCounter;


    @GroupMessageHandler
    @MessageHandlerFilter(startWith = RANDOM_PIC_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int randomPic(Bot bot, GroupMessageEvent evt) {
        String keyword = parseArgs(RANDOM_PIC_CMD, evt.getMessage());

        List<String> urls = pixivService.randomPic(keyword);
        log.info("keyword: {}, urls: {}", keyword, urls);

        sendImgGroupMsg(urls);
        pluginCallCounter.decrementCnt();

        return MESSAGE_IGNORE;
    }


}

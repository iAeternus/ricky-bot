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
import org.ricky.core.common.utils.AesUtil;
import org.ricky.core.pixiv.config.PixivProperties;
import org.ricky.core.pixiv.service.PixivService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.common.constants.CmdConstants.RANDOM_PIC_CMD;
import static org.ricky.common.constants.CmdConstants.RANDOM_PIC_CMD2;
import static org.ricky.core.common.utils.BotUtil.*;

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
    private final PixivProperties pixivProperties;

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

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = RANDOM_PIC_CMD2)
    @HandleException(handler = BotExceptionHandler.class)
    public int randomPic2(Bot bot, GroupMessageEvent evt) throws Exception {
        if (!pixivProperties.isEnable()) {
            sendTextGroupMsg("暂不支持该项服务！");
            return MESSAGE_IGNORE;
        }

        String keyword = parseArgs(RANDOM_PIC_CMD2, evt.getMessage());
        String url = pixivService.randomPic2(keyword);
        log.info("keyword: {}, url: {}", keyword, url);

        String encrypt = AesUtil.encrypt(url, pixivProperties.getSecretKey());
        sendTextGroupMsg(encrypt);
        pluginCallCounter.decrementCnt();

        return MESSAGE_IGNORE;
    }

}

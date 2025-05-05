package org.ricky.core.funny;

import com.alibaba.fastjson2.JSONObject;
import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.notice.NoticeEvent;
import com.mikuac.shiro.dto.event.notice.PokeNoticeEvent;
import com.mikuac.shiro.handler.event.NotifyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.exception.handler.HandleException;
import org.ricky.common.exception.handler.impl.BotExceptionHandler;
import org.ricky.core.funny.domain.PokeInfo;
import org.ricky.core.funny.service.FunnyService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static java.util.Arrays.stream;
import static org.ricky.core.common.constants.CmdConstants.*;
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
    private final NotifyEvent notifyEvent;

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

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = PID_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int pidSearch(Bot bot, GroupMessageEvent evt) {
        String keyword = parseArgs(PID_CMD, evt.getMessage());

        String url = funnyService.pidSearch(keyword);
        sendImgGroupMsg(url);

        return MESSAGE_IGNORE;
    }

    /**
     * TODO
     * @see <a href="https://apifox.com/apidoc/shared/c3bab595-b4a3-429b-a873-cbbe6b9a1f6a/226659265e0">参考api</a>
     */
    @GroupMessageHandler
    @MessageHandlerFilter(startWith = POKE_CMD)
    @HandleException(handler = BotExceptionHandler.class)
    public int poke(Bot bot, GroupMessageEvent evt) {
        String msg = parseArgs(POKE_CMD, evt.getMessage());
        List<String> args = Arrays.stream(msg.split("次"))
                .map(String::trim)
                .filter(arg -> !arg.isEmpty())
                .collect(toImmutableList());

        PokeInfo info = PokeInfo.of(args);

        for(int i = 0; i < info.getTimes(); ++i) {
            // String pokeMsg = MsgUtils.builder()
            //         .poke(info.getUserId())
            //         .build();
            // // bot.sendGroupMsg(evt.getGroupId(), pokeMsg, false);
            // // evt.setMessage(pokeMsg);
            // bot.sendGroupNotice(evt.getGroupId(), pokeMsg);
            //
            //
            // PokeNoticeEvent pokeNoticeEvent = PokeNoticeEvent.builder()
            //         .groupId(evt.getGroupId())
            //         .userId(info.getUserId())
            //         .time(info.getTimes())
            //         .senderId(evt.getSender().getUserId())
            //         .build();


            JSONObject jsonObject = JSONObject.of("group_id", evt.getGroupId(), "user_id", info.getUserId());
            notifyEvent.poke(bot, jsonObject);
        }


        return MESSAGE_IGNORE;
    }

}

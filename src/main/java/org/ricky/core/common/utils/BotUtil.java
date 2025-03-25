package org.ricky.core.common.utils;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.ricky.core.common.context.ThreadLocalContext;

import java.util.List;

import static org.ricky.core.common.utils.ValidationUtil.isBlank;
import static org.ricky.core.common.utils.ValidationUtil.isEmpty;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className BotUtil
 * @desc
 */
public class BotUtil {

    /**
     * 发送文本群聊信息
     */
    public static void sendTextGroupMsg(String msg) {
        if(isBlank(msg)) {
            return;
        }

        Bot bot = ThreadLocalContext.getContext().getBot();
        GroupMessageEvent evt = ThreadLocalContext.getContext().getEvt();

        String sendMsg = MsgUtils.builder()
                .at(evt.getUserId())
                .text("\n" + msg)
                .build();
        bot.sendGroupMsg(evt.getGroupId(), sendMsg, false);
    }

    /**
     * 发送图片群聊信息
     */
    public static void sendImgGroupMsg(List<String> imgUrls) {
        if(isEmpty(imgUrls)) {
            return;
        }

        Bot bot = ThreadLocalContext.getContext().getBot();
        GroupMessageEvent evt = ThreadLocalContext.getContext().getEvt();

        String sendMsg = MsgUtils.builder()
                .at(evt.getUserId())
                .img(imgUrls.get(0))
                .build();
        bot.sendGroupMsg(evt.getGroupId(), sendMsg, false);
    }

}

package org.ricky.core.common.context;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.Value;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className GroupMsgContext
 * @desc
 */
@Value
public class GroupMsgContext {

    Bot bot;
    GroupMessageEvent evt;

    private GroupMsgContext(Bot bot, GroupMessageEvent evt) {
        this.bot = bot;
        this.evt = evt;
    }

    public static GroupMsgContext newInstance(Bot bot, GroupMessageEvent evt) {
        return new GroupMsgContext(bot, evt);
    }

}

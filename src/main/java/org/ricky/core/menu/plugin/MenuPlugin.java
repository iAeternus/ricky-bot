package org.ricky.core.menu.plugin;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import org.ricky.common.properties.SystemProperties;
import org.springframework.stereotype.Component;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.common.constants.CmdConstants.MENU_CMD;
import static org.ricky.common.constants.SuccessMsgConstants.MENU_MSG;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/20
 * @className MenuPlugin
 * @desc
 */
@Shiro
@Component
@RequiredArgsConstructor
public class MenuPlugin {

    private final SystemProperties systemProperties;

    @GroupMessageHandler
    @MessageHandlerFilter(startWith = MENU_CMD)
    public int sayHello(Bot bot, GroupMessageEvent evt) {
        String sendMsg = MsgUtils.builder()
                .at(evt.getUserId())
                .text("\n" + buildMsg())
                .build();
        bot.sendGroupMsg(evt.getGroupId(), sendMsg, false);
        return MESSAGE_IGNORE;
    }

    public String buildMsg() {
        return String.format(MENU_MSG, systemProperties.getVersion(), systemProperties.getAuthor());
    }

}

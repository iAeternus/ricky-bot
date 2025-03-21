package org.ricky.common.exception;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import lombok.Getter;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className BotException
 * @desc
 */
@Getter
public class BotException extends MyException {

    private final Bot bot;
    private final MessageEvent messageEvent;

    public BotException(ErrorCodeEnum code, String userMessage, Bot bot, MessageEvent messageEvent) {
        super(code, userMessage);
        this.bot = bot;
        this.messageEvent = messageEvent;
    }
}

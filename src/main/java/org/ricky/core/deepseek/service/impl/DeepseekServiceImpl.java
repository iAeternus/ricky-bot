package org.ricky.core.deepseek.service.impl;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import org.ricky.core.common.context.ThreadLocalContext;
import org.ricky.core.deepseek.service.DeepseekService;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.ricky.common.constants.SuccessMsgConstants.PLEASE_WAIT_MSG;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className DeepseekServiceImpl
 * @desc
 */
@Service
@RequiredArgsConstructor
public class DeepseekServiceImpl implements DeepseekService {

    private final OpenAiChatModel chatModel;

    private final Map<Long, Integer> callCnt = new HashMap<>();

    @Override
    public String call(String message) {
        Bot bot = ThreadLocalContext.getContext().getBot();
        GroupMessageEvent evt = ThreadLocalContext.getContext().getEvt();
        Long userId = evt.getUserId();

        if(callCnt.containsKey(userId)) {
            return "请勿重复提问！";
        }
        callCnt.put(userId, 1);

        String waitMsg = MsgUtils.builder()
                .at(evt.getUserId())
                .text("\n" + PLEASE_WAIT_MSG)
                .build();
        bot.sendGroupMsg(evt.getGroupId(), waitMsg, false);

        String ans = chatModel.call(message);
        callCnt.remove(userId);
        return ans;
    }
}

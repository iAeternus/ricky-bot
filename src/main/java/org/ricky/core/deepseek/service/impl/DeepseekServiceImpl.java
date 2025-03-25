package org.ricky.core.deepseek.service.impl;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import org.ricky.core.common.context.ThreadLocalContext;
import org.ricky.core.deepseek.service.DeepseekService;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.ricky.common.constants.SuccessMsgConstants.PLEASE_WAIT_MSG;
import static org.ricky.core.common.utils.BotUtil.sendTextGroupMsg;

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

    private final Map<Long, Integer> callCnt = new ConcurrentHashMap<>();

    @Override
    public String call(String message) {
        GroupMessageEvent evt = ThreadLocalContext.getContext().getEvt();
        Long userId = evt.getUserId();

        if (callCnt.containsKey(userId)) {
            return "请勿重复提问！";
        }

        sendTextGroupMsg(String.format(PLEASE_WAIT_MSG, "deepseek"));

        callCnt.put(userId, 1);
        String ans = chatModel.call(message);
        callCnt.remove(userId);

        return "内容由 AI 生成，请仔细甄别\n" + ans;
    }
}

package org.ricky.core.deepseek.service.impl;

import lombok.RequiredArgsConstructor;
import org.ricky.common.redis.PluginCallCounter;
import org.ricky.core.deepseek.service.DeepseekService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import static org.ricky.common.constants.ConfigConstant.DEEP_SEEK_MODEL;
import static org.ricky.common.constants.SuccessMsgConstants.PLEASE_DO_NOT_REPEAT_MSG;
import static org.ricky.common.constants.SuccessMsgConstants.PLEASE_WAIT_FOR_GEN_MSG;
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

    private final ChatModel chatModel;
    private final PluginCallCounter pluginCallCounter;

    @Override
    public String call(String message) {
        if (pluginCallCounter.isCalling()) {
            sendTextGroupMsg(PLEASE_DO_NOT_REPEAT_MSG);
            return "";
        }

        sendTextGroupMsg(String.format(PLEASE_WAIT_FOR_GEN_MSG, DEEP_SEEK_MODEL));

        pluginCallCounter.incrementCnt();
        String ans = chatModel.call(message);
        return "内容由 AI 生成，请仔细甄别\n" + ans;
    }
}

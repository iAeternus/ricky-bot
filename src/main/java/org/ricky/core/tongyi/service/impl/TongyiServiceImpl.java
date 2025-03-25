package org.ricky.core.tongyi.service.impl;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.redis.ModelCallCounter;
import org.ricky.core.common.context.ThreadLocalContext;
import org.ricky.core.tongyi.config.TongyiProperties;
import org.ricky.core.tongyi.service.TongYiService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Collections.emptyList;
import static org.ricky.common.constants.ConfigConstant.TONGYI_MODEL;
import static org.ricky.common.constants.SuccessMsgConstants.PLEASE_DO_NOT_REPEAT_MSG;
import static org.ricky.common.constants.SuccessMsgConstants.PLEASE_WAIT_MSG;
import static org.ricky.core.common.utils.BotUtil.sendTextGroupMsg;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className TongyiServiceImpl
 * @desc
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TongyiServiceImpl implements TongYiService {

    private final TongyiProperties tongYiProperties;
    private final ModelCallCounter modelCallCounter;

    private static final String URL = "url";

    @Override
    public List<String> text2image(String prompt) {
        if (modelCallCounter.isCalling()) {
            sendTextGroupMsg(PLEASE_DO_NOT_REPEAT_MSG);
            return emptyList();
        }

        sendTextGroupMsg(String.format(PLEASE_WAIT_MSG, TONGYI_MODEL));

        modelCallCounter.incrementCnt();
        List<String> res = asyncCall(prompt);
        modelCallCounter.decrementCnt();

        return res;
    }

    private List<String> asyncCall(String prompt) {
        String taskId = createAsyncTask(prompt);
        return waitAsyncTask(taskId);
    }

    /**
     * 创建异步任务
     *
     * @return taskId
     */
    public String createAsyncTask(String prompt) {
        ImageSynthesisParam param = buildImageSynthesisParam(prompt);

        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result;
        try {
            result = imageSynthesis.asyncCall(param);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        log.info("result: {}", JsonUtils.toJson(result));
        return result.getOutput().getTaskId();
    }

    private ImageSynthesisParam buildImageSynthesisParam(String prompt) {
        return ImageSynthesisParam.builder()
                .apiKey(tongYiProperties.getApiKey())
                .model(tongYiProperties.getModel())
                .prompt(prompt)
                .n(tongYiProperties.getN())
                .size(tongYiProperties.getPicSize())
                .build();
    }

    /**
     * 等待异步任务结束
     *
     * @param taskId 任务id
     */
    private List<String> waitAsyncTask(String taskId) {
        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result;
        try {
            result = imageSynthesis.wait(taskId, tongYiProperties.getApiKey());
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result.getOutput().getResults().stream()
                .map(mp -> mp.get(URL))
                .collect(toImmutableList());
    }

}

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
import org.ricky.core.common.context.ThreadLocalContext;
import org.ricky.core.common.utils.BotUtil;
import org.ricky.core.tongyi.config.TongYiProperties;
import org.ricky.core.tongyi.service.TongYiService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Collections.emptyList;
import static org.ricky.common.constants.SuccessMsgConstants.PLEASE_WAIT_MSG;
import static org.ricky.core.common.utils.BotUtil.sendTextGroupMsg;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className TongYiServiceImpl
 * @desc
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TongYiServiceImpl implements TongYiService {

    private final TongYiProperties tongYiProperties;

    private static final String URL = "url";

    private final Map<Long, Integer> callCnt = new ConcurrentHashMap<>();

    @Override
    public List<String> text2image(String prompt) {
        GroupMessageEvent evt = ThreadLocalContext.getContext().getEvt();
        Long userId = evt.getUserId();

        if (callCnt.containsKey(userId)) {
            BotUtil.sendTextGroupMsg("请勿重复提问！");
            return emptyList();
        }

        sendTextGroupMsg(String.format(PLEASE_WAIT_MSG, "通义大模型"));

        callCnt.put(userId, 1);
        List<String> res = asyncCall(prompt);
        callCnt.remove(userId);

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

package org.ricky.core.utilitytool.service.impl;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.exception.MyException;
import org.ricky.core.common.api.RestApis;
import org.ricky.core.utilitytool.config.TongyiProperties;
import org.ricky.core.utilitytool.domain.LiveWeather;
import org.ricky.core.utilitytool.domain.TranslationInfo;
import org.ricky.core.utilitytool.domain.WeatherResponse;
import org.ricky.core.utilitytool.service.UtilityToolService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.joining;
import static org.ricky.common.exception.ErrorCodeEnum.*;
import static org.ricky.core.common.constants.ConfigConstant.*;
import static org.ricky.core.common.constants.ErrorMsgConstants.*;
import static org.ricky.core.common.constants.SuccessMsgConstants.*;
import static org.ricky.core.common.utils.BotUtil.sendTextGroupMsg;
import static org.ricky.core.common.utils.ValidationUtil.isEmpty;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/20
 * @className UtilityToolServiceImpl
 * @desc
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UtilityToolServiceImpl implements UtilityToolService {

    private final RestApis restApis;
    private final ChatModel chatModel;
    private final TongyiProperties tongyiProperties;

    @Override
    public String weather(String city) {
        WeatherResponse weatherResponse = restApis.getCurrentWeather(city);
        if (!weatherResponse.isValid()) {
            log.error("获取天气信息失败，状态码: {}, 信息: {}", weatherResponse.getStatus(), weatherResponse.getInfo());
            throw new MyException(RPC_FAILED, GET_WEATHER_FAILED_MSG);
        }

        List<LiveWeather> lives = weatherResponse.getLives();
        log.info("查询结果：{}", lives);

        if (isEmpty(lives)) {
            throw new MyException(CITY_NOT_FOUND, CITY_NOT_FOUND_MSG);
        }

        return lives.stream()
                .map(weather -> String.format(WEATHER_MSG,
                        weather.getProvince(),
                        weather.getCity(),
                        weather.getWeather(),
                        weather.getTemperature(),
                        weather.getWinddirection(),
                        weather.getWindpower(),
                        weather.getHumidity(),
                        weather.getReporttime()))
                .collect(joining("\n", "匹配到的结果有：\n", ""));
    }

    @Override
    public String deepseekChat(String message) {
        sendTextGroupMsg(String.format(PLEASE_WAIT_FOR_GEN_MSG, DEEP_SEEK_MODEL));
        String ans = chatModel.call(message);
        return GENERATED_BY_AI_MSG + ans;
    }

    @Override
    public List<String> text2image(String prompt) {
        sendTextGroupMsg(String.format(PLEASE_WAIT_FOR_GEN_MSG, TONGYI_MODEL));
        List<String> urls = asyncCall(prompt);
        log.info("通义万相文生图 urls: {}", urls);
        return urls;
    }

    @Override
    public String translation(TranslationInfo info) {
        JSONObject json = restApis.getTranslation(info.getQ(), info.getFrom(), info.getTo());
        checkTranslationApiCall(json);
        
        return Optional.ofNullable(json.getJSONArray("trans_result"))
                .filter(arr -> !arr.isEmpty())
                .map(transResults -> IntStream.range(0, transResults.size())
                        .mapToObj(i -> transResults.getJSONObject(i).getString("dst"))
                        .collect(Collectors.joining(" "))
                        .trim())
                .orElseThrow(() -> new MyException(NO_RESULT, NO_RESULT_MSG));
    }

    private void checkTranslationApiCall(JSONObject response) {
        if (!response.containsKey("error_code")) return;

        final String errorCode = response.getString("error_code");
        final String errorMsg = response.getString("error_msg");

        if ("58001".equals(errorCode)) {
            throw new MyException(API_CALL_FAILED, LANGUAGE_NOT_SUPPORTED,
                    "error_code", errorCode, "error_msg", errorMsg);
        }
        throw new MyException(API_CALL_FAILED, API_CALL_FAILED_MSG,
                "error_code", errorCode, "error_msg", errorMsg);
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
    private String createAsyncTask(String prompt) {
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
                .apiKey(tongyiProperties.getApiKey())
                .model(tongyiProperties.getModel())
                .prompt(prompt)
                .n(tongyiProperties.getN())
                .size(tongyiProperties.getPicSize())
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
            result = imageSynthesis.wait(taskId, tongyiProperties.getApiKey());
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result.getOutput().getResults().stream()
                .map(mp -> mp.get(URL))
                .collect(toImmutableList());
    }
}

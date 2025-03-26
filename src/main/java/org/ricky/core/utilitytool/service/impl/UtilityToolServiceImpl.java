package org.ricky.core.utilitytool.service.impl;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.exception.MyException;
import org.ricky.core.common.api.RestApis;
import org.ricky.core.utilitytool.config.PixivProperties;
import org.ricky.core.utilitytool.config.TongyiProperties;
import org.ricky.core.utilitytool.domain.LiveWeather;
import org.ricky.core.utilitytool.domain.WeatherResponse;
import org.ricky.core.utilitytool.service.UtilityToolService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static org.ricky.core.common.constants.ConfigConstant.*;
import static org.ricky.core.common.constants.ErrorMsgConstants.*;
import static org.ricky.core.common.constants.SuccessMsgConstants.*;
import static org.ricky.common.exception.ErrorCodeEnum.*;
import static org.ricky.core.common.utils.AesUtil.encrypt;
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
    private final PixivProperties pixivProperties;

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
    public List<String> randomPic(String keyword) {
        sendTextGroupMsg(String.format(PLEASE_WAIT_FOR_SEARCH_MSG, PIXIV));

        String json = restApis.getRandomPicByKeyword(keyword);

        Gson gson = new Gson();
        Type type = new TypeToken<List<JsonUrl>>() {
        }.getType();
        List<JsonUrl> objs = gson.fromJson(json, type);

        List<String> urls = objs.stream()
                .map(obj -> obj.url)
                .collect(toImmutableList());

        if (isEmpty(urls)) {
            sendTextGroupMsg(RANDOM_PIC_NOT_FOUND_MSG);
            return emptyList();
        }

        log.info("keyword: {}, urls: {}", keyword, urls);
        return urls;
    }

    @Override
    public List<String> randomPic2(String keyword) {
        if (!pixivProperties.isEnable()) {
            throw new MyException(NOT_SUPPORTED_THIS_SERVER, "暂不支持该项服务！");
        }

        sendTextGroupMsg(String.format(PLEASE_WAIT_FOR_SEARCH_MSG, PIXIV));

        String json = restApis.getRandomPicByKeyword2(keyword);
        Gson gson = new Gson();
        Type type = new TypeToken<List<JsonUrl>>() {
        }.getType();
        List<JsonUrl> objs = gson.fromJson(json, type);

        List<String> urls = objs.stream()
                .map(obj -> obj.url)
                .collect(toImmutableList());

        if (isEmpty(urls)) {
            sendTextGroupMsg(RANDOM_PIC_NOT_FOUND_MSG);
            return List.of();
        }

        log.info("keyword: {}, urls: {}", keyword, urls);
        return urls.stream()
                .map(url -> {
                    try {
                        return encrypt(url, pixivProperties.getSecretKey());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(toImmutableList());
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

    @Data
    private static class JsonUrl {
        private String url;
    }
}

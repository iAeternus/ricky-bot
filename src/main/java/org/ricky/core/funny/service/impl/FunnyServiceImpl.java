package org.ricky.core.funny.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ricky.common.exception.MyException;
import org.ricky.core.common.api.RestApis;
import org.ricky.core.funny.config.PixivProperties;
import org.ricky.core.funny.service.FunnyService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Collections.emptyList;
import static org.ricky.common.exception.ErrorCodeEnum.NOT_SUPPORTED_THIS_SERVER;
import static org.ricky.core.common.constants.ConfigConstant.PIXIV;
import static org.ricky.core.common.constants.ErrorMsgConstants.RANDOM_PIC_NOT_FOUND_MSG;
import static org.ricky.core.common.constants.SuccessMsgConstants.PLEASE_WAIT_FOR_SEARCH_MSG;
import static org.ricky.core.common.utils.AesUtil.encrypt;
import static org.ricky.core.common.utils.BotUtil.sendTextGroupMsg;
import static org.ricky.core.common.utils.ValidationUtil.isEmpty;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/26
 * @className FunnyServiceImpl
 * @desc
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FunnyServiceImpl implements FunnyService {

    private final RestApis restApis;
    private final PixivProperties pixivProperties;

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
            log.info("keyword: {}, urls: {}", keyword, urls);
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

    @Override
    public String pidSearch(String keyword) {
        String[] split = keyword.split("-");
        if (split.length == 1) {
            return restApis.pidSearch(split[0], "1");
        }
        return restApis.pidSearch(split[0], split[1]);
    }

    @Data
    private static class JsonUrl {
        private String url;
    }

}

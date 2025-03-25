package org.ricky.core.pixiv.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.ricky.common.redis.PluginCallCounter;
import org.ricky.core.common.api.RestApis;
import org.ricky.core.pixiv.service.PixivService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Collections.emptyList;
import static org.ricky.common.constants.ConfigConstant.PIXIV;
import static org.ricky.common.constants.ErrorMsgConstants.RANDOM_PIC_NOT_FOUND_MSG;
import static org.ricky.common.constants.SuccessMsgConstants.PLEASE_DO_NOT_REPEAT_MSG;
import static org.ricky.common.constants.SuccessMsgConstants.PLEASE_WAIT_FOR_SEARCH_MSG;
import static org.ricky.core.common.utils.BotUtil.sendTextGroupMsg;
import static org.ricky.core.common.utils.ValidationUtil.isEmpty;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className PixivServiceImpl
 * @desc
 */
@Service
@RequiredArgsConstructor
public class PixivServiceImpl implements PixivService {

    private final RestApis restApis;
    private final PluginCallCounter pluginCallCounter;

    @Override
    public List<String> randomPic(String keyword) {
        if (pluginCallCounter.isCalling()) {
            sendTextGroupMsg(PLEASE_DO_NOT_REPEAT_MSG);
            return emptyList();
        }

        sendTextGroupMsg(String.format(PLEASE_WAIT_FOR_SEARCH_MSG, PIXIV));

        pluginCallCounter.incrementCnt();
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

        return urls;
    }

    @Data
    private static class JsonUrl {
        private String url;
    }
}

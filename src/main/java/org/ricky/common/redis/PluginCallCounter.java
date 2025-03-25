package org.ricky.common.redis;

import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.RequiredArgsConstructor;
import org.ricky.common.exception.MyException;
import org.ricky.core.common.context.ThreadLocalContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static org.ricky.common.exception.ErrorCodeEnum.REDIS_ERROR;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className PluginCallCounter
 * @desc 模型调用计数器
 */
@Component
@RequiredArgsConstructor
public class PluginCallCounter {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 记录模型调用
     */
    public void incrementCnt() {
        String key = buildKey();
        try {
            stringRedisTemplate.opsForValue().increment(key, 1);
        } catch (Exception e) {
            throw new MyException(REDIS_ERROR, "Increment count failed!");
        }
    }

    /**
     * 结束模型调用
     */
    public void decrementCnt() {
        if (!isCalling()) {
            return;
        }

        String key = buildKey();
        try {
            stringRedisTemplate.opsForValue().increment(key, -1);
        } catch (Exception e) {
            throw new MyException(REDIS_ERROR, "Decrement count failed!");
        }
    }

    /**
     * 判断模型是否正在被调用
     *
     * @return true 如果正在调用，false 否则
     */
    public boolean isCalling() {
        String key = buildKey();
        try {
            String count = stringRedisTemplate.opsForValue().get(key);
            return count != null && Integer.parseInt(count) > 0;
        } catch (Exception e) {
            throw new MyException(REDIS_ERROR, "Judge calling count failed!");
        }
    }

    /**
     * 构建 Redis key
     *
     * @return Redis key
     */
    private String buildKey() {
        GroupMessageEvent evt = ThreadLocalContext.getContext().getEvt();
        if (evt == null) {
            throw new IllegalStateException("Event is not available in the current context");
        }
        Long groupId = evt.getGroupId();
        Long userId = evt.getUserId();
        if (groupId == null || userId == null) {
            throw new IllegalStateException("Group ID or User ID is not available in the current event");
        }
        return groupId + ":" + userId;
    }
}
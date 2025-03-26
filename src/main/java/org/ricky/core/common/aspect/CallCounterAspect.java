package org.ricky.core.common.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ricky.common.redis.PluginCallCounter;
import org.springframework.stereotype.Component;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static org.ricky.core.common.constants.SuccessMsgConstants.PLEASE_DO_NOT_REPEAT_MSG;
import static org.ricky.core.common.utils.BotUtil.sendTextGroupMsg;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/26
 * @className CallCounterAspect
 * @desc 调用计数切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CallCounterAspect {

    private final PluginCallCounter pluginCallCounter;

    @Pointcut("@annotation(com.mikuac.shiro.annotation.GroupMessageHandler)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object checkGroupId(ProceedingJoinPoint joinPoint) throws Throwable {
        if (pluginCallCounter.isCalling()) {
            log.info("Thread: {}", Thread.currentThread());
            sendTextGroupMsg(PLEASE_DO_NOT_REPEAT_MSG);
            return MESSAGE_IGNORE;
        }

        pluginCallCounter.incrementCnt();
        try {
            return joinPoint.proceed();
        } finally {
            pluginCallCounter.decrementCnt();
        }
    }

}

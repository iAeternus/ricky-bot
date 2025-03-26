package org.ricky.core.common.aspect;

import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ricky.common.exception.MyException;
import org.springframework.stereotype.Component;

import static com.mikuac.shiro.core.BotPlugin.MESSAGE_IGNORE;
import static java.util.Arrays.stream;
import static org.ricky.core.common.constants.ConfigConstant.EXCLUDED_GROUP_IDS;
import static org.ricky.common.exception.ErrorCodeEnum.INVALID_MEG_HANDLER_ARGS;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/26
 * @className ExcludeGroupIdAspect
 * @desc 群聊黑名单切面
 */
@Aspect
@Component
public class ExcludeGroupIdAspect {

    @Pointcut("@annotation(com.mikuac.shiro.annotation.GroupMessageHandler)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object checkGroupId(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length != 2) {
            throw new MyException(INVALID_MEG_HANDLER_ARGS, "The plugin processor parameter is incorrect", "args", args);
        }

        GroupMessageEvent evt = (GroupMessageEvent) args[1];
        if (stream(EXCLUDED_GROUP_IDS).anyMatch(groupId -> groupId.equals(evt.getGroupId()))) {
            return MESSAGE_IGNORE;
        }

        return joinPoint.proceed();
    }

}

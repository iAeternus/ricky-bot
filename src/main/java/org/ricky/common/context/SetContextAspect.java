package org.ricky.common.context;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.ricky.common.exception.MyException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

import static org.ricky.common.exception.ErrorCodeEnum.INVALID_MEG_HANDLER_ARGS;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className SetContextAspect
 * @desc
 */
@Aspect
@Component
public class SetContextAspect {

    @Pointcut("@annotation(com.mikuac.shiro.annotation.GroupMessageHandler)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object executeWithContext(ProceedingJoinPoint joinPoint) throws Throwable {
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] argValues = joinPoint.getArgs();
        if(argNames.length != 2 || argValues.length != 2) {
            throw new MyException(INVALID_MEG_HANDLER_ARGS, "The plugin processor parameter is incorrect",
                    Map.of("argNames", argNames, "argValues", argValues));
        }

        ThreadLocalContext.setContext(GroupMsgContext.newInstance((Bot) argValues[0], (GroupMessageEvent) argValues[1]));
        Object proceed = joinPoint.proceed();
        ThreadLocalContext.removeContext();

        return proceed;
    }

}

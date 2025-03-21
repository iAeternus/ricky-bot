package org.ricky.common.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.ricky.common.exception.MyError;
import org.ricky.common.exception.MyException;
import org.ricky.common.spring.SpringApplicationContext;
import org.ricky.common.tracing.TracingService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

import static org.ricky.common.exception.ErrorCodeEnum.*;
import static org.ricky.core.common.utils.ValidationUtil.notNull;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className BotExceptionHandlerAspect
 * @desc
 */
@Slf4j
@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
public class BotExceptionHandlerAspect {

    private final TracingService tracingService;

    @Pointcut("@annotation(org.ricky.common.exception.handler.HandleException)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object executeWithExceptionHandle(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (MyException ex) {
            return handleBotException(joinPoint, ex);
        } catch (Exception ex) {
            log.error("不支持的异常类型：{}", ex.getClass());
            throw new MyException(UNSUPPORTED_EXCEPTION_TYPE, "不支持的异常类型", Map.of("exceptionClass", ex.getClass()));
        }
    }

    private int handleBotException(ProceedingJoinPoint joinPoint, MyException ex) {
        String methodName = joinPoint.getSignature().getName();
        String traceId = tracingService.currentTraceId();
        MyError error = new MyError(ex, "", traceId);
        log.error("Method: [{}], Error: [{}]", methodName, error);

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        HandleException handleException = method.getAnnotation(HandleException.class);
        if (notNull(handleException)) {
            Class<? extends ExceptionHandler> handlerClass = handleException.handler();
            try {
                ExceptionHandler handler = SpringApplicationContext.getBean(handlerClass);
                return handler.handle(ex);
            } catch (Exception handlerEx) {
                log.error("处理异常时出错：{}", handlerEx.getMessage());
                throw new MyException(HANDLING_ERROR, "处理异常时出错");
            }
        } else {
            log.error("方法 [{}] 上未找到 HandleException 注解", methodName);
            throw new MyException(ANNOTATION_NOT_FOUND, "未找到 HandleException 注解", Map.of("methodName", methodName));
        }
    }

}

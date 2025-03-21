package org.ricky.common.exception.handler;

import org.ricky.common.exception.MyException;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className ExceptionHandler
 * @desc 异常处理器
 */
public interface ExceptionHandler {

    /**
     * 处理BotException，构建异常消息
     *
     * @param ex 异常
     * @return plugin返回值
     */
    int handle(MyException ex);

}

package org.ricky.common.exception.handler;

import org.ricky.common.exception.BotException;
import org.ricky.common.exception.MyException;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className ExceptionHandler
 * @desc 异常处理器
 */
public interface ExceptionHandler {

    int handle(BotException ex);

}

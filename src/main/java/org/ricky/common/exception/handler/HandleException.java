package org.ricky.common.exception.handler;

import java.lang.annotation.*;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className HandleException
 * @desc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface HandleException {

    Class<? extends ExceptionHandler> handler();

}

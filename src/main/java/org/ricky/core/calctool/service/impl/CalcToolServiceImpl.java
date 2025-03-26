package org.ricky.core.calctool.service.impl;

import org.ricky.common.exception.MyException;
import org.ricky.core.calctool.domain.Expr;
import org.ricky.core.calctool.service.CalcToolService;
import org.springframework.stereotype.Service;

import static org.ricky.common.exception.ErrorCodeEnum.PARAMS_ERROR;
import static org.ricky.core.common.constants.ErrorMsgConstants.INVALID_CMD_ARGS_MSG;
import static org.ricky.core.common.utils.ValidationUtil.isBlank;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className CalcToolServiceImpl
 * @desc
 */
@Service
public class CalcToolServiceImpl implements CalcToolService {
    @Override
    public String eval(String expr) {
        if (isBlank(expr)) {
            throw new MyException(PARAMS_ERROR, INVALID_CMD_ARGS_MSG);
        }

        return expr + " = " + new Expr(expr).eval();
    }
}

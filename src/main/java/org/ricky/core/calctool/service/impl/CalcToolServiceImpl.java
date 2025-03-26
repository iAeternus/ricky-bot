package org.ricky.core.calctool.service.impl;

import lombok.RequiredArgsConstructor;
import org.matheclipse.core.eval.ExprEvaluator;
import org.ricky.common.exception.MyException;
import org.ricky.core.calctool.domain.DerivativeInfo;
import org.ricky.core.calctool.domain.Expr;
import org.ricky.core.calctool.domain.IntegralInfo;
import org.ricky.core.calctool.domain.LimitInfo;
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
@RequiredArgsConstructor
public class CalcToolServiceImpl implements CalcToolService {

    private final ExprEvaluator exprEvaluator;

    @Override
    public String eval(String expr) {
        if (isBlank(expr)) {
            throw new MyException(PARAMS_ERROR, INVALID_CMD_ARGS_MSG);
        }

        return expr + " = " + new Expr(expr).eval();
    }

    @Override
    public String calcIntegral(IntegralInfo info) {
        return exprEvaluator.eval(info.isDefiniteIntegral() ? info.definiteIntegralExpr() : info.indefiniteIntegralExpr()).toString();
    }

    @Override
    public String calcDerivative(DerivativeInfo info) {
        return exprEvaluator.eval(info.derivativeExpr()).toString();
    }

    @Override
    public String calcLimit(LimitInfo info) {
        return exprEvaluator.eval(info.limitExpr()).toString();
    }

}

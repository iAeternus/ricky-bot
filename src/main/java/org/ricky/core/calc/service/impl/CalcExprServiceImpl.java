package org.ricky.core.calc.service.impl;

import org.ricky.core.calc.domain.Expr;
import org.ricky.core.calc.service.CalcExprService;
import org.springframework.stereotype.Service;

import static org.ricky.core.common.utils.ValidationUtil.requireNotBlank;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className CalcExprServiceImpl
 * @desc
 */
@Service
public class CalcExprServiceImpl implements CalcExprService {
    @Override
    public String eval(String expr) {
        requireNotBlank(expr, "Expr must not be blank.");

        return expr + " = " + new Expr(expr).eval();
    }
}

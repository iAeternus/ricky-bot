package org.ricky.core.calctool.service;

import org.ricky.core.calctool.domain.DerivativeInfo;
import org.ricky.core.calctool.domain.IntegralInfo;
import org.ricky.core.calctool.domain.LimitInfo;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className CalcToolService
 * @desc
 */
public interface CalcToolService {
    String eval(String expr);

    String calcIntegral(IntegralInfo info);

    String calcDerivative(DerivativeInfo info);

    String calcLimit(LimitInfo info);
}

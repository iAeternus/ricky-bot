package org.ricky.core.calctool.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.ricky.common.exception.MyException;

import java.util.List;

import static org.ricky.common.exception.ErrorCodeEnum.INVALID_CMD_ARGS;
import static org.ricky.core.common.constants.ErrorMsgConstants.INVALID_CMD_ARGS_MSG;
import static org.ricky.core.common.utils.ValidationUtil.isNotBlank;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/26
 * @className IntegralInfo
 * @desc 积分计算信息
 */
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegralInfo {

    String function;
    String variable;
    String lowerLimit;
    String upperLimit;

    public static IntegralInfo of(List<String> args) {
        if (args.size() != 2 && args.size() != 4) {
            throw new MyException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG);
        }

        return args.size() == 2 ?
                new IntegralInfo(args.get(0), args.get(1), null, null) :
                new IntegralInfo(args.get(0), args.get(1), args.get(2), args.get(3));
    }

    /**
     * 判断是否计算定积分
     */
    public boolean isDefiniteIntegral() {
        return isNotBlank(lowerLimit) && isNotBlank(upperLimit);
    }

    /**
     * 不定积分表达式
     */
    public String indefiniteIntegralExpr() {
        return String.format("Integrate(%s, %s)", function, variable);
    }

    /**
     * 定积分表达式
     */
    public String definiteIntegralExpr() {
        return String.format("Integrate(%s, {%s, %s, %s})", function, variable, lowerLimit, upperLimit);
    }

}

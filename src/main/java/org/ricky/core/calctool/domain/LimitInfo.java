package org.ricky.core.calctool.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.ricky.common.exception.MyException;

import java.util.List;

import static org.ricky.common.exception.ErrorCodeEnum.INVALID_CMD_ARGS;
import static org.ricky.core.common.constants.ErrorMsgConstants.INVALID_CMD_ARGS_MSG;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/26
 * @className LimitInfo
 * @desc
 */
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LimitInfo {

    String function;
    String variable;
    String point;

    public static LimitInfo of(List<String> args) {
        if (args.size() != 3) {
            throw new MyException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG);
        }

        return new LimitInfo(args.get(0), args.get(1), args.get(2));
    }

    public String limitExpr() {
        return String.format("Limit(%s, %s -> %s)", function, variable, point);
    }

}

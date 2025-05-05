package org.ricky.core.funny.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.ricky.common.exception.MyException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static org.ricky.common.exception.ErrorCodeEnum.INVALID_CMD_ARGS;
import static org.ricky.core.common.constants.ErrorMsgConstants.INVALID_CMD_ARGS_MSG;
import static org.ricky.core.common.constants.RegexConstants.AT_CQ_REGEX;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/26
 * @className PokeInfo
 * @desc
 */
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PokeInfo {

    Long userId;
    Long times;

    static Pattern AT_CQ_PATTERN = Pattern.compile(AT_CQ_REGEX);

    public static PokeInfo of(List<String> args) {
        if (args.size() != 2) {
            throw new MyException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG);
        }

        try {
            long count = parseInt(args.get(0));
            long userId = parseInt(extractUserId(args.get(1)));
            return new PokeInfo(userId, count);
        } catch (Exception e) {
            throw new MyException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG);
        }
    }

    private static String extractUserId(String atCQ) {
        Matcher matcher = AT_CQ_PATTERN.matcher(atCQ);
        if (!matcher.find()) {
            throw new MyException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG);
        }
        return matcher.group(1);
    }

}

package org.ricky.core.utilitytool.domain;

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
 * @className TranslationInfo
 * @desc
 */
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TranslationInfo {

    /**
     * 查询文本
     */
    String q;

    /**
     * 源语言代码
     */
    String from;

    /**
     * 目标语言代码
     */
    String to;

    public static TranslationInfo of(List<String> args) {
        if(args.size() != 2 && args.size() != 3) {
            throw new MyException(INVALID_CMD_ARGS, INVALID_CMD_ARGS_MSG);
        }

        return args.size() == 2 ?
                new TranslationInfo(args.get(0), "", args.get(1)) :
                new TranslationInfo(args.get(0), args.get(1), args.get(2));
    }
}

package org.ricky.core.calc.plugin;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.ricky.common.constants.CmdConstants.CALC_CMD;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className CalcPlugin
 * @desc
 */
@Slf4j
@Shiro
@Component
public class CalcPlugin {

    // @GroupMessageHandler
    // @MessageHandlerFilter(startWith = CALC_CMD)
    // public int calcExpr()

}

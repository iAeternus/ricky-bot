package org.ricky.core.calctool.config;

import org.matheclipse.core.eval.ExprEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/26
 * @className SymjaConfig
 * @desc
 */
@Configuration
public class SymjaConfig {

    @Bean
    public ExprEvaluator exprEvaluator() {
        return new ExprEvaluator();
    }

}

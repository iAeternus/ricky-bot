package org.ricky.common.spring;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/4/29
 * @className SpringAIConfig
 * @desc
 */
@Configuration
public class SpringAIConfig {

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

}

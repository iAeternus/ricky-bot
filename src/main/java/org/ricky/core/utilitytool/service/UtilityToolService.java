package org.ricky.core.utilitytool.service;

import java.util.List;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/20
 * @className UtilityToolService
 * @desc
 */
public interface UtilityToolService {

    String weather(String city);

    String deepseekChat(String message);

    List<String> text2image(String prompt);

    List<String> randomPic(String keyword);

    List<String> randomPic2(String keyword);
}

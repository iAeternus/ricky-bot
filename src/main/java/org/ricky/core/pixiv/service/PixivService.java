package org.ricky.core.pixiv.service;

import java.util.List;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className PixivService
 * @desc
 */
public interface PixivService {
    List<String> randomPic(String keyword);

    String randomPic2(String keyword);
}

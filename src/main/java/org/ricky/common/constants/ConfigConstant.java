package org.ricky.common.constants;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/2/26
 * @className ConfigConstant
 * @desc 整个系统需要的配置常数，统一配置避免出现不匹配的情况
 */
public interface ConfigConstant {

    String CHINA_TIME_ZONE = "Asia/Shanghai";

    String DEEP_SEEK_MODEL = "deepseek";
    String TONGYI_MODEL = "通义大模型";

    String PIXIV = "p站查询";

    Long[] EXCLUDED_GROUP_IDS = new Long[] {
        515119476L,
    };

}

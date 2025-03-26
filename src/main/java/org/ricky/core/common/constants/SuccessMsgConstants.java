package org.ricky.core.common.constants;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/20
 * @className SuccessMsgConstants
 * @desc
 */
public interface SuccessMsgConstants {

    String HELLO_MSG = "Hello! This is ricky-bot.";

    String MENU_MSG = """
            ## Ricky-Bot 功能菜单

            1. 📢 公告
               - 最新版本：%s
               - 开发者：%s

            2. 💻 系统功能
               - 查看菜单：`#menu`
               - 打招呼：`#hello`

            3. 📅 实用工具
               - 查看天气：`#天气 [城市]`
               - deepseek聊天：`#ds [消息]`
               - 通义万相文生图：`#ty [文本]`
               - 随机涩图：`#rpic` 或 `#rpic [keyword]`
               - 文本翻译：`#trans #[文本] #[to]`（自动检测源语言）
               - 文本翻译2： `#trans #[文本] #[from] #[to]`
               - 目标语言列表：`#trans`

            4. 🔢 计算工具
               - 计算表达式：`#expr [表达式]`
                 支持加/减/乘/除/取模/求幂/取负操作
               - 原神圣遗物评分计算器：`#cg [角色] [佩戴位置]`（待开发）
               - 绝区零驱动盘评分计算器：`#cz [角色] [驱动盘号]`（待开发）

            ## 使用说明
            - 直接输入对应指令使用功能
            - 需要参数的指令请按照提示格式输入
            """;

    String WEATHER_MSG = "- %s省 %s 当前天气%s，温度%s℃，%s风%s级，湿度%s%%，报告时间%s。";

    String PLEASE_WAIT_FOR_GEN_MSG = "[%s] 正在生成中，请耐心等待！";
    String PLEASE_WAIT_FOR_SEARCH_MSG = "[%s] 正在查询中，请耐心等待！";

    String GENERATED_BY_AI_MSG = "内容由 AI 生成，请仔细甄别\n";

    String PLEASE_DO_NOT_REPEAT_MSG = "请勿重复调用！";

    String LANGUAGE_LIST = """
            ## 语言列表
            中文 zh      文言文 wyw     阿拉伯语 ara
            英语 en      希腊语 el      西班牙语 spa
            粤语 yue     荷兰语 nl      葡萄牙语 pt
            韩语 kor     波兰语 pl      意大利语 it
            日语 jp      丹麦语 dan     匈牙利语 hu
            法语 fra     越南语 vie     繁体中文 cht
            泰语 th      芬兰语 fin     保加利亚语 bul
            俄语 ru      捷克语 cs      爱沙尼亚语 est
            德语 de      瑞典语 swe     罗马尼亚语 rom
            斯洛文尼亚语 slo
            """;

}

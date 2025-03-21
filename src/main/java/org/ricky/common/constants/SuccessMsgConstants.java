package org.ricky.common.constants;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/20
 * @className SuccessMsgConstants
 * @desc
 */
public interface SuccessMsgConstants {

    String MENU_MSG = """
            ## Ricky-Bot 功能菜单

            1. 📢 公告
               - 最新版本：%s
               - 开发者：%s
               
            2. 💻 系统功能
               - 查看菜单：“#menu”
               - 打招呼：“#hello”
               
            3. 📅 实用工具
               - 查看天气：“#天气 [城市]”
               - deepseek聊天：“#ds [问题]”
            
            4. 🔢 计算工具
               - 计算表达式：“#expr [表达式]”
                 支持加/减/乘/除/取模/求幂/取负操作
               - 原神圣遗物评分计算器：“#cg [角色] [佩戴位置]”（待开发）
               - 绝区零驱动盘评分计算器：“#cz [角色] [驱动盘号]”（待开发）

            ## 使用说明
            - 直接输入对应指令使用功能
            - 需要参数的指令请按照提示格式输入
            """;

    String WEATHER_MSG = "- %s省 %s 当前天气%s，温度%s℃，%s风%s级，湿度%s%%，报告时间%s。";

    String PLEASE_WAIT_MSG = "正在生成中，请耐心等待！";

}

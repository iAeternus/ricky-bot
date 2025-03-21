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
               - 查看菜单：输入“#menu”查看此菜单
               - 打招呼：输入“#hello”打招呼
               
            3. 📅 日常工具
               - 查看天气：输入“#天气 [城市]”查看天气情况

            ## 使用说明
            - 直接输入对应指令使用功能
            - 需要参数的指令请按照提示格式输入
            """;

    String WEATHER_MSG = "%s当前天气%s，温度%s℃，%s风%s级，湿度%s%%，报告时间%s。";

}

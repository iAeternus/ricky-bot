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
            # Ricky-Bot 功能菜单

            ## 📢 公告
                        
            1. 最新版本：2.0.0
            2. 开发者：Ricky

            ## 💻 系统功能

            1. 查看菜单：`#menu`
            2. 打招呼：`#hello`

            ## 📅 实用工具

            1. 查看天气：`#天气 [城市]`
            2. deepseek 聊天：`#ds [消息]`
            3. 通义万相文生图：`#ty [文本]`
            4. 文本翻译（自动检测源语言）：`#trans #[文本] #[to]`
            5. 文本翻译2：`#trans #[文本] #[from] #[to]`
            6. 目标语言列表：`#trans`

            ## 🔢 计算工具

            1. 计算表达式：`#expr [表达式]`（支持 +|−|×|÷|%%|^|− 操作）
            2. 原神圣遗物评分计算器：`#cg [角色] [佩戴位置]`（待开发）
            3. 绝区零驱动盘评分计算器：`#cz [角色] [驱动盘号]`（待开发）
            4. 计算不定积分：`#积分 #[function] #[variable]`
            5. 计算定积分：`#积分 #[function] #[variable] #[积分下限] #[积分上限]`
            6. 计算导数：`#导数 #[function] #[variable]`
            7. 计算极限：`#极限 #[function] #[variable] #[point]`
            8. 函数格式：`#函数帮助`

            ## 😍 超有意思的小功能

            1. 随机涩图：`#rpic` 或 `#rpic [keyword]`
            2. pid查询：`#pid [pid]-[idx]`

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

    String FUNCTION_HELP = """
            ## 幂函数
            Sqrt(x)：根号x
            x^2：幂函数
                        
            ## 三角函数
            Sin(x)：正弦函数
            Cos(x)：余弦函数
            Tan(x)：正切函数
            ArcSin(x)：反正弦函数
            ArcCos(x)：反余弦函数
            ArcTan(x)：反正切函数
                        
            ## 指数和对数函数
            Exp(x)：自然指数函数
            Log(x)：自然对数函数
            Log10(x)：以 10 为底的对数函数
                        
            ## 特殊函数
            Gamma(x)：伽马函数
            BesselJ(n, x)：贝塞尔函数
            """;

}

package org.ricky.core.tongyi;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/25
 * @className TestWanx
 * @desc
 */
public class TestWanx {

    public static void main(String[] args) {
        TestWanx testWanx = new TestWanx();
        testWanx.asyncCall();
    }

    public void asyncCall() {
        System.out.println("---create task----");
        String taskId = this.createAsyncTask();
        System.out.println("---wait task done then return image url----");
        this.waitAsyncTask(taskId);
    }


    /**
     * 创建异步任务
     *
     * @return taskId
     */
    public String createAsyncTask() {
        String prompt = "一间有着精致窗户的花店，漂亮的木质门，摆放着花朵";
        ImageSynthesisParam param =
                ImageSynthesisParam.builder()
                        .apiKey("sk-7e262e47043a4050b13438f0c3923322")
                        .model("wanx2.1-t2i-turbo")
                        .prompt(prompt)
                        .n(1)
                        .size("1024*1024")
                        .build();

        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result;
        try {
            result = imageSynthesis.asyncCall(param);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(JsonUtils.toJson(result));
        String taskId = result.getOutput().getTaskId();
        System.out.println("taskId=" + taskId);
        return taskId;
    }


    /**
     * 等待异步任务结束
     *
     * @param taskId 任务id
     */
    public void waitAsyncTask(String taskId) {
        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            // 如果已经在环境变量中设置了 DASHSCOPE_API_KEY，wait()方法可将apiKey设置为null
            result = imageSynthesis.wait(taskId, "sk-7e262e47043a4050b13438f0c3923322");
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(JsonUtils.toJson(result));
        System.out.println(JsonUtils.toJson(result.getOutput()));
        System.out.println(JsonUtils.toJson(result.getOutput().getResults().get(0).get("url")));
    }

}

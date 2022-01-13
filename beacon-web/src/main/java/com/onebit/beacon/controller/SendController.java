package com.onebit.beacon.controller;

import com.onebit.beacon.handler.SmsHandler;
import com.onebit.beacon.pojo.TaskInfo;
import com.onebit.beacon.vo.BasicResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;

/**
 *  发送短信的业务由 web 请求而来，web 请求封装成 common 中的 pojo，并交由 handler 处理
 *  handler 需要与 pojo 交互，并且需要 support 进行支持
 */

@RestController
public class SendController {

    /*@Autowired
    private TencentSmsScript tencentSmsScript;

    // content是创建模板时需要填充占位符的地方
    public String sendSms(String phone, String content) {
        // 首先需要构造出一个信息参数对象
        // 这个对象需要在 common 中定义
        SmsParam smsParam = SmsParam.builder()
                .phones(new HashSet<>(Arrays.asList(phone)))
                .content(content)
                .build();
        // 接着需要用腾讯脚本发送
        // 需要注入腾讯脚本依赖，这个脚本需要定义在 handler 中
        return tencentSmsScript.send(smsParam);
    }*/

    // 因为需要将发送短信后的响应入库，所以我们需要在发送之后多做些操作
    // 那么就需要再来一个接口
    // 我们先将请求参数拼装成 TaskInfo，再由这个接口拆包，拼装成 SmsParam，调用腾讯脚本发消息，
    // 发完消息之后，由这个接口调用 dao，入库
    @Autowired
    private SmsHandler smsHandler;

    /**
     * 测试发送短信
     * @param phone 手机号
     * @return
     */
    @GetMapping("/sendSms")
    public BasicResultVO<Void> sendSms(String phone,String content,Long messageTemplateId ) {
        TaskInfo taskInfo = TaskInfo.builder()
                .receiver(new HashSet<>(Arrays.asList(phone)))
                .content(content)
                .messageTemplateId(messageTemplateId)
                .build();

        if (smsHandler.doHandler(taskInfo)) {
            return BasicResultVO.success("发送消息成功");
        }
        return BasicResultVO.fail();
    }

}

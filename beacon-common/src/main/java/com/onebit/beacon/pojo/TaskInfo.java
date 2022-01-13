package com.onebit.beacon.pojo;

import com.onebit.beacon.dto.ContentModel;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @Author: Onebit
 * @Date: 2022/1/12
 */
@Data
@Builder
public class TaskInfo {
    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 业务Id
     */
    private Long businessId;

    /**
     * 接收者
     */
    private Set<String> receiver;

    /**
     * 发送的Id类型
     */
    private Integer idType;

    /**
     * 发送渠道
     */
    private Integer sendChannel;

    /**
     * 模板类型
     */
    private Integer templateType;

    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 发送文案内容
     */
    // private String content;
    // 把每个模板内容定义成 json 不太好看，所以定义成一个个 ContentModel 类

    /**
     * 发送文案模型
     * message_template 表存储的 content 是 JSON (所有内容都会塞进去)
     * 不同的渠道要发送的内容不一样(比如发 push 会有 img，而短信没有)
     * 所以会有 ContentModel
     */
    private ContentModel contentModel;


    /**
     * 发送账号（邮件下可有多个发送账号、短信可有多个发送账号..）
     */
    private Integer sendAccount;

    /**
     * 消息去重时间 单位小时
     */
    private Integer deduplicationTime;

    /**
     * 是否夜间屏蔽
     * 0:不屏蔽
     * 1：屏蔽
     */
    private Integer isNightShield;

}

package com.onebit.beacon.handler.util;

import com.onebit.beacon.common.enums.ChannelType;
import com.onebit.beacon.common.enums.MessageType;
import com.onebit.beacon.common.domain.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
public class GroupIdMappingUtil {

    public static String getGroupIdByTaskInfo(TaskInfo taskInfo) {
        String channelCodeEn = ChannelType.getEnumByCode(taskInfo.getSendChannel()).getCode_en();
        String msgCodeEn = MessageType.getEnumByCode(taskInfo.getMsgType()).getCode_en();
        return channelCodeEn + "." + msgCodeEn;
    }

    /**
     * 获取所有的groupIds
     * (不同的渠道不同的消息类型拥有自己的groupId)
     */
    public static List<String> getAllGroupIds() {
        List<String> groupIds = new ArrayList<>();
        for (ChannelType channelType : ChannelType.values()) {
            for (MessageType messageType : MessageType.values()) {
                groupIds.add(channelType.getCode_en() + "." + messageType.getCode_en());
            }
        }
        return groupIds;
    }
}

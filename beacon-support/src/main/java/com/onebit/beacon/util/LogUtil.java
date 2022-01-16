package com.onebit.beacon.util;

import cn.monitor4all.logRecord.bean.LogDTO;
import cn.monitor4all.logRecord.service.CustomLogListener;
import com.alibaba.fastjson.JSON;
import com.onebit.beacon.domain.AnchorInfo;
import com.onebit.beacon.domain.LogParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 操作日志工具类
 * 日志分为：系统日志和操作日志。系统日志就是记录系统运行信息：error、warn、info 等，而操作日志，就是记录这一步操作干了说明。
 * @Author: Onebit
 * @Date: 2022/1/17
 */
@Slf4j
@Component
public class LogUtil extends CustomLogListener {
    /**
     * 使用了 @OperationLog 记录了之后，会回调到这个方法
     * @param logDTO
     * @throws Exception
     */
    @Override
    public void createLog(LogDTO logDTO) throws Exception {
        log.info(JSON.toJSONString(logDTO));
    }


    // 定义几个我们自己的操作日志方法

    /**
     * 记录 logParam
     * @param logParam
     */
    public static void print(LogParam logParam) {
        logParam.setTimestamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(logParam));
    }

    /**
     * 记录埋点信息
     * @param anchorInfo
     */
    public static void print(AnchorInfo anchorInfo) {
        anchorInfo.setTimestamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(anchorInfo));
    }

    /**
     * 上面两个方法的组合
     * @param logParam
     * @param anchorInfo
     */
    public static void print(LogParam logParam, AnchorInfo anchorInfo) {
        print(anchorInfo);
        print(logParam);
    }
}

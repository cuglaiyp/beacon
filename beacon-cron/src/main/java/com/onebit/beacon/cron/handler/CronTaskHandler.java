package com.onebit.beacon.cron.handler;

import com.onebit.beacon.cron.service.TaskHandler;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 定时任务处理类：
 * MessageTemplateController（startCron）-> xxl-job-admin -> CronTaskHandler -> TaskHandler      -> CrowdBatchTaskPending             -> SendService
 *                                                                              1. 读取文件          1. 单线程循环判断是否达到发送与阈值
 *                                                                              2. 往阻塞队列里塞     2. 是的话，组装参数，调用送
 * @Author: Onebit
 * @Date: 2022/2/18
 */
@Service
@Slf4j
public class CronTaskHandler {

    @Resource
    private TaskHandler taskHandler;

    @XxlJob("beaconJob")
    public void execute() {
        log.info("CronTaskHandler#execute messageTemplateId:{} cron exec!", XxlJobHelper.getJobParam());
        Long messageTemplateId = Long.valueOf(XxlJobHelper.getJobParam());
        taskHandler.handle(messageTemplateId);
    }
}

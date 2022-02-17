package com.onebit.beacon.cron.xxl.service;

import com.onebit.beacon.cron.xxl.entity.XxlJobGroup;
import com.onebit.beacon.cron.xxl.entity.XxlJobInfo;
import com.onebit.beacon.common.vo.BasicResultVO;

/**
 * 定时任务服务
 * @Author: Onebit
 * @Date: 2022/2/17
 */
public interface CronTaskService {
    /**
     * 新增/修改 定时任务
     * @param xxlJobInfo
     * @return 新增时返回任务Id，修改时无返回
     */
    BasicResultVO saveCronTask(XxlJobInfo xxlJobInfo);

    /**
     * 删除定时任务
     * @param taskId
     * @return
     */
    BasicResultVO deleteCronTask(Integer taskId);

    /**
     * 启动定时任务
     * @param taskId
     * @return
     */
    BasicResultVO startCronTask(Integer taskId);

    /**
     * 暂停定时任务
     * @param taskId
     * @return
     */
    BasicResultVO stopCronTask(Integer taskId);

    /**
     * 得到执行器Id
     * @param appName
     * @param title
     * @return
     */
    BasicResultVO getGroupId(String appName, String title);

    /**
     * 创建执行器
     * @param xxlJobGroup
     * @return
     */
    BasicResultVO createGroup(XxlJobGroup xxlJobGroup);
}

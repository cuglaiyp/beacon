package com.onebit.beacon.cron.xxl.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.onebit.beacon.common.constant.BeaconConstant;
import com.onebit.beacon.common.enums.RespStatusEnum;
import com.onebit.beacon.common.vo.BasicResultVO;
import com.onebit.beacon.cron.xxl.constant.XxlJobConstant;
import com.onebit.beacon.cron.xxl.entity.XxlJobGroup;
import com.onebit.beacon.cron.xxl.entity.XxlJobInfo;
import com.onebit.beacon.cron.xxl.enums.*;
import com.onebit.beacon.cron.xxl.service.CronTaskService;
import com.onebit.beacon.support.domain.MessageTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * xxl-job 工具类
 *
 * @Author: Onebit
 * @Date: 2022/2/17
 */
@Component
public class XxlJobUtil {

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.executor.jobHandlerName}")
    private String jobHandlerName;

    @Resource
    private CronTaskService cronTaskService;

    /**
     * 构建xxlJobInfo信息
     *
     * @param messageTemplate
     * @return
     */
    public XxlJobInfo buildXxlJobInfo(MessageTemplate messageTemplate) {
        String expectPushTime = messageTemplate.getExpectPushTime();
        if (expectPushTime.equals(String.valueOf(BeaconConstant.FALSE))) {
            // 如果没有指定cron表达式，说明立即执行(给到xxl-job延迟5秒的cron表达式)
            expectPushTime = DateUtil.format(DateUtil.offsetSecond(new Date(), XxlJobConstant.DELAY_TIME), BeaconConstant.CRON_FORMAT);
        }
        XxlJobInfo xxlJobInfo = XxlJobInfo.builder()
                .jobGroup(queryJobGroupId())
                .jobDesc(messageTemplate.getName())
                .author(messageTemplate.getCreator())
                .scheduleConf(expectPushTime)
                .scheduleType(ScheduleTypeEnum.CRON.name())
                .misfireStrategy(MisfireStrategyEnum.DO_NOTHING.name())
                .executorRouteStrategy(ExecutorRouteStrategyEnum.CONSISTENT_HASH.name())
                .executorHandler(XxlJobConstant.JOB_HANDLER_NAME)
                .executorParam(JSON.toJSONString(messageTemplate))
                .executorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION.name())
                .executorTimeout(XxlJobConstant.TIME_OUT)
                .executorFailRetryCount(XxlJobConstant.RETRY_COUNT)
                .glueType(GlueTypeEnum.BEAN.name())
                .triggerStatus(BeaconConstant.FALSE)
                .glueRemark(StrUtil.EMPTY)
                .glueSource(StrUtil.EMPTY)
                .alarmEmail(StrUtil.EMPTY)
                .childJobId(StrUtil.EMPTY).build();

        if (messageTemplate.getCronTaskId() != null) {
            xxlJobInfo.setId(messageTemplate.getCronTaskId());
        }
        return xxlJobInfo;
    }

    /**
     * 根据就配置文件的内容获取jobGroupId，没有则创建
     *
     * @return
     */
    private Integer queryJobGroupId() {
        BasicResultVO basicResultVO = cronTaskService.getGroupId(appName, jobHandlerName);
        if (basicResultVO.getData() == null) {
            XxlJobGroup xxlJobGroup = XxlJobGroup.builder().appname(appName).title(jobHandlerName).addressType(BeaconConstant.FALSE).build();
            if (RespStatusEnum.SUCCESS.getCode().equals(cronTaskService.createGroup(xxlJobGroup).getStatus())) {
                return (int) cronTaskService.getGroupId(appName, jobHandlerName).getData();
            }
        }
        return (Integer) basicResultVO.getData();
    }

}

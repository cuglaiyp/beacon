package com.onebit.beacon.handler.pending;

import cn.hutool.core.thread.ExecutorBuilder;
import com.onebit.beacon.handler.util.GroupIdMappingUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Onebit
 * @Date: 2022/1/14
 * <p>
 * 存储 以 发送渠道+消息类型 为 Id 的组 与 线程池 的关系
 */

@Component
public class TaskPendingHolder {

    private Map<String, ExecutorService> taskPendingHolder = new HashMap<>(32);

    private static List<String> allGroupIds = GroupIdMappingUtil.getAllGroupIds();

    /**
     * 线程池的参数
     */
    private Integer coreSize = 3;
    private Integer maxSize = 3;
    private Integer keepAlive = 60;
    private Integer queueSize = 100;

    // 执行顺序：TaskPendingHolder 构造方法 -> [Autowired] -> init(PostConstruct)
    @PostConstruct
    private void init() {
        for (String id : allGroupIds) {
            taskPendingHolder.put(id, ExecutorBuilder.create()
                    .setCorePoolSize(coreSize)
                    .setMaxPoolSize(maxSize)
                    .setKeepAliveTime(keepAlive, TimeUnit.SECONDS)
                    .setWorkQueue(new LinkedBlockingQueue<>(queueSize))
                    .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                    .build());
        }
    }

    public ExecutorService route(String id) {
        return taskPendingHolder.get(id);
    }
}

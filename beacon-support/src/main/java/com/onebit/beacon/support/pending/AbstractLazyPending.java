package com.onebit.beacon.support.pending;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Onebit
 * @Date: 2022/2/18
 */
@Slf4j
public abstract class AbstractLazyPending<T> {
    /**
     * 子类构造方法必须初始化该参数
     */
    protected PendingParam<T> pendingParam;

    /**
     * 批量装载任务
     */
    private List<T> tasks = new ArrayList<>();

    /**
     * 上次执行的时间
     */
    private Long lastHandleTime = System.currentTimeMillis();

    /**
     * 单线程消费 阻塞队列的数据
     */
    @PostConstruct
    public void initConsumePending() {
        ThreadUtil.newSingleExecutor().execute(() -> {
            BlockingQueue<T> queue = pendingParam.getQueue();
            while (true) {
                try {
                    T obj = queue.poll(pendingParam.getTimeThreshold(), TimeUnit.MILLISECONDS);
                    if (null != obj) {
                        tasks.add(obj);
                    }

                    // 处理条件：1. 数量超限 2. 时间超限
                    if (CollUtil.isNotEmpty(tasks) && dataReady()) {
                        List<T> taskRef = tasks;
                        // 清空 tasks
                        tasks = Lists.newArrayList();
                        lastHandleTime = System.currentTimeMillis();

                        // 具体执行逻辑
                        pendingParam.getExecutorService().execute(() -> this.handle(taskRef));
                    }
                } catch (Exception e) {
                    log.error("Pending#initConsumePending failed:{}", Throwables.getStackTraceAsString(e));
                }
            }
        });
    }

    /**
     * 1. 数量超限
     * 2. 时间超限
     * @return
     */
    private boolean dataReady() {
        return tasks.size() >= pendingParam.getNumThreshold() ||
                (System.currentTimeMillis() - lastHandleTime >= pendingParam.getTimeThreshold());
    }

    /**
     * 将元素放入阻塞队列中
     *
     * @param t
     */
    public void pending(T t) {
        try {
            pendingParam.getQueue().put(t);
        } catch (InterruptedException e) {
            log.error("Pending#pending error:{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 消费阻塞队列元素时的方法
     *
     * @param t
     */
    public void handle(List<T> t) {
        if (t.isEmpty()) {
            return;
        }
        try {
            doHandle(t);
        } catch (Exception e) {
            log.error("Pending#handle failed:{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 处理阻塞队列的元素 真正方法
     *
     * @param list
     */
    public abstract void doHandle(List<T> list);
}

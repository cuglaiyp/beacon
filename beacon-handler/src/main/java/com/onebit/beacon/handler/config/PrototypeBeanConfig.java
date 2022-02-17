package com.onebit.beacon.handler.config;

import com.onebit.beacon.handler.pending.Task;
import com.onebit.beacon.handler.receiver.Receiver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Author: Onebit
 * @Date: 2022/1/14
 */
@Configuration
public class PrototypeBeanConfig {

    /**
     * 线程池的任务是不能公用的
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Task task() {
        return new Task();
    }

    /**
     * 不同组的消费者也不一样，所以也要分开生成
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Receiver receiver() {
        return new Receiver();
    }

}

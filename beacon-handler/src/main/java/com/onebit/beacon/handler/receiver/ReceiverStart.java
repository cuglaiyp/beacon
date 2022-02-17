package com.onebit.beacon.handler.receiver;

import com.onebit.beacon.handler.util.GroupIdMappingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 生成与 Group 相同个数的 Receiver，每个 Receiver 的组号不同
 * @Author: Onebit
 * @Date: 2022/1/14
 */
@Component
public class ReceiverStart {

    @Autowired
    private ApplicationContext applicationContext;

    // 获取所有 GroupId
    private static List<String> allGroupIds = GroupIdMappingUtil.getAllGroupIds();

    // 生成对应个数的 Receiver
    // 执行顺序：ReceiverStart 构造方法 -> [Autowired] -> init(PostConstruct)
    @PostConstruct
    private void init() {
        for (String id : allGroupIds) {
            applicationContext.getBean(Receiver.class);
        }
    }

    // 给每个Receiver对象的consumer方法 @KafkaListener赋值相应的groupId

    private static final String RECEIVER_METHOD_NAME = "Receiver.consume";
    private static Integer index = 0;

    @Bean
    public static KafkaListenerAnnotationBeanPostProcessor.AnnotationEnhancer groupIdEnhancer() {
        return (attrs, element) -> {
            if (element instanceof Method) {
                String name = ((Method) element).getDeclaringClass().getSimpleName() + "." + ((Method) element).getName();
                if (RECEIVER_METHOD_NAME.equals(name)) {
                    attrs.put("groupId", allGroupIds.get(index));
                    index++;
                }
            }
            return attrs;
        };
    }
}

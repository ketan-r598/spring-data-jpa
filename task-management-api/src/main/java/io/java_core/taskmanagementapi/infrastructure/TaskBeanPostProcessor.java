package io.java_core.taskmanagementapi.infrastructure;

import io.java_core.taskmanagementapi.repository.TaskRepository;
import io.java_core.taskmanagementapi.service.TaskService;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class TaskBeanPostProcessor implements BeanPostProcessor {

    @Nullable
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if(bean instanceof TaskRepository || bean instanceof TaskService) {
            System.out.println("[BPP] - " + beanName + " is initializing...");
        }
        return bean;
    }

    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if(bean instanceof TaskRepository || bean instanceof TaskService) {
            System.out.println("[BPP] - " + beanName + " is ready to use...");
        }
        return bean;
    }
}

package com.aliuken.jobvacanciesapp.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    public static <T> T getBean(Class<T> bean) {
        return ApplicationContextUtil.context.getBean(bean);
    }

    public static <T> T getBean(String beanName, Class<T> bean) {
        return ApplicationContextUtil.context.getBean(beanName, bean);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	ApplicationContextUtil.context = applicationContext;
    }
}
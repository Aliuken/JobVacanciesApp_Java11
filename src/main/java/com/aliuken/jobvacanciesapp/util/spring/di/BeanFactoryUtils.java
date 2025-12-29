package com.aliuken.jobvacanciesapp.util.spring.di;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanFactoryUtils implements ApplicationContextAware {
	private static GenericApplicationContext genericApplicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BeanFactoryUtils.genericApplicationContext = (GenericApplicationContext) applicationContext;
	}

	public static GenericApplicationContext getGenericApplicationContext() {
		return BeanFactoryUtils.genericApplicationContext;
	}

	public static <T> @NonNull T getBean(final Class<T> beanClass) throws BeansException {
		final T bean = BeanFactoryUtils.genericApplicationContext.getBean(beanClass);
		return bean;
	}

	public static <T> @NonNull T getBean(final String beanName, final Class<T> beanClass) throws BeansException {
		final T bean = BeanFactoryUtils.genericApplicationContext.getBean(beanName, beanClass);
		return bean;
	}

	public static <T> @NonNull T getBean(final Class<T> beanClass, final Object... args) throws BeansException {
		final T bean = BeanFactoryUtils.genericApplicationContext.getBean(beanClass, args);
		return bean;
	}

	public static Object refreshBean(final String beanName) throws BeansException {
		final Object beanObject = BeanFactoryUtils.refreshBean(BeanFactoryUtils.genericApplicationContext, beanName);
		return beanObject;
	}

	public static Object refreshBean(final GenericApplicationContext genericApplicationContext, final String beanName) throws BeansException {
		if(genericApplicationContext != null && beanName != null) {
			final BeanDefinition beanDefinition = genericApplicationContext.getBeanDefinition(beanName);
			if(beanDefinition != null) {
				final Object beanObject = genericApplicationContext.getBean(beanName);
				if(beanObject != null) {
					final ConfigurableListableBeanFactory beanFactory = genericApplicationContext.getBeanFactory();
					if(genericApplicationContext.containsBeanDefinition(beanName)) {
						genericApplicationContext.removeBeanDefinition(beanName);
					}
					beanFactory.registerSingleton(beanName, beanObject);
					genericApplicationContext.registerBeanDefinition(beanName, beanDefinition);
					return beanObject;
				}
			}
		}
		return null;
	}

	public static Object replaceBean(final String beanName, final String newBeanName) throws BeansException {
		final Object newBeanObject = BeanFactoryUtils.replaceBean(BeanFactoryUtils.genericApplicationContext, beanName, newBeanName);
		return newBeanObject;
	}

	public static Object replaceBean(final GenericApplicationContext genericApplicationContext, final String beanName, final String newBeanName) throws BeansException {
		if(genericApplicationContext != null && beanName != null && newBeanName != null) {
			final BeanDefinition newBeanDefinition = genericApplicationContext.getBeanDefinition(newBeanName);
			if(newBeanDefinition != null) {
				final Object newBeanObject = genericApplicationContext.getBean(newBeanName);
				if(newBeanObject != null) {
					final ConfigurableListableBeanFactory beanFactory = genericApplicationContext.getBeanFactory();
					if(genericApplicationContext.containsBeanDefinition(beanName)) {
						genericApplicationContext.removeBeanDefinition(beanName);
					}
					beanFactory.registerSingleton(beanName, newBeanObject);
					genericApplicationContext.registerBeanDefinition(beanName, newBeanDefinition);
					return newBeanObject;
				}
			}
		}
		return null;
	}
}
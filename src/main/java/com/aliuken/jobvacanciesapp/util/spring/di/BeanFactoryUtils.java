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
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		BeanFactoryUtils.genericApplicationContext = (GenericApplicationContext) applicationContext;
	}

	public static GenericApplicationContext getGenericApplicationContext() {
		return BeanFactoryUtils.genericApplicationContext;
	}

	public static <T> @NonNull T getBean(final @NonNull Class<T> beanClass) throws BeansException {
		final T bean = BeanFactoryUtils.genericApplicationContext.getBean(beanClass);
		return bean;
	}

	public static <T> @NonNull T getBean(final @NonNull String beanName, final @NonNull Class<T> beanClass) throws BeansException {
		final T bean = BeanFactoryUtils.genericApplicationContext.getBean(beanName, beanClass);
		return bean;
	}

	public static <T> @NonNull T getBean(final @NonNull Class<T> beanClass, final Object @NonNull ... args) throws BeansException {
		final T bean = BeanFactoryUtils.genericApplicationContext.getBean(beanClass, args);
		return bean;
	}

	public static @NonNull Object refreshBean(final @NonNull String beanName) throws BeansException {
		final Object beanObject = BeanFactoryUtils.replaceBean(BeanFactoryUtils.genericApplicationContext, beanName, beanName);
		return beanObject;
	}

	public static @NonNull Object refreshBean(final @NonNull GenericApplicationContext genericApplicationContext, final @NonNull String beanName) throws BeansException {
		final Object beanObject = BeanFactoryUtils.replaceBean(genericApplicationContext, beanName, beanName);
		return beanObject;
	}

	public static @NonNull Object replaceBean(final @NonNull String beanName, final @NonNull String newBeanName) throws BeansException {
		final Object newBeanObject = BeanFactoryUtils.replaceBean(BeanFactoryUtils.genericApplicationContext, beanName, newBeanName);
		return newBeanObject;
	}

	public static @NonNull Object replaceBean(final @NonNull GenericApplicationContext genericApplicationContext, final @NonNull String beanName, final @NonNull String newBeanName) throws BeansException {
		final BeanDefinition newBeanDefinition = genericApplicationContext.getBeanDefinition(newBeanName);
		final Object newBeanObject = genericApplicationContext.getBean(newBeanName);
		final ConfigurableListableBeanFactory beanFactory = genericApplicationContext.getBeanFactory();
		if(genericApplicationContext.containsBeanDefinition(beanName)) {
			genericApplicationContext.removeBeanDefinition(beanName);
		}
		beanFactory.registerSingleton(beanName, newBeanObject);
		genericApplicationContext.registerBeanDefinition(beanName, newBeanDefinition);
		return newBeanObject;
	}
}
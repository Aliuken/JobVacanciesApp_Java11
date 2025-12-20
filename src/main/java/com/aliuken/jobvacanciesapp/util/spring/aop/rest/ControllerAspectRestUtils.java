package com.aliuken.jobvacanciesapp.util.spring.aop.rest;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

public class ControllerAspectRestUtils {

	private ControllerAspectRestUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	public static RequestMapping getRequestMapping(final JoinPoint joinPoint) {
		final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		final Method method = methodSignature.getMethod();

		final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		return requestMapping;
	}

	public static GetMapping getGetMapping(final JoinPoint joinPoint) {
		final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		final Method method = methodSignature.getMethod();

		final GetMapping getMapping = method.getAnnotation(GetMapping.class);
		return getMapping;
	}

	public static PostMapping getPostMapping(final JoinPoint joinPoint) {
		final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		final Method method = methodSignature.getMethod();

		final PostMapping postMapping = method.getAnnotation(PostMapping.class);
		return postMapping;
	}

	public static PutMapping getPutMapping(final JoinPoint joinPoint) {
		final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		final Method method = methodSignature.getMethod();

		final PutMapping putMapping = method.getAnnotation(PutMapping.class);
		return putMapping;
	}

	public static DeleteMapping getDeleteMapping(final JoinPoint joinPoint) {
		final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		final Method method = methodSignature.getMethod();

		final DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
		return deleteMapping;
	}
}

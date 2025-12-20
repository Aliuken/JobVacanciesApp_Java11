package com.aliuken.jobvacanciesapp.aop.aspect;

import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.spring.di.BeanFactoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.CannotCreateTransactionException;

/**
 * Class that contains the Advises used around services
 */
@Aspect
@Slf4j
public class ServiceAspect {

	@Pointcut("@annotation(com.aliuken.jobvacanciesapp.annotation.ServiceMethod)")
	private static final void serviceMethod(){}

	private static boolean isInsideSpecificService = false;
	private static boolean isInsideServiceSuperinterface = false;
	private static boolean isInsideSecurity = false;

	@Around("execution(public * com.aliuken.jobvacanciesapp.service.*.*(..)) && serviceMethod()")
	public Object adviseAroundExecutionInServices(final ProceedingJoinPoint joinPoint) throws Throwable {
		final Object result;
		if(!isInsideSpecificService && !isInsideServiceSuperinterface && !isInsideSecurity) {
			try {
				isInsideSpecificService = true;
				result = this.adviseAroundExecutionInServicesCommon(joinPoint);
			} finally {
				isInsideSpecificService = false;
			}
		} else {
			result = joinPoint.proceed();
		}
		return result;
	}

	@Around("execution(public * com.aliuken.jobvacanciesapp.service.superclass.*.*(..)) && serviceMethod()")
	public Object adviseAroundExecutionInServiceInterfaces(final ProceedingJoinPoint joinPoint) throws Throwable {
		final Object result;
		if(!isInsideSpecificService && !isInsideServiceSuperinterface && !isInsideSecurity) {
			try {
				isInsideServiceSuperinterface = true;
				result = this.adviseAroundExecutionInServicesCommon(joinPoint);
			} finally {
				isInsideServiceSuperinterface = false;
			}
		} else {
			result = joinPoint.proceed();
		}
		return result;
	}

	@Around("execution(public * com.aliuken.jobvacanciesapp.security.*.*(..)) && serviceMethod()")
	public Object adviseAroundExecutionInHandlers(final ProceedingJoinPoint joinPoint) throws Throwable {
		final Object result;
		if(!isInsideSpecificService && !isInsideServiceSuperinterface && !isInsideSecurity) {
			try {
				isInsideSecurity = true;
				result = this.adviseAroundExecutionInServicesCommon(joinPoint);
			} finally {
				isInsideSecurity = false;
			}
		} else {
			result = joinPoint.proceed();
		}
		return result;
	}

	/**
	 * Advise that is executed around the service methods
	 */
	private Object adviseAroundExecutionInServicesCommon(final ProceedingJoinPoint joinPoint) throws Throwable {
		Object result;
		try {
			result = joinPoint.proceed();
		} catch(final InvalidDataAccessApiUsageException | CannotCreateTransactionException specificException) {
			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(specificException);
			if("EntityManagerFactory is closed".equals(rootCauseMessage)) {
				if(log.isInfoEnabled()) {
					log.info("EntityManagerFactory is closed. A new EntityManagerFactory will be created.");
				}
				BeanFactoryUtils.refreshBean("dataSource");
				BeanFactoryUtils.refreshBean("entityManagerFactory");
				BeanFactoryUtils.refreshBean("transactionManager");
				BeanFactoryUtils.refreshBean("jpaContext");
				result = joinPoint.proceed();
			} else {
				throw specificException;
			}
		} catch(final Exception exception) {
			throw exception;
		}

		return result;
	}
}
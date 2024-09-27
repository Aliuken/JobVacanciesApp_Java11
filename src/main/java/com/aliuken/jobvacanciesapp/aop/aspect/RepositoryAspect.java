package com.aliuken.jobvacanciesapp.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.enumtype.ControllerDependentTraceType;
import com.aliuken.jobvacanciesapp.enumtype.RepositoryAspectOrigin;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.springcore.aop.logging.ControllerAspectLoggingUtils;
import com.aliuken.jobvacanciesapp.util.springcore.aop.logging.RepositoryAspectLoggingUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Class that contains the Advises used around DAOs/repositories
 */
@Aspect
@Slf4j
public class RepositoryAspect {

	@Pointcut("@annotation(com.aliuken.jobvacanciesapp.annotation.RepositoryMethod)")
	private static final void repositoryMethod(){}

	@Pointcut("@annotation(com.aliuken.jobvacanciesapp.annotation.LazyEntityRelationGetter)")
	private static final void lazyEntityRelationGetter(){}

	private static boolean insideSpecificRepository = false;
	private static boolean insideUpgradedJpaRepository = false;
	private static boolean insideLazyEntityRelationGetter = false;

	public static boolean getInsideSpecificRepository() {
		return insideSpecificRepository;
	}

	public static boolean getInsideUpgradedJpaRepository() {
		return insideUpgradedJpaRepository;
	}

	public static boolean getInsideLazyEntityRelationGetter() {
		return insideLazyEntityRelationGetter;
	}

	@Around("execution(public * com.aliuken.jobvacanciesapp.repository.*.*(..)) && repositoryMethod()")
	public Object adviseAroundExecutionInRepositories(final ProceedingJoinPoint joinPoint) throws Throwable {
		final Object result;
		if(!insideSpecificRepository && !insideUpgradedJpaRepository && !insideLazyEntityRelationGetter) {
			try {
				insideSpecificRepository = true;
				result = this.adviseAroundExecutionInRepositoriesOrEntityRelationsCommon(joinPoint);
			} finally {
				insideSpecificRepository = false;
			}
		} else {
			result = joinPoint.proceed();
		}
		return result;
	}

	@Around("execution(public * com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository.*(..)) && repositoryMethod()")
	public Object adviseAroundExecutionInUpgradedJpaRepository(final ProceedingJoinPoint joinPoint) throws Throwable {
		final Object result;
		if(!insideSpecificRepository && !insideUpgradedJpaRepository && !insideLazyEntityRelationGetter) {
			try {
				insideUpgradedJpaRepository = true;
				result = this.adviseAroundExecutionInRepositoriesOrEntityRelationsCommon(joinPoint);
			} finally {
				insideUpgradedJpaRepository = false;
			}
		} else {
			result = joinPoint.proceed();
		}
		return result;
	}

	@Around("execution(public * com.aliuken.jobvacanciesapp.model.entity.*.*(..)) && lazyEntityRelationGetter()")
	public Object adviseAroundLazyEntityRelationGetters(final ProceedingJoinPoint joinPoint) throws Throwable {
		final Object result;
		if(!insideSpecificRepository && !insideUpgradedJpaRepository && !insideLazyEntityRelationGetter) {
			try {
				insideLazyEntityRelationGetter = true;
				result = this.adviseAroundExecutionInRepositoriesOrEntityRelationsCommon(joinPoint);
			} finally {
				insideLazyEntityRelationGetter = false;
			}
		} else {
			result = joinPoint.proceed();
		}
		return result;
	}

	/**
	 * Advise that is executed around the DAO/repository methods or lazy entity relation getters
	 */
	private Object adviseAroundExecutionInRepositoriesOrEntityRelationsCommon(final ProceedingJoinPoint joinPoint) throws Throwable {
		final long inputTimeMillis = System.currentTimeMillis();

		final RepositoryAspectOrigin repositoryAspectOrigin = RepositoryAspectLoggingUtils.getRepositoryAspectOrigin();

		Object result;
		try {
			result = joinPoint.proceed();
		} finally {
			if(repositoryAspectOrigin != null) {
				final boolean isInsideController = ControllerAspect.getIsInsideController();
				final long timeInside = System.currentTimeMillis() - inputTimeMillis;

				final String dbTimeString;
				if(isInsideController) {
					final long dbTime = RepositoryAspectLoggingUtils.MDCgetDBTime() + timeInside;
					RepositoryAspectLoggingUtils.MDCputDBTime(dbTime);
					dbTimeString = StringUtils.getStringJoined(" [total db ts: ", String.valueOf(dbTime), " ms]");
				} else {
					dbTimeString = Constants.EMPTY_STRING;
				}

				if(log.isInfoEnabled()) {
					final String traceType = ControllerAspectLoggingUtils.getTraceType(ControllerDependentTraceType.DATABASE_TRACE);
					final String methodName = joinPoint.getSignature().getName();
					log.info(StringUtils.getStringJoined(traceType, repositoryAspectOrigin.name(), ". [", methodName, "] [db ts: ", String.valueOf(timeInside), " ms]", dbTimeString));
				}
			}
		}

		return result;
	}
}
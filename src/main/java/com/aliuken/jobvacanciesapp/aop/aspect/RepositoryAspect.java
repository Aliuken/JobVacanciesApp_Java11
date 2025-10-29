package com.aliuken.jobvacanciesapp.aop.aspect;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.enumtype.ControllerDependentTraceType;
import com.aliuken.jobvacanciesapp.enumtype.RepositoryAspectOrigin;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.spring.aop.logging.RepositoryAspectLoggingUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

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

	private static boolean isInsideSpecificRepository = false;
	private static boolean isInsideUpgradedJpaRepository = false;
	private static boolean isInsideLazyEntityRelationGetter = false;

	@Around("execution(public * com.aliuken.jobvacanciesapp.repository.*.*(..)) && repositoryMethod()")
	public Object adviseAroundExecutionInRepositories(final ProceedingJoinPoint joinPoint) throws Throwable {
		final Object result;
		if(!isInsideSpecificRepository && !isInsideUpgradedJpaRepository && !isInsideLazyEntityRelationGetter) {
			try {
				isInsideSpecificRepository = true;
				result = this.adviseAroundExecutionInRepositoriesOrEntityRelationsCommon(joinPoint);
			} finally {
				isInsideSpecificRepository = false;
			}
		} else {
			result = joinPoint.proceed();
		}
		return result;
	}

	@Around("execution(public * com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository.*(..)) && repositoryMethod()")
	public Object adviseAroundExecutionInUpgradedJpaRepository(final ProceedingJoinPoint joinPoint) throws Throwable {
		final Object result;
		if(!isInsideSpecificRepository && !isInsideUpgradedJpaRepository && !isInsideLazyEntityRelationGetter) {
			try {
				isInsideUpgradedJpaRepository = true;
				result = this.adviseAroundExecutionInRepositoriesOrEntityRelationsCommon(joinPoint);
			} finally {
				isInsideUpgradedJpaRepository = false;
			}
		} else {
			result = joinPoint.proceed();
		}
		return result;
	}

	@Around("execution(public * com.aliuken.jobvacanciesapp.model.entity.*.*(..)) && lazyEntityRelationGetter()")
	public Object adviseAroundLazyEntityRelationGetters(final ProceedingJoinPoint joinPoint) throws Throwable {
		final Object result;
		if(!isInsideSpecificRepository && !isInsideUpgradedJpaRepository && !isInsideLazyEntityRelationGetter) {
			try {
				isInsideLazyEntityRelationGetter = true;
				result = this.adviseAroundExecutionInRepositoriesOrEntityRelationsCommon(joinPoint);
			} finally {
				isInsideLazyEntityRelationGetter = false;
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

		final RepositoryAspectOrigin repositoryAspectOrigin = RepositoryAspect.getRepositoryAspectOrigin();

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
					final String traceType = ControllerAspect.getTraceType(ControllerDependentTraceType.DATABASE_TRACE);
					final String methodName = joinPoint.getSignature().getName();
					log.info(StringUtils.getStringJoined(traceType, repositoryAspectOrigin.name(), ". [", methodName, "] [db ts: ", String.valueOf(timeInside), " ms]", dbTimeString));
				}
			}
		}

		return result;
	}

	private static RepositoryAspectOrigin getRepositoryAspectOrigin() {
		final RepositoryAspectOrigin repositoryAspectOrigin;
		if(isInsideSpecificRepository && !isInsideUpgradedJpaRepository) {
			repositoryAspectOrigin = RepositoryAspectOrigin.SPECIFIC_JPA_REPO;
		} else if(isInsideUpgradedJpaRepository) {
			repositoryAspectOrigin = RepositoryAspectOrigin.UPGRADED_JPA_REPO;
		} else if(isInsideLazyEntityRelationGetter) {
			repositoryAspectOrigin = RepositoryAspectOrigin.LAZY_JPA_RELATION;
		} else {
			repositoryAspectOrigin = null;
		}
		return repositoryAspectOrigin;
	}
}
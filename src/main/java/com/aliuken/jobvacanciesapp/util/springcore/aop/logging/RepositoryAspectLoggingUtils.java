package com.aliuken.jobvacanciesapp.util.springcore.aop.logging;

import org.slf4j.MDC;

import com.aliuken.jobvacanciesapp.aop.aspect.RepositoryAspect;
import com.aliuken.jobvacanciesapp.enumtype.LoggingStats;
import com.aliuken.jobvacanciesapp.enumtype.RepositoryAspectOrigin;

/**
 * Utility class that contains the common methods used for logging in repositories
 */
public class RepositoryAspectLoggingUtils {

	private static final long ZERO_TIME = 0L;

	public static RepositoryAspectOrigin getRepositoryAspectOrigin() {
		final RepositoryAspectOrigin repositoryAspectOrigin;
		if(RepositoryAspect.getInsideSpecificRepository() && !RepositoryAspect.getInsideUpgradedJpaRepository()) {
			repositoryAspectOrigin = RepositoryAspectOrigin.SPECIFIC_JPA_REPO;
		} else if(RepositoryAspect.getInsideUpgradedJpaRepository()) {
			repositoryAspectOrigin = RepositoryAspectOrigin.UPGRADED_JPA_REPO;
		} else if(RepositoryAspect.getInsideLazyEntityRelationGetter()) {
			repositoryAspectOrigin = RepositoryAspectOrigin.LAZY_JPA_RELATION;
		} else {
			repositoryAspectOrigin = null;
		}
		return repositoryAspectOrigin;
	}

	public static long MDCgetDBTime() {
		long dbTime = MDCgetStatsTime(LoggingStats.DB_TIME);
		return dbTime;
	}

	public static void MDCputDBTime(final long dbTime) {
		MDCputStatsTime(LoggingStats.DB_TIME, dbTime);
	}

	public static long MDCgetGetEntityManagerNotCachedTime() {
		long getEntityManagerNotCachedTime = MDCgetStatsTime(LoggingStats.GET_ENTITY_MANAGER_NOT_CACHED_TIME);
		return getEntityManagerNotCachedTime;
	}

	public static void MDCputGetEntityManagerNotCachedTime(final long getEntityManagerNotCachedTime) {
		MDCputStatsTime(LoggingStats.GET_ENTITY_MANAGER_NOT_CACHED_TIME, getEntityManagerNotCachedTime);
	}

	private static long MDCgetStatsTime(final LoggingStats stats) {
		try {
			final String statsKey = stats.getKey();
			final String timeString = MDC.get(statsKey);

			final long time;
			if(timeString != null) {
				time = Long.valueOf(timeString);
			} else {
				time = ZERO_TIME;
			}
			return time;
		} catch(final Exception exception) {
			return ZERO_TIME;
		}
	}

	private static void MDCputStatsTime(final LoggingStats stats, final long time) {
		final String statsKey = stats.getKey();
		final String timeString = Long.toString(time);
		MDC.put(statsKey, timeString);
	}

}
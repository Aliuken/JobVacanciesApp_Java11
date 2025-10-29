package com.aliuken.jobvacanciesapp.util.spring.aop.logging;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.enumtype.LoggingStats;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import org.slf4j.MDC;

/**
 * Utility class that contains the common methods used for logging in repositories
 */
public class RepositoryAspectLoggingUtils {

	private static final long ZERO_TIME = 0L;

	private RepositoryAspectLoggingUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	public static long MDCgetDBTime() {
		long dbTime = MDCgetStatsTime(LoggingStats.DB_TIME);
		return dbTime;
	}

	public static void MDCputDBTime(final long dbTime) {
		MDCputStatsTime(LoggingStats.DB_TIME, dbTime);
	}

	public static long MDCgetGetEntityManagerNotCachedTime() {
		long getEntityManagerNotCachedTime = MDCgetStatsTime(LoggingStats.NOT_CACHED_TIME);
		return getEntityManagerNotCachedTime;
	}

	public static void MDCputGetEntityManagerNotCachedTime(final long getEntityManagerNotCachedTime) {
		MDCputStatsTime(LoggingStats.NOT_CACHED_TIME, getEntityManagerNotCachedTime);
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
package com.aliuken.jobvacanciesapp.util.springcore.aop.logging;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aliuken.jobvacanciesapp.aop.aspect.ControllerAspect;
import com.aliuken.jobvacanciesapp.enumtype.ControllerDependentTraceType;
import com.aliuken.jobvacanciesapp.enumtype.EndpointType;
import com.aliuken.jobvacanciesapp.enumtype.LoggingStats;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class that contains the common methods used for logging in controllers
 */
@Slf4j
public class ControllerAspectLoggingUtils {

	private static final String ZERO_TIME_STRING = Long.toString(0L);

	private ControllerAspectLoggingUtils() throws InstantiationException {
		throw new InstantiationException(StringUtils.getStringJoined("Cannot instantiate class ", ControllerAspectLoggingUtils.class.getName()));
	}

	public static String getTraceType(final ControllerDependentTraceType controllerDependentTraceType) {
		final boolean isInsideController = ControllerAspect.getIsInsideController();

		final String finalTraceType;
		if(isInsideController) {
			finalTraceType = controllerDependentTraceType.getTraceInsideController();
		} else {
			finalTraceType = controllerDependentTraceType.getTraceOutsideController();
		}
		return finalTraceType;
	}

	public static void initMDC(final String operation, final String mappingPath, final RequestMethod requestMethod, final HttpServletRequest request) {
		MDC.clear();
		MDC.put(LoggingStats.HTTP_METHOD.getKey(), requestMethod.toString());
		MDC.put(LoggingStats.MAPPING_PATH.getKey(), mappingPath);

		final String requestURI = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI();
		MDC.put(LoggingStats.INFORMED_PATH.getKey(), requestURI);

		MDC.put(LoggingStats.OPERATION.getKey(), operation);
		MDC.put(LoggingStats.THREAD_ID.getKey(), Long.toString(Thread.currentThread().getId()));

		MDC.put(LoggingStats.DB_TIME.getKey(), ZERO_TIME_STRING);
		MDC.put(LoggingStats.NOT_CACHED_TIME.getKey(), ZERO_TIME_STRING);
		MDC.put(LoggingStats.OTHER_TIME.getKey(), ZERO_TIME_STRING);
		MDC.put(LoggingStats.TOTAL_TIME.getKey(), Long.toString(System.currentTimeMillis()));
	}

	public static String endMDC() {
		try {
			final Long initTime = Long.valueOf(MDC.get(LoggingStats.TOTAL_TIME.getKey()));
			final Long dbTime = Long.valueOf(MDC.get(LoggingStats.DB_TIME.getKey()));
			final Long totalTime = System.currentTimeMillis() - initTime;
			final Long otherTime = totalTime - dbTime;

			MDC.put(LoggingStats.OTHER_TIME.getKey(), Long.toString(otherTime));
			MDC.put(LoggingStats.TOTAL_TIME.getKey(), Long.toString(totalTime));
		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
				log.error(StringUtils.getStringJoined("Error in endMDC(). Exception: ", rootCauseMessage));
			}

			MDC.put(LoggingStats.OTHER_TIME.getKey(), ZERO_TIME_STRING);
			MDC.put(LoggingStats.TOTAL_TIME.getKey(), ZERO_TIME_STRING);
		}

		final String statsResult = getStatsResult();
		return statsResult;
	}

	private static String getStatsResult() {
		try {
			final String httpMethod = MDC.get(LoggingStats.HTTP_METHOD.getKey());
			final String mappingPath = MDC.get(LoggingStats.MAPPING_PATH.getKey());
			final String informedPath = MDC.get(LoggingStats.INFORMED_PATH.getKey());

			final EndpointType endpointType = EndpointType.getInstance(httpMethod, informedPath);
			final String endpointTypeString = (endpointType != null) ? endpointType.toString() : null;

			final String operation = MDC.get(LoggingStats.OPERATION.getKey());
			final String threadId = MDC.get(LoggingStats.THREAD_ID.getKey());

			final String dbTime = MDC.get(LoggingStats.DB_TIME.getKey());
			final String getEntityManagerNotCachedTime = MDC.get(LoggingStats.NOT_CACHED_TIME.getKey());
			final String otherTime = MDC.get(LoggingStats.OTHER_TIME.getKey());
			final String totalTime = MDC.get(LoggingStats.TOTAL_TIME.getKey());

			final String statsResult = StringUtils.getStringJoined(
				"\n- HTTP method: ", httpMethod, "\n- mapping path: ", mappingPath, "\n- informed path: ", informedPath, "\n- endpoint type: ", endpointTypeString, "\n- operation: ", operation, "\n- thread id: ", threadId, "\n- DB time: ", dbTime, " (getEntityManagerNotCached time: ", getEntityManagerNotCachedTime, ")\n- other time: ", otherTime, "\n- total time: ", totalTime);
			return statsResult;
		} catch(final Exception exception) {
			return null;
		}
	}
}
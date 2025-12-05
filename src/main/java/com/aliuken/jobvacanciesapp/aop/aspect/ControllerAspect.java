package com.aliuken.jobvacanciesapp.aop.aspect;

import com.aliuken.jobvacanciesapp.enumtype.ControllerDependentTraceType;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.spring.aop.logging.ControllerAspectLoggingUtils;
import com.aliuken.jobvacanciesapp.util.spring.aop.rest.ControllerAspectRestUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * Class that contains the Advises used around controllers
 */
@Aspect
@Slf4j
public class ControllerAspect {
	private static final String CONTROLLER_INPUT_TRACE = ">>>> ";
	private static final String CONTROLLER_OUTPUT_TRACE = "<<<< ";

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	private static final void requestMapping(){}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	private static final void getMapping(){}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	private static final void postMapping(){}

//	@Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
//	private static final void putMapping(){}

//	@Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
//	private static final void deleteMapping(){}

	private static boolean isInsideController = false;

	public static boolean getIsInsideController() {
		return ControllerAspect.isInsideController;
	}

	public static String getTraceType(final ControllerDependentTraceType controllerDependentTraceType) {
		final String finalTraceType;
		if(isInsideController) {
			finalTraceType = controllerDependentTraceType.getTraceInsideController();
		} else {
			finalTraceType = controllerDependentTraceType.getTraceOutsideController();
		}
		return finalTraceType;
	}

	@Before("execution(public * com.aliuken.jobvacanciesapp.controller.*.*(..)) && requestMapping()")
	public void adviseBeforeExecutionInRequestMappingControllers(final JoinPoint joinPoint) {
		final RequestMapping requestMapping = ControllerAspectRestUtils.getRequestMapping(joinPoint);
		this.adviseBeforeExecutionInControllersCommon(joinPoint, requestMapping.method()[0], requestMapping.value()[0]);
	}

	@Before("execution(public * com.aliuken.jobvacanciesapp.controller.*.*(..)) && getMapping()")
	public void adviseBeforeExecutionInGetMappingControllers(final JoinPoint joinPoint) {
		final GetMapping getMapping = ControllerAspectRestUtils.getGetMapping(joinPoint);
		this.adviseBeforeExecutionInControllersCommon(joinPoint, RequestMethod.GET, getMapping.value()[0]);
	}

	@Before("execution(public * com.aliuken.jobvacanciesapp.controller.*.*(..)) && postMapping()")
	public void adviseBeforeExecutionInPostMappingControllers(final JoinPoint joinPoint) {
		final PostMapping postMapping = ControllerAspectRestUtils.getPostMapping(joinPoint);
		this.adviseBeforeExecutionInControllersCommon(joinPoint, RequestMethod.POST, postMapping.value()[0]);
	}

	/**
	 * Advise that is executed before executing the REST controllers
	 */
	private void adviseBeforeExecutionInControllersCommon(final JoinPoint joinPoint, final RequestMethod requestMethod, final String mappingPath) {
		final String methodName = joinPoint.getSignature().getName();
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		ControllerAspectLoggingUtils.initMDC(methodName, mappingPath, requestMethod, request);

		if(log.isInfoEnabled()) {
			final String methodSignature = joinPoint.getSignature().toShortString();
			log.info(StringUtils.getStringJoined(CONTROLLER_INPUT_TRACE, "[", methodSignature, "] mapped in [", mappingPath, "]"));
		}

		if(log.isTraceEnabled()) {
			final Object[] args = joinPoint.getArgs();

			int index = 0;
			for(final Object arg : args) {
				final String indexString = String.valueOf(index);
				if(arg == null) {
					log.trace(StringUtils.getStringJoined("Input parameter [", indexString, "]: ", null));
				} else if(arg instanceof String) {
					log.trace(StringUtils.getStringJoined("Input parameter [", indexString, "]: ", (String) arg));
				} else if(arg instanceof HttpServletRequest) {
					final String redirectUri = ControllerAspect.extractPathFromPattern((HttpServletRequest) arg);
					log.trace(StringUtils.getStringJoined("Input parameter [", indexString, "]: ", redirectUri));
				} else if(arg instanceof Map) {
					final Set<?> mapEntrySet = ((Map<?,?>) arg).entrySet();
					log.trace(StringUtils.getStringJoined("Input parameter [", indexString, "]: ", mapEntrySet.toString()));
				} else {
					log.trace(StringUtils.getStringJoined("Input parameter [", indexString, "]: ", arg.toString()));
				}
				index++;
			}
		}

		ControllerAspect.isInsideController = true;
	}

	@AfterReturning(pointcut = "execution(public * com.aliuken.jobvacanciesapp.controller.*.*(..)) && requestMapping()", returning = "returnValue")
	public void adviseAfterExecutionInRequestMappingControllers(final JoinPoint joinPoint, final String returnValue) {
		final RequestMapping requestMapping = ControllerAspectRestUtils.getRequestMapping(joinPoint);
		this.adviseAfterExecutionInControllersCommon(joinPoint, requestMapping.method()[0], requestMapping.value()[0], returnValue);
	}

	@AfterReturning(pointcut = "execution(public * com.aliuken.jobvacanciesapp.controller.*.*(..)) && getMapping()", returning = "returnValue")
	public void adviseAfterExecutionInGetMappingControllers(final JoinPoint joinPoint, final String returnValue) {
		final GetMapping getMapping = ControllerAspectRestUtils.getGetMapping(joinPoint);
		this.adviseAfterExecutionInControllersCommon(joinPoint, RequestMethod.GET, getMapping.value()[0], returnValue);
	}

	@AfterReturning(pointcut = "execution(public * com.aliuken.jobvacanciesapp.controller.*.*(..)) && postMapping()", returning = "returnValue")
	public void adviseAfterExecutionInPostMappingControllers(final JoinPoint joinPoint, final String returnValue) {
		final PostMapping postMapping = ControllerAspectRestUtils.getPostMapping(joinPoint);
		this.adviseAfterExecutionInControllersCommon(joinPoint, RequestMethod.POST, postMapping.value()[0], returnValue);
	}

	/**
	 * Advise that is executed after executing the REST controllers when the result is obtained correctly
	 */
	private void adviseAfterExecutionInControllersCommon(final JoinPoint joinPoint, final RequestMethod requestMethod, final String mappingPath, final String returnValue) {
		ControllerAspect.isInsideController = false;

		final String statsResult = ControllerAspectLoggingUtils.endMDC();

		if(log.isInfoEnabled()) {
			final String methodName = joinPoint.getSignature().getName();
			log.info(StringUtils.getStringJoined(CONTROLLER_OUTPUT_TRACE, "[", methodName, "] mapped in [", mappingPath, "] returned: ", returnValue));
			log.info(StringUtils.getStringJoined("Stats result:", statsResult));
		}
	}

	@AfterThrowing(pointcut = "execution(public * com.aliuken.jobvacanciesapp.controller.*.*(..)) && requestMapping()", throwing = "exception")
	public void adviseAfterExceptionIsThrownInRequestMappingControllers(final JoinPoint joinPoint, final Exception exception) {
		final RequestMapping requestMapping = ControllerAspectRestUtils.getRequestMapping(joinPoint);
		this.adviseAfterExceptionIsThrownInControllersCommon(joinPoint, requestMapping.method()[0], requestMapping.value()[0], exception);
	}

	@AfterThrowing(pointcut = "execution(public * com.aliuken.jobvacanciesapp.controller.*.*(..)) && getMapping()", throwing = "exception")
	public void adviseAfterExceptionIsThrownInGetMappingControllers(final JoinPoint joinPoint, final Exception exception) {
		final GetMapping getMapping = ControllerAspectRestUtils.getGetMapping(joinPoint);
		this.adviseAfterExceptionIsThrownInControllersCommon(joinPoint, RequestMethod.GET, getMapping.value()[0], exception);
	}

	@AfterThrowing(pointcut = "execution(public * com.aliuken.jobvacanciesapp.controller.*.*(..)) && postMapping()", throwing = "exception")
	public void adviseAfterExceptionIsThrownInPostMappingControllers(final JoinPoint joinPoint, final Exception exception) {
		final PostMapping postMapping = ControllerAspectRestUtils.getPostMapping(joinPoint);
		this.adviseAfterExceptionIsThrownInControllersCommon(joinPoint, RequestMethod.POST, postMapping.value()[0], exception);
	}

	/**
	 * Advise that is executed after executing the REST controllers when an exception is thrown
	 */
	private void adviseAfterExceptionIsThrownInControllersCommon(final JoinPoint joinPoint, final RequestMethod requestMethod, final String mappingPath, final Exception exception) {
		ControllerAspect.isInsideController = false;

		final String statsResult = ControllerAspectLoggingUtils.endMDC();

		if(log.isInfoEnabled()) {
			final String methodName = joinPoint.getSignature().getName();
			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			log.info(StringUtils.getStringJoined(CONTROLLER_OUTPUT_TRACE, "[", methodName, "] mapped in [", mappingPath, "]. Finished in Exception: ", exception.getClass().toString(), ", message: ", rootCauseMessage));
			log.info(StringUtils.getStringJoined("Stats result:", statsResult));
		}
	}

	private static String extractPathFromPattern(final HttpServletRequest request) {
		final String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		final String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

		final PathMatcher pathMatcher = new AntPathMatcher();
		String finalPath = pathMatcher.extractPathWithinPattern(bestMatchPattern, path);

		//The previous method deletes one of the two "/" in "http://" so it is added manually in the next code
		if(LogicalUtils.contains(finalPath, ":/")) {
			final String[] splitResult = finalPath.split(":/");
			finalPath = StringUtils.getStringJoined(splitResult[0], "://", splitResult[1]);
		}

		return finalPath;
	}
}
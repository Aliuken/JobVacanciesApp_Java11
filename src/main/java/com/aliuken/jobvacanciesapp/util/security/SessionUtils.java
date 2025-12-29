package com.aliuken.jobvacanciesapp.util.security;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.spring.di.BeanFactoryUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

	private SessionUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	//----------------------------------------------------------------------------------------------------------------------------

	// Methods to get the sessionAuthUser object from the Authentication object of Spring Security

	public static AuthUser getSessionAuthUserFromSecurityContextHolder() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromSecurityContext(securityContext);
		return sessionAuthUser;
	}

	public static String getSessionAuthUserIdStringFromSecurityContext(final SecurityContext securityContext) {
		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromSecurityContext(securityContext);
		final Long sessionAuthUserId = sessionAuthUser.getId();

		final String sessionAuthUserIdString = String.valueOf(sessionAuthUserId);
		return sessionAuthUserIdString;
	}

	public static AuthUser getSessionAuthUserFromSecurityContext(final SecurityContext securityContext) {
		final Authentication authentication = securityContext.getAuthentication();

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromAuthentication(authentication);
		return sessionAuthUser;
	}

	public static AuthUser getSessionAuthUserFromAuthentication(final Authentication authentication) {
		final AuthUserService authUserService = BeanFactoryUtils.getBean(AuthUserService.class);
		final String sessionAuthUserEmail = authentication.getName();

		AuthUser sessionAuthUser = authUserService.findByEmail(sessionAuthUserEmail);
		sessionAuthUser = authUserService.refreshEntity(sessionAuthUser);
		return sessionAuthUser;
	}

	//----------------------------------------------------------------------------------------------------------------------------

	// Methods to get the sessionAuthUser object from the "sessionAuthUser" attribute of the HttpSession object of the Servlet API

	public static AuthUser getSessionAuthUserFromRequestContextHolder() {
		final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		final HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest);
		return sessionAuthUser;
	}

	public static String getSessionAuthUserIdStringFromHttpServletRequest(final HttpServletRequest httpServletRequest) {
		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest);
		final Long sessionAuthUserId = sessionAuthUser.getId();

		final String sessionAuthUserIdString = String.valueOf(sessionAuthUserId);
		return sessionAuthUserIdString;
	}

	public static AuthUser getSessionAuthUserFromHttpServletRequest(final HttpServletRequest httpServletRequest) {
		final HttpSession httpSession = httpServletRequest.getSession();

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpSession(httpSession);
		return sessionAuthUser;
	}

	public static AuthUser getSessionAuthUserFromHttpSession(final HttpSession httpSession) {
		final AuthUserService authUserService = BeanFactoryUtils.getBean(AuthUserService.class);

		AuthUser sessionAuthUser = (AuthUser) httpSession.getAttribute(Constants.SESSION_AUTH_USER);
		sessionAuthUser = authUserService.refreshEntity(sessionAuthUser);
		return sessionAuthUser;
	}
}
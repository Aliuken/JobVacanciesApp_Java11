package com.aliuken.jobvacanciesapp.util.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

public class SessionUtils {

	private SessionUtils() throws InstantiationException {
		throw new InstantiationException(StringUtils.getStringJoined("Cannot instantiate class ", SessionUtils.class.getName()));
	}

	//----------------------------------------------------------------------------------------------------------------------------

	// Methods to get the sessionAuthUser object from the Authentication object of Spring Security

	public static AuthUser getSessionAuthUserFromSecurityContextHolder(final AuthUserService authUserService) {
		final SecurityContext securityContext = SecurityContextHolder.getContext();

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromSecurityContext(securityContext, authUserService);
		return sessionAuthUser;
	}

	public static AuthUser getSessionAuthUserFromSecurityContext(final SecurityContext securityContext, final AuthUserService authUserService) {
		final Authentication authentication = securityContext.getAuthentication();

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromAuthentication(authentication, authUserService);
		return sessionAuthUser;
	}

	public static AuthUser getSessionAuthUserFromAuthentication(final Authentication authentication, final AuthUserService authUserService) {
		final String sessionAuthUserEmail = authentication.getName();

		AuthUser sessionAuthUser = authUserService.findByEmail(sessionAuthUserEmail);
		sessionAuthUser = authUserService.refreshEntity(sessionAuthUser);
		return sessionAuthUser;
	}

	//----------------------------------------------------------------------------------------------------------------------------

	// Methods to get the sessionAuthUser object from the "sessionAuthUser" attribute of the HttpSession object of the Servlet API

	public static AuthUser getSessionAuthUserFromRequestContextHolder(final AuthUserService authUserService) {
		final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		final HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest, authUserService);
		return sessionAuthUser;
	}

	public static AuthUser getSessionAuthUserFromHttpServletRequest(final HttpServletRequest httpServletRequest, final AuthUserService authUserService) {
		final HttpSession httpSession = httpServletRequest.getSession();

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpSession(httpSession, authUserService);
		return sessionAuthUser;
	}

	public static AuthUser getSessionAuthUserFromHttpSession(final HttpSession httpSession, final AuthUserService authUserService) {
		AuthUser sessionAuthUser = (AuthUser) httpSession.getAttribute(Constants.SESSION_AUTH_USER);
		if(authUserService != null) {
			sessionAuthUser = authUserService.refreshEntity(sessionAuthUser);
		}
		return sessionAuthUser;
	}

}
package com.aliuken.jobvacanciesapp.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.service.AuthUserService;

@Component
public class CustomAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler, LogoutSuccessHandler {

	private static final int LOGOUT_STATUS = HttpStatus.OK.value();
	private static final String LOGOUT_REDIRECT = "/index";

    @Autowired
    private AuthUserService authUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
    	final String email = authentication.getName();
		final AuthUser sessionAuthUser = authUserService.findByEmail(email);

        httpServletRequest.getSession().setAttribute(Constants.SESSION_AUTH_USER, sessionAuthUser);

        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
    	httpServletRequest.getSession().removeAttribute(Constants.SESSION_AUTH_USER);

		final String redirect = httpServletRequest.getContextPath() + LOGOUT_REDIRECT;

		httpServletResponse.setStatus(LOGOUT_STATUS);
		httpServletResponse.sendRedirect(redirect);
    }

}
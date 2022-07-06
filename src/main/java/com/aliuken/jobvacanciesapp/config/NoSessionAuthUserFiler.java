package com.aliuken.jobvacanciesapp.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.service.AuthUserService;

@Component
public class NoSessionAuthUserFiler extends OncePerRequestFilter {

	@Autowired
    private AuthUserService authUserService;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		final AuthUser anonymousAuthUser = authUserService.findById(Constants.ANONYMOUS_AUTH_USER_ID);
        httpServletRequest.getSession().setAttribute(Constants.ANONYMOUS_AUTH_USER, anonymousAuthUser);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
	}
}

package com.aliuken.jobvacanciesapp.util.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

public class ServletUtils {
	private ServletUtils() throws InstantiationException {
		throw new InstantiationException(StringUtils.getStringJoined("Cannot instantiate class ", ServletUtils.class.getName()));
	}

	public static String getUrlFromHttpServletRequest(final HttpServletRequest httpServletRequest) {
		final ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(httpServletRequest);
		final UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(servletServerHttpRequest).build();
		final String url = uriComponents.toUriString();
		return url;
	}
}
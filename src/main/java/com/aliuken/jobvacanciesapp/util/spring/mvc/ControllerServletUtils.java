package com.aliuken.jobvacanciesapp.util.spring.mvc;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
public class ControllerServletUtils {

	private ControllerServletUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	public static @NonNull String getUrlFromHttpServletRequest(final HttpServletRequest httpServletRequest) {
		final ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(httpServletRequest);
		final UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(servletServerHttpRequest).build();
		final String url = uriComponents.toUriString();
		return url;
	}

	public static Map<String, ?> getInputFlashMap(final @NonNull HttpServletRequest httpServletRequest) {
		final Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(httpServletRequest);

		if(log.isInfoEnabled()) {
			final String url = getUrlFromHttpServletRequest(httpServletRequest);
			final String inputFlashMapString = StringUtils.getMapString(inputFlashMap);
			log.info(StringUtils.getStringJoined("url:", url, "\ninputFlashMap:", inputFlashMapString));
		}

		return inputFlashMap;
	}
}
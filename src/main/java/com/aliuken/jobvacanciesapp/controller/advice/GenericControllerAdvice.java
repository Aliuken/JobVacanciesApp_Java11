package com.aliuken.jobvacanciesapp.controller.advice;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerNavigationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GenericControllerAdvice {

	//To use "${requestURI}" instead of "${#httpServletRequest.requestURI}" in Thymeleaf pages
	@ModelAttribute("requestURI")
	private String getRequestURI(HttpServletRequest httpServletRequest) {
		final String requestURI = httpServletRequest.getRequestURI();
		return requestURI;
	}

	//To handle the exception when uploading files too big
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception, RedirectAttributes redirectAttributes) {
		if(log.isErrorEnabled()) {
			final String stackTrace = ThrowableUtils.getStackTrace(exception);
			log.error(StringUtils.getStringJoined("An exception happened when trying to upload a file. The maximum file size was exceeded. Exception: ", stackTrace));
		}
		redirectAttributes.addFlashAttribute("errorMsg", "The file size is bigger than 10 MB");
		return ControllerNavigationUtils.getNextRedirect("/", "en");
	}
}
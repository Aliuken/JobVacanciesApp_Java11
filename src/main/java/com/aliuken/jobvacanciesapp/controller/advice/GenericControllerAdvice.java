package com.aliuken.jobvacanciesapp.controller.advice;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerNavigationUtils;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@ControllerAdvice
@Slf4j
public class GenericControllerAdvice {
	@Autowired
	private AuthUserService authUserService;

	//To get the object with the data used in the Thymeleaf page template.html like "${templateDataDTO...}"
	@ModelAttribute("templateDataDTO")
	public TemplateDataDTO getTemplateDataDTO(final @NonNull HttpSession httpSession) {
		final AuthUser refreshedSessionAuthUser = this.getRefreshedSessionAuthUser(httpSession);

		final String colorModeValue;
		final String languageCode;
		if (refreshedSessionAuthUser != null) {
			final ColorMode sessionColorMode = refreshedSessionAuthUser.getColorMode();
			if (Constants.ENUM_UTILS.hasASpecificValue(sessionColorMode)) {
				colorModeValue = sessionColorMode.getValue();
			} else {
				colorModeValue = ConfigPropertiesBean.CURRENT_DEFAULT_COLOR_MODE.getValue();
			}

			final Language sessionLanguage = refreshedSessionAuthUser.getLanguage();
			if (Constants.ENUM_UTILS.hasASpecificValue(sessionLanguage)) {
				languageCode = sessionLanguage.getCode();
			} else {
				languageCode = ConfigPropertiesBean.CURRENT_DEFAULT_LANGUAGE.getCode();
			}
		} else {
			colorModeValue = ConfigPropertiesBean.CURRENT_DEFAULT_COLOR_MODE.getValue();
			languageCode = ConfigPropertiesBean.CURRENT_DEFAULT_LANGUAGE.getCode();
		}

		final String userInterfaceFrameworkCode = ConfigPropertiesBean.CURRENT_DEFAULT_USER_INTERFACE_FRAMEWORK.getCode();

		final TemplateDataDTO templateDataDTO = new TemplateDataDTO(colorModeValue, languageCode, userInterfaceFrameworkCode);
		if(log.isInfoEnabled()) {
			final String templateDataDtoString = String.valueOf(templateDataDTO);
			log.info(StringUtils.getStringJoined("getTemplateDataDTO result:", templateDataDtoString));
		}
		return templateDataDTO;
	}

	private AuthUser getRefreshedSessionAuthUser(final @NonNull HttpSession httpSession) {
		final Long sessionAuthUserId = (Long) httpSession.getAttribute(Constants.SESSION_AUTH_USER_ID);
		AuthUser sessionAuthUser = authUserService.findByIdNotOptional(sessionAuthUserId);
		sessionAuthUser = authUserService.refreshEntity(sessionAuthUser);
		return sessionAuthUser;
	}

	//To handle the exception when uploading files too big
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxUploadSizeExceededException(final @NonNull MaxUploadSizeExceededException exception, final @NonNull RedirectAttributes redirectAttributes) {
		if(log.isErrorEnabled()) {
			final String stackTrace = ThrowableUtils.getStackTrace(exception);
			log.error(StringUtils.getStringJoined("An exception happened when trying to upload a file. The maximum file size was exceeded. Exception: ", stackTrace));
		}
		redirectAttributes.addFlashAttribute("errorMsg", "The file size is bigger than 10 MB");
		return ControllerNavigationUtils.getNextRedirect("/", "en");
	}
}
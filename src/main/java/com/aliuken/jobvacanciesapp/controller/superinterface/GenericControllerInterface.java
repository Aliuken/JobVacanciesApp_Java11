package com.aliuken.jobvacanciesapp.controller.superinterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

import com.aliuken.jobvacanciesapp.model.AuthUserLanguage;
import com.aliuken.jobvacanciesapp.util.StringUtils;

public interface GenericControllerInterface {
	public static final Logger log = LoggerFactory.getLogger(GenericControllerInterface.class);

	abstract MessageSource getMessageSource();

	default String getInternationalizedMessage(String language, String messageName, Object[] messageParameters) {
		final AuthUserLanguage authUserLanguage = AuthUserLanguage.findByCode(language);
		final String internationalizedMessage = this.getInternationalizedMessage(authUserLanguage, messageName, messageParameters);

		return internationalizedMessage;
	}

	default String getInternationalizedMessage(AuthUserLanguage authUserLanguage, String messageName, Object[] messageParameters) {
		MessageSource messageSource = this.getMessageSource();
		String message = messageSource.getMessage(messageName, messageParameters, authUserLanguage.getLocale());

		return message;
	}

	default String getNextView(final Model model, final String operation, final String language, final String nextView) {
		model.addAttribute("operation", operation);
		if (language != null) {
			model.addAttribute("lang", language);
		}

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("Going to the view '", nextView, "' with the following attributes: ", model.asMap().entrySet().toString()));
		}
		return nextView;
	}

	default String getNextRedirect(final String language, final String nextRedirectPath) {
		String nextRedirect;
		if (language != null) {
			nextRedirect = StringUtils.getStringJoined("redirect:", nextRedirectPath, "?lang=", language);
		} else {
			nextRedirect = StringUtils.getStringJoined("redirect:", nextRedirectPath);
		}

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("Redirecting to the path '", nextRedirect));
		}
		return nextRedirect;
	}

}

package com.aliuken.jobvacanciesapp.util.spring.mvc;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

@Slf4j
public class ControllerValidationUtils {

	private ControllerValidationUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	public static String getFirstBindingErrorString(final BindingResult bindingResult) {
		if(bindingResult == null) {
			return null;
		}

		final boolean hasBindingErrors = bindingResult.hasErrors();

		final String firstBindingErrorString;
		if(hasBindingErrors) {
			final List<ObjectError> allBindingErrors = bindingResult.getAllErrors();
			final String allBindingErrorsString = allBindingErrors.toString();

			if(log.isErrorEnabled()) {
				log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", allBindingErrorsString));
			}

			final ObjectError firstBindingError = allBindingErrors.get(0);
			firstBindingErrorString = firstBindingError.toString();
		} else {
			firstBindingErrorString = null;
		}
		return firstBindingErrorString;
	}
}

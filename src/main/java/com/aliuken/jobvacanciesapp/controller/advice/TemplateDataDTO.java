package com.aliuken.jobvacanciesapp.controller.advice;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class TemplateDataDTO implements Serializable {
	private static final long serialVersionUID = -3943025053487973796L;

	@NotEmpty(message="{colorModeValue.notEmpty}")
	private final String colorModeValue;

	@NotEmpty(message="{languageCode.notEmpty}")
	private final String languageCode;

	@NotEmpty(message="{userInterfaceFrameworkCode.notEmpty}")
	private final String userInterfaceFrameworkCode;

	@Override
	public @NonNull String toString() {
		final String result = StringUtils.getStringJoined(
			"TemplateDataDTO [colorModeValue=", colorModeValue,
			", languageCode=", languageCode,
			", userInterfaceFrameworkCode=", userInterfaceFrameworkCode, "]");
		return result;
	}
}

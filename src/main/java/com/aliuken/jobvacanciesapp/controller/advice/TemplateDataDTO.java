package com.aliuken.jobvacanciesapp.controller.advice;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class TemplateDataDTO implements Serializable {
	private static final long serialVersionUID = -3943025053487973796L;

	private static final TemplateDataDTO NO_ARGS_INSTANCE = new TemplateDataDTO(null, null, null);

	@NotEmpty(message="{colorModeValue.notEmpty}")
	private final String colorModeValue;

	@NotEmpty(message="{languageCode.notEmpty}")
	private final String languageCode;

	@NotEmpty(message="{userInterfaceFrameworkCode.notEmpty}")
	private final String userInterfaceFrameworkCode;

	public static TemplateDataDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public String toString() {
		final String result = StringUtils.getStringJoined(
			"TemplateDataDTO [colorModeValue=", colorModeValue,
			", languageCode=", languageCode,
			", userInterfaceFrameworkCode=", userInterfaceFrameworkCode, "]");
		return result;
	}
}

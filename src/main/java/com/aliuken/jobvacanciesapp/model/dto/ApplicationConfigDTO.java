package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

import lombok.Data;

@Data
public class ApplicationConfigDTO implements Serializable {
	private static final long serialVersionUID = -3843025053487973796L;

	private static final ApplicationConfigDTO NO_ARGS_INSTANCE = new ApplicationConfigDTO(null, null, null, null, null);

	@NotEmpty(message="{nextDefaultLanguageCode.notEmpty}")
	private final String nextDefaultLanguageCode;

	@NotEmpty(message="{nextAnonymousAccessPermissionName.notEmpty}")
	private final String nextAnonymousAccessPermissionName;

	@NotEmpty(message="{nextDefaultInitialTablePageSizeValue.notEmpty}")
	private final String nextDefaultInitialTablePageSizeValue;

	@NotEmpty(message="{nextDefaultColorModeCode.notEmpty}")
	private final String nextDefaultColorModeCode;

	@NotEmpty(message="{nextUserInterfaceFrameworkCode.notEmpty}")
	private final String nextUserInterfaceFrameworkCode;

	public static ApplicationConfigDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public String toString() {
		final String result = StringUtils.getStringJoined("ApplicationConfigDTO [nextDefaultLanguageCode=", nextDefaultLanguageCode, ", nextAnonymousAccessPermissionName=", nextAnonymousAccessPermissionName, ", nextDefaultInitialTablePageSizeValue=", nextDefaultInitialTablePageSizeValue, ", nextDefaultColorModeCode=", nextDefaultColorModeCode, ", nextUserInterfaceFrameworkCode=", nextUserInterfaceFrameworkCode, "]");
		return result;
	}
}

package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class ApplicationNextConfigDTO implements Serializable {
	private static final long serialVersionUID = -3843025053487973796L;

	private static final ApplicationNextConfigDTO NO_ARGS_INSTANCE = new ApplicationNextConfigDTO(null, null, null, null, null, null, null);

	@NotEmpty(message="{nextDefaultLanguageCode.notEmpty}")
	private final String nextDefaultLanguageCode;

	@NotEmpty(message="{nextDefaultAnonymousAccessPermissionValue.notEmpty}")
	private final String nextDefaultAnonymousAccessPermissionValue;

	@NotEmpty(message="{nextDefaultInitialTableSortingDirectionCode.notEmpty}")
	private final String nextDefaultInitialTableSortingDirectionCode;

	@NotEmpty(message="{nextDefaultInitialTablePageSizeValue.notEmpty}")
	private final String nextDefaultInitialTablePageSizeValue;

	@NotEmpty(message="{nextDefaultColorModeCode.notEmpty}")
	private final String nextDefaultColorModeCode;

	@NotEmpty(message="{nextDefaultUserInterfaceFrameworkCode.notEmpty}")
	private final String nextDefaultUserInterfaceFrameworkCode;

	@NotEmpty(message="{nextDefaultPdfDocumentPageFormatCode.notEmpty}")
	private final String nextDefaultPdfDocumentPageFormatCode;

	public static ApplicationNextConfigDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public String toString() {
		final String result = StringUtils.getStringJoined(
			"ApplicationNextConfigDTO [nextDefaultLanguageCode=", nextDefaultLanguageCode,
			", nextDefaultAnonymousAccessPermissionValue=", nextDefaultAnonymousAccessPermissionValue,
			", nextDefaultInitialTableSortingDirectionCode=", nextDefaultInitialTableSortingDirectionCode,
			", nextDefaultInitialTablePageSizeValue=", nextDefaultInitialTablePageSizeValue,
			", nextDefaultColorModeCode=", nextDefaultColorModeCode,
			", nextDefaultUserInterfaceFrameworkCode=", nextDefaultUserInterfaceFrameworkCode,
			", nextDefaultPdfDocumentPageFormatCode=", nextDefaultPdfDocumentPageFormatCode, "]");
		return result;
	}
}

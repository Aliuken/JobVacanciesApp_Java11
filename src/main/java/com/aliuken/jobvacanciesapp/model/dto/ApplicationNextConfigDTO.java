package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class ApplicationNextConfigDTO implements Serializable {
	private static final long serialVersionUID = -3843025053487973796L;

	@NotEmpty(message="{nextDefaultLanguageCode.notEmpty}")
	private final @NonNull String nextDefaultLanguageCode;

	@NotEmpty(message="{nextDefaultAnonymousAccessPermissionValue.notEmpty}")
	private final @NonNull String nextDefaultAnonymousAccessPermissionValue;

	@NotEmpty(message="{nextDefaultInitialTableSortingDirectionCode.notEmpty}")
	private final @NonNull String nextDefaultInitialTableSortingDirectionCode;

	@NotEmpty(message="{nextDefaultInitialTablePageSizeValue.notEmpty}")
	private final @NonNull String nextDefaultInitialTablePageSizeValue;

	@NotEmpty(message="{nextDefaultColorModeCode.notEmpty}")
	private final @NonNull String nextDefaultColorModeCode;

	@NotEmpty(message="{nextDefaultUserInterfaceFrameworkCode.notEmpty}")
	private final @NonNull String nextDefaultUserInterfaceFrameworkCode;

	@NotEmpty(message="{nextDefaultPdfDocumentPageFormatCode.notEmpty}")
	private final @NonNull String nextDefaultPdfDocumentPageFormatCode;

	@Override
	public @NonNull String toString() {
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

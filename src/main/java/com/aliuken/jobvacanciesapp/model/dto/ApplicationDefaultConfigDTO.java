package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.enumtype.AnonymousAccessPermission;
import com.aliuken.jobvacanciesapp.enumtype.UserInterfaceFramework;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class ApplicationDefaultConfigDTO implements Serializable {
	private static final long serialVersionUID = 7326190421458078482L;

	private static final ApplicationDefaultConfigDTO NO_ARGS_INSTANCE = new ApplicationDefaultConfigDTO(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	//Non-overwritable properties
	private final String authUserCurriculumFilesPath;
	private final String authUserEntityQueryFilesPath;
	private final String jobCompanyLogosPath;
	private final Boolean useAjaxToRefreshJobCompanyLogos;
	private final Boolean useEntityManagerCache;
	private final Boolean useParallelStreams;
	private final Long signupConfirmationLinkExpirationHours;
	private final Long resetPasswordLinkExpirationHours;

	//Overwritable properties
	private final Language defaultLanguage;
	private final AnonymousAccessPermission defaultAnonymousAccessPermission;
	private final TableSortingDirection defaultInitialTableSortingDirection;
	private final TablePageSize defaultInitialTablePageSize;
	private final ColorMode defaultColorMode;
	private final UserInterfaceFramework defaultUserInterfaceFramework;
	private final PdfDocumentPageFormat defaultPdfDocumentPageFormat;

	public static ApplicationDefaultConfigDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public String toString() {
		final String useAjaxToRefreshJobCompanyLogosString = Objects.toString(useAjaxToRefreshJobCompanyLogos);
		final String useEntityManagerCacheString = Objects.toString(useEntityManagerCache);
		final String useParallelStreamsString = Objects.toString(useParallelStreams);
		final String signupConfirmationLinkExpirationHoursString = Objects.toString(signupConfirmationLinkExpirationHours);
		final String resetPasswordLinkExpirationHoursString = Objects.toString(resetPasswordLinkExpirationHours);
		final String defaultLanguageName = Objects.toString(defaultLanguage);
		final String defaultAnonymousAccessPermissionName = Objects.toString(defaultAnonymousAccessPermission);
		final String defaultInitialTableSortingDirectionName = Objects.toString(defaultInitialTableSortingDirection);
		final String defaultInitialTablePageSizeName = Objects.toString(defaultInitialTablePageSize);
		final String defaultColorModeName = Objects.toString(defaultColorMode);
		final String defaultUserInterfaceFrameworkName = Objects.toString(defaultUserInterfaceFramework);
		final String defaultPdfDocumentPageFormatName = Objects.toString(defaultPdfDocumentPageFormat);

		final String result = StringUtils.getStringJoined(
			"ApplicationDefaultConfigDTO [authUserCurriculumFilesPath=", authUserCurriculumFilesPath,
			", authUserEntityQueryFilesPath=", authUserEntityQueryFilesPath,
			", jobCompanyLogosPath=", jobCompanyLogosPath,
			", useAjaxToRefreshJobCompanyLogos=", useAjaxToRefreshJobCompanyLogosString,
			", useEntityManagerCache=", useEntityManagerCacheString,
			", useParallelStreams=", useParallelStreamsString,
			", signupConfirmationLinkExpirationHours=", signupConfirmationLinkExpirationHoursString,
			", resetPasswordLinkExpirationHours=", resetPasswordLinkExpirationHoursString,
			", defaultLanguage=", defaultLanguageName,
			", defaultAnonymousAccessPermission=", defaultAnonymousAccessPermissionName,
			", defaultInitialTableSortingDirection=", defaultInitialTableSortingDirectionName,
			", defaultInitialTablePageSize=", defaultInitialTablePageSizeName,
			", defaultColorMode=", defaultColorModeName,
			", defaultUserInterfaceFramework=", defaultUserInterfaceFrameworkName,
			", defaultPdfDocumentPageFormat=", defaultPdfDocumentPageFormatName, "]");
		return result;
	}
}

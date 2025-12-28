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
		final String useAjaxToRefreshJobCompanyLogosString = String.valueOf(useAjaxToRefreshJobCompanyLogos);
		final String useEntityManagerCacheString = String.valueOf(useEntityManagerCache);
		final String useParallelStreamsString = String.valueOf(useParallelStreams);
		final String signupConfirmationLinkExpirationHoursString = String.valueOf(signupConfirmationLinkExpirationHours);
		final String resetPasswordLinkExpirationHoursString = String.valueOf(resetPasswordLinkExpirationHours);
		final String defaultLanguageName = String.valueOf(defaultLanguage);
		final String defaultAnonymousAccessPermissionName = String.valueOf(defaultAnonymousAccessPermission);
		final String defaultInitialTableSortingDirectionName = String.valueOf(defaultInitialTableSortingDirection);
		final String defaultInitialTablePageSizeName = String.valueOf(defaultInitialTablePageSize);
		final String defaultColorModeName = String.valueOf(defaultColorMode);
		final String defaultUserInterfaceFrameworkName = String.valueOf(defaultUserInterfaceFramework);
		final String defaultPdfDocumentPageFormatName = String.valueOf(defaultPdfDocumentPageFormat);

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

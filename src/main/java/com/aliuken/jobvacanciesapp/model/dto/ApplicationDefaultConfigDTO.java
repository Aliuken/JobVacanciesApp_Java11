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
import org.jspecify.annotations.NonNull;

import java.io.Serializable;

@Data
public class ApplicationDefaultConfigDTO implements Serializable {
	private static final long serialVersionUID = 7326190421458078482L;

	//Non-overwritable properties
	private final @NonNull String authUserCurriculumFilesPath;
	private final @NonNull String authUserEntityQueryFilesPath;
	private final @NonNull String jobCompanyLogosPath;
	private final @NonNull Boolean useAjaxToRefreshJobCompanyLogos;
	private final @NonNull Boolean useEntityManagerCache;
	private final @NonNull Boolean useParallelStreams;
	private final @NonNull Long signupConfirmationLinkExpirationHours;
	private final @NonNull Long resetPasswordLinkExpirationHours;

	//Overwritable properties
	private final @NonNull Language defaultLanguage;
	private final @NonNull AnonymousAccessPermission defaultAnonymousAccessPermission;
	private final @NonNull TableSortingDirection defaultInitialTableSortingDirection;
	private final @NonNull TablePageSize defaultInitialTablePageSize;
	private final @NonNull ColorMode defaultColorMode;
	private final @NonNull UserInterfaceFramework defaultUserInterfaceFramework;
	private final @NonNull PdfDocumentPageFormat defaultPdfDocumentPageFormat;

	@Override
	public @NonNull String toString() {
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

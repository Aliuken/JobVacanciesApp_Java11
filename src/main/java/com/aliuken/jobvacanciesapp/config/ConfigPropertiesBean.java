package com.aliuken.jobvacanciesapp.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.enumtype.AnonymousAccessPermission;
import com.aliuken.jobvacanciesapp.enumtype.UserInterfaceFramework;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.security.SessionUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Getter
@Slf4j
public class ConfigPropertiesBean {
	private static final String BY_DEFAULT_PAGE_SIZE_VALUE_STRING = String.valueOf(TablePageSize.BY_DEFAULT.getValue());

	//Non-overwritable properties
	private final String authUserCurriculumFilesPath;
	private final String authUserEntityQueryFilesPath;
	private final String jobCompanyLogosPath;
	private final boolean useAjaxToRefreshJobCompanyLogos;
	private final boolean useEntityManagerCache;
	private final boolean useParallelStreams;

	//Overwritable properties
	private final AnonymousAccessPermission defaultAnonymousAccessPermission;
	private final ColorMode defaultColorMode;
	private final Language defaultLanguage;
	private final PdfDocumentPageFormat defaultPdfDocumentPageFormat;
	private final TablePageSize defaultInitialTablePageSize;
	private final UserInterfaceFramework defaultUserInterfaceFramework;

	//Overwriting properties
	private final AnonymousAccessPermission defaultAnonymousAccessPermissionOverwritten;
	private final ColorMode defaultColorModeOverwritten;
	private final Language defaultLanguageOverwritten;
	private final PdfDocumentPageFormat defaultPdfDocumentPageFormatOverwritten;
	private final TablePageSize defaultInitialTablePageSizeOverwritten;
	private final UserInterfaceFramework defaultUserInterfaceFrameworkOverwritten;

	public static AnonymousAccessPermission CURRENT_DEFAULT_ANONYMOUS_ACCESS_PERMISSION;
	public static ColorMode CURRENT_DEFAULT_COLOR_MODE;
	public static Language CURRENT_DEFAULT_LANGUAGE;
	public static PdfDocumentPageFormat CURRENT_DEFAULT_PDF_DOCUMENT_PAGE_FORMAT;
	public static TablePageSize CURRENT_DEFAULT_INITIAL_TABLE_PAGE_SIZE;
	public static UserInterfaceFramework CURRENT_DEFAULT_USER_INTERFACE_FRAMEWORK;

	public static AnonymousAccessPermission CURRENT_OVERWRITTEN_ANONYMOUS_ACCESS_PERMISSION;
	public static ColorMode CURRENT_OVERWRITTEN_COLOR_MODE;
	public static Language CURRENT_OVERWRITTEN_LANGUAGE;
	public static PdfDocumentPageFormat CURRENT_OVERWRITTEN_PDF_DOCUMENT_PAGE_FORMAT;
	public static TablePageSize CURRENT_OVERWRITTEN_INITIAL_TABLE_PAGE_SIZE;
	public static UserInterfaceFramework CURRENT_OVERWRITTEN_USER_INTERFACE_FRAMEWORK;

	private ConfigPropertiesBean(
		@Value("${jobvacanciesapp.authUserCurriculumFilesPath}") String authUserCurriculumFilesPath,
		@Value("${jobvacanciesapp.authUserEntityQueryFilesPath}") String authUserEntityQueryFilesPath,
		@Value("${jobvacanciesapp.jobCompanyLogosPath}") String jobCompanyLogosPath,
		@Value("${jobvacanciesapp.useAjaxToRefreshJobCompanyLogos}") boolean useAjaxToRefreshJobCompanyLogos,
		@Value("${jobvacanciesapp.useEntityManagerCache}") boolean useEntityManagerCache,
		@Value("${jobvacanciesapp.useParallelStreams}") boolean useParallelStreams,
		@Value("${jobvacanciesapp.defaultAnonymousAccessPermissionValue}") String defaultAnonymousAccessPermissionValue,
		@Value("${jobvacanciesapp.defaultColorModeCode}") String defaultColorModeCode,
		@Value("${jobvacanciesapp.defaultLanguageCode}") String defaultLanguageCode,
		@Value("${jobvacanciesapp.defaultPdfDocumentPageFormatCode}") String defaultPdfDocumentPageFormatCode,
		@Value("${jobvacanciesapp.defaultInitialTablePageSizeValue}") int defaultInitialTablePageSizeValue,
		@Value("${jobvacanciesapp.defaultUserInterfaceFrameworkCode}") String defaultUserInterfaceFrameworkCode,
		@Value("${jobvacanciesapp.defaultAnonymousAccessPermissionValueOverwritten:-}") String defaultAnonymousAccessPermissionValueOverwritten,
		@Value("${jobvacanciesapp.defaultColorModeCodeOverwritten:-}") String defaultColorModeCodeOverwritten,
		@Value("${jobvacanciesapp.defaultLanguageCodeOverwritten:--}") String defaultLanguageCodeOverwritten,
		@Value("${jobvacanciesapp.defaultPdfDocumentPageFormatCodeOverwritten:---}") String defaultPdfDocumentPageFormatCodeOverwritten,
		@Value("${jobvacanciesapp.defaultInitialTablePageSizeValueOverwritten:0}") int defaultInitialTablePageSizeValueOverwritten,
		@Value("${jobvacanciesapp.defaultUserInterfaceFrameworkCodeOverwritten:-}") String defaultUserInterfaceFrameworkCodeOverwritten) {

		this.authUserCurriculumFilesPath = authUserCurriculumFilesPath;
		this.authUserEntityQueryFilesPath = authUserEntityQueryFilesPath;
		this.jobCompanyLogosPath = jobCompanyLogosPath;
		this.useAjaxToRefreshJobCompanyLogos = useAjaxToRefreshJobCompanyLogos;
		this.useEntityManagerCache = useEntityManagerCache;
		this.useParallelStreams = useParallelStreams;

		this.defaultAnonymousAccessPermission = AnonymousAccessPermission.findByValue(defaultAnonymousAccessPermissionValue);
		this.defaultColorMode = ColorMode.findByCode(defaultColorModeCode);
		this.defaultLanguage = Language.findByCode(defaultLanguageCode);
		this.defaultPdfDocumentPageFormat = PdfDocumentPageFormat.findByCode(defaultPdfDocumentPageFormatCode);
		this.defaultInitialTablePageSize = TablePageSize.findByValue(defaultInitialTablePageSizeValue);
		this.defaultUserInterfaceFramework = UserInterfaceFramework.findByCode(defaultUserInterfaceFrameworkCode);

		this.defaultAnonymousAccessPermissionOverwritten = AnonymousAccessPermission.findByValue(defaultAnonymousAccessPermissionValueOverwritten);
		this.defaultColorModeOverwritten = ColorMode.findByCode(defaultColorModeCodeOverwritten);
		this.defaultLanguageOverwritten = Language.findByCode(defaultLanguageCodeOverwritten);
		this.defaultPdfDocumentPageFormatOverwritten = PdfDocumentPageFormat.findByCode(defaultPdfDocumentPageFormatCodeOverwritten);
		this.defaultInitialTablePageSizeOverwritten = TablePageSize.findByValue(defaultInitialTablePageSizeValueOverwritten);
		this.defaultUserInterfaceFrameworkOverwritten = UserInterfaceFramework.findByCode(defaultUserInterfaceFrameworkCodeOverwritten);
	}

	@PostConstruct
	private void postConstruct() {
		CURRENT_DEFAULT_ANONYMOUS_ACCESS_PERMISSION = Constants.ENUM_UTILS.getCurrentDefaultEnumElement(AnonymousAccessPermission.class, this);
		CURRENT_DEFAULT_COLOR_MODE = Constants.ENUM_UTILS.getCurrentDefaultEnumElement(ColorMode.class, this);
		CURRENT_DEFAULT_LANGUAGE = Constants.ENUM_UTILS.getCurrentDefaultEnumElement(Language.class, this);
		CURRENT_DEFAULT_PDF_DOCUMENT_PAGE_FORMAT = Constants.ENUM_UTILS.getCurrentDefaultEnumElement(PdfDocumentPageFormat.class, this);
		CURRENT_DEFAULT_INITIAL_TABLE_PAGE_SIZE = Constants.ENUM_UTILS.getCurrentDefaultEnumElement(TablePageSize.class, this);
		CURRENT_DEFAULT_USER_INTERFACE_FRAMEWORK = Constants.ENUM_UTILS.getCurrentDefaultEnumElement(UserInterfaceFramework.class, this);

		CURRENT_OVERWRITTEN_ANONYMOUS_ACCESS_PERMISSION = Constants.ENUM_UTILS.getCurrentOverwrittenEnumElement(AnonymousAccessPermission.class, this);
		CURRENT_OVERWRITTEN_COLOR_MODE = Constants.ENUM_UTILS.getCurrentOverwrittenEnumElement(ColorMode.class, this);
		CURRENT_OVERWRITTEN_LANGUAGE = Constants.ENUM_UTILS.getCurrentOverwrittenEnumElement(Language.class, this);
		CURRENT_OVERWRITTEN_PDF_DOCUMENT_PAGE_FORMAT = Constants.ENUM_UTILS.getCurrentOverwrittenEnumElement(PdfDocumentPageFormat.class, this);
		CURRENT_OVERWRITTEN_INITIAL_TABLE_PAGE_SIZE = Constants.ENUM_UTILS.getCurrentOverwrittenEnumElement(TablePageSize.class, this);
		CURRENT_OVERWRITTEN_USER_INTERFACE_FRAMEWORK = Constants.ENUM_UTILS.getCurrentOverwrittenEnumElement(UserInterfaceFramework.class, this);
	}

	public String getInitialTablePageSizeValue(final String pageSize) {
		if(pageSize != null && !BY_DEFAULT_PAGE_SIZE_VALUE_STRING.equals(pageSize)) {
			return pageSize;
		} else {
			int initialTablePageSizeValue;
			try {
				final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromSecurityContextHolder();

				final TablePageSize initialTablePageSize = (sessionAuthUser != null) ? sessionAuthUser.getInitialTablePageSize() : null;

				if(Constants.ENUM_UTILS.hasASpecificValue(initialTablePageSize)) {
					initialTablePageSizeValue = initialTablePageSize.getValue();
				} else {
					initialTablePageSizeValue = this.getCurrentDefaultInitialTablePageSizeValue();
				}
			} catch(final Exception exception) {
				if(log.isErrorEnabled()) {
					final String stackTrace = ThrowableUtils.getStackTrace(exception);
					log.error(StringUtils.getStringJoined("An exception happened when trying to get the initial table page size. The default initial table page size will be used. Exception: ", stackTrace));
				}

				initialTablePageSizeValue = this.getCurrentDefaultInitialTablePageSizeValue();
			}

			final String initialTablePageSizeValueString = String.valueOf(initialTablePageSizeValue);
			return initialTablePageSizeValueString;
		}
	}

	private int getCurrentDefaultInitialTablePageSizeValue() {
		try {
			final int currentDefaultInitialTablePageSizeValue = ConfigPropertiesBean.CURRENT_DEFAULT_INITIAL_TABLE_PAGE_SIZE.getValue();
			return currentDefaultInitialTablePageSizeValue;
		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when trying to get the current default initial table page size. The page size '5' will be used. Exception: ", stackTrace));
			}

			final int currentDefaultInitialTablePageSizeValue = TablePageSize.SIZE_5.getValue();
			return currentDefaultInitialTablePageSizeValue;
		}
	}
}
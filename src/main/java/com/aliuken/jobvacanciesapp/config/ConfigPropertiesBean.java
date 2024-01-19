package com.aliuken.jobvacanciesapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aliuken.jobvacanciesapp.enumtype.AnonymousAccessPermission;
import com.aliuken.jobvacanciesapp.enumtype.UserInterfaceFramework;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.security.SessionUtils;
import com.aliuken.jobvacanciesapp.util.springcore.di.BeanFactoryUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Getter
@Slf4j
public class ConfigPropertiesBean {
	private static final String BY_DEFAULT_PAGE_SIZE_VALUE_STRING = String.valueOf(TablePageSize.BY_DEFAULT.getValue());

	//Non-overwritable properties
	private final String jobCompanyLogosPath;
	private final String authUserCurriculumFilesPath;
	private final boolean useAjaxToRefreshJobCompanyLogos;
	private final boolean useEntityManagerCache;
	private final boolean useParallelStreams;

	//Overwritable properties
	private final Language defaultLanguage;
	private final boolean anonymousAccessAllowed;
	private final TablePageSize defaultInitialTablePageSize;
	private final ColorMode defaultColorMode;
	private final UserInterfaceFramework userInterfaceFramework;

	//Overwriting properties
	private final Language defaultLanguageOverwritten;
	private final AnonymousAccessPermission anonymousAccessPermissionOverwritten;
	private final TablePageSize defaultInitialTablePageSizeOverwritten;
	private final ColorMode defaultColorModeOverwritten;
	private final UserInterfaceFramework userInterfaceFrameworkOverwritten;

	private ConfigPropertiesBean(
		@Value("${jobvacanciesapp.jobCompanyLogosPath}") String jobCompanyLogosPath,
		@Value("${jobvacanciesapp.authUserCurriculumFilesPath}") String authUserCurriculumFilesPath,
		@Value("${jobvacanciesapp.useAjaxToRefreshJobCompanyLogos}") boolean useAjaxToRefreshJobCompanyLogos,
		@Value("${jobvacanciesapp.useEntityManagerCache}") boolean useEntityManagerCache,
		@Value("${jobvacanciesapp.useParallelStreams}") boolean useParallelStreams,
		@Value("${jobvacanciesapp.defaultLanguageCode}") String defaultLanguageCode,
		@Value("${jobvacanciesapp.anonymousAccessAllowed}") boolean anonymousAccessAllowed,
		@Value("${jobvacanciesapp.defaultInitialTablePageSizeValue}") int defaultInitialTablePageSizeValue,
		@Value("${jobvacanciesapp.defaultColorModeCode}") String defaultColorModeCode,
		@Value("${jobvacanciesapp.userInterfaceFrameworkCode}") String userInterfaceFrameworkCode,
		@Value("${jobvacanciesapp.defaultLanguageCodeOverwritten:#{null}}") String defaultLanguageCodeOverwritten,
		@Value("${jobvacanciesapp.anonymousAccessPermissionNameOverwritten:ACCESS_BY_PROPERTY}") String anonymousAccessPermissionNameOverwritten,
		@Value("${jobvacanciesapp.defaultInitialTablePageSizeValueOverwritten:0}") String defaultInitialTablePageSizeValueOverwritten,
		@Value("${jobvacanciesapp.defaultColorModeCodeOverwritten:-}") String defaultColorModeCodeOverwritten,
		@Value("${jobvacanciesapp.userInterfaceFrameworkCodeOverwritten:#{null}}") String userInterfaceFrameworkCodeOverwritten) {

		this.jobCompanyLogosPath = jobCompanyLogosPath;
		this.authUserCurriculumFilesPath = authUserCurriculumFilesPath;
		this.useAjaxToRefreshJobCompanyLogos = useAjaxToRefreshJobCompanyLogos;
		this.useEntityManagerCache = useEntityManagerCache;
		this.useParallelStreams = useParallelStreams;

		this.defaultLanguage = Language.findByCode(defaultLanguageCode);
		this.anonymousAccessAllowed = anonymousAccessAllowed;
		this.defaultInitialTablePageSize = TablePageSize.findByValue(defaultInitialTablePageSizeValue);
		this.defaultColorMode = ColorMode.findByCode(defaultColorModeCode);
		this.userInterfaceFramework = UserInterfaceFramework.findByCode(userInterfaceFrameworkCode);

		this.defaultLanguageOverwritten = Language.findByCode(defaultLanguageCodeOverwritten);
		this.anonymousAccessPermissionOverwritten = ConfigPropertiesBean.getAnonymousAccessPermissionOverwritten(anonymousAccessPermissionNameOverwritten);
		this.defaultInitialTablePageSizeOverwritten = TablePageSize.findByValue(Integer.valueOf(defaultInitialTablePageSizeValueOverwritten));
		this.defaultColorModeOverwritten = ColorMode.findByCode(defaultColorModeCodeOverwritten);
		this.userInterfaceFrameworkOverwritten = UserInterfaceFramework.findByCode(userInterfaceFrameworkCodeOverwritten);
	}

	private static AnonymousAccessPermission getAnonymousAccessPermissionOverwritten(final String anonymousAccessPermissionNameOverwritten) {
		AnonymousAccessPermission anonymousAccessPermissionOverwritten = AnonymousAccessPermission.valueOf(anonymousAccessPermissionNameOverwritten);
		if(anonymousAccessPermissionOverwritten == null) {
			anonymousAccessPermissionOverwritten = AnonymousAccessPermission.ACCESS_BY_PROPERTY;
		}
		return anonymousAccessPermissionOverwritten;
	}

	public Language getCurrentDefaultLanguage() {
		final Language defaultLanguage;
		if(this.defaultLanguageOverwritten != null) {
			defaultLanguage = this.defaultLanguageOverwritten;
		} else if(this.defaultLanguage != null) {
			defaultLanguage = this.defaultLanguage;
		} else {
			defaultLanguage = Language.ENGLISH;
		}

		return defaultLanguage;
	}

	public boolean getCurrentAnonymousAccessAllowed() {
		final boolean anonymousAccessAllowed;
		if(AnonymousAccessPermission.ACCESS_ALLOWED == this.anonymousAccessPermissionOverwritten) {
			anonymousAccessAllowed = true;
		} else if(AnonymousAccessPermission.ACCESS_NOT_ALLOWED == this.anonymousAccessPermissionOverwritten) {
			anonymousAccessAllowed = false;
		} else {
			anonymousAccessAllowed = this.anonymousAccessAllowed;
		}

		return anonymousAccessAllowed;
	}

	public TablePageSize getCurrentDefaultInitialTablePageSize() {
		final TablePageSize currentDefaultInitialTablePageSize;
		if(TablePageSize.hasASpecificValue(this.defaultInitialTablePageSizeOverwritten)) {
			currentDefaultInitialTablePageSize = this.defaultInitialTablePageSizeOverwritten;
		} else if(TablePageSize.hasASpecificValue(this.defaultInitialTablePageSize)) {
			currentDefaultInitialTablePageSize = this.defaultInitialTablePageSize;
		} else {
			currentDefaultInitialTablePageSize = TablePageSize.SIZE_5;
		}

		return currentDefaultInitialTablePageSize;
	}

	public ColorMode getCurrentDefaultColorMode() {
		final ColorMode currentDefaultColorMode;
		if(ColorMode.hasASpecificValue(this.defaultColorModeOverwritten)) {
			currentDefaultColorMode = this.defaultColorModeOverwritten;
		} else if(ColorMode.hasASpecificValue(this.defaultColorMode)) {
			currentDefaultColorMode = this.defaultColorMode;
		} else {
			currentDefaultColorMode = ColorMode.LIGHT;
		}

		return currentDefaultColorMode;
	}

	public UserInterfaceFramework getCurrentUserInterfaceFramework() {
		final UserInterfaceFramework userInterfaceFramework;
		if(this.userInterfaceFrameworkOverwritten != null) {
			userInterfaceFramework = this.userInterfaceFrameworkOverwritten;
		} else if(this.userInterfaceFramework != null) {
			userInterfaceFramework = this.userInterfaceFramework;
		} else {
			userInterfaceFramework = UserInterfaceFramework.MATERIAL_DESIGN;
		}

		return userInterfaceFramework;
	}

	public String getInitialTablePageSizeValue(final String pageSize) {
		if(pageSize != null && !BY_DEFAULT_PAGE_SIZE_VALUE_STRING.equals(pageSize)) {
			return pageSize;
		} else {
			int initialTablePageSizeValue;
			try {
				final AuthUserService authUserService = BeanFactoryUtils.getBean(AuthUserService.class);
				final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromSecurityContextHolder(authUserService);

				final TablePageSize initialTablePageSize = (sessionAuthUser != null) ? sessionAuthUser.getInitialTablePageSize() : null;

				if(TablePageSize.hasASpecificValue(initialTablePageSize)) {
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
			final TablePageSize currentDefaultInitialTablePageSize = this.getCurrentDefaultInitialTablePageSize();

			final int currentDefaultInitialTablePageSizeValue = currentDefaultInitialTablePageSize.getValue();
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
package com.aliuken.jobvacanciesapp;

import com.aliuken.jobvacanciesapp.enumtype.AnonymousAccessPermission;
import com.aliuken.jobvacanciesapp.enumtype.UserInterfaceFramework;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Currency;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.util.javase.ConfigurableEnumUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamUtilsImpl;
import com.aliuken.jobvacanciesapp.util.javase.stream.superinterface.StreamUtils;
import com.aliuken.jobvacanciesapp.util.javase.time.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.javase.time.DateUtils;
import com.aliuken.jobvacanciesapp.util.javase.time.superinterface.TemporalUtils;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Constants {

	public static final String EMPTY_STRING = "";
	public static final String SPACE = " ";
	public static final String HYPHEN = "-";
	public static final String DOT = ".";
	public static final String QUESTION_MARK = "?";
	public static final String NEWLINE = "\n";
	public static final String FIELD_NAME_VALUE_SEPARATOR = ": ";
	public static final String MAP_ENTRY_PREFIX = "\n- ";
	public static final String KEY_VALUE_SEPARATOR = " -> ";

	public static final ConfigurableEnumUtils<String,AnonymousAccessPermission> ANONYMOUS_ACCESS_PERMISSION_UTILS = ConfigurableEnumUtils.getInstance(String.class, AnonymousAccessPermission.class);
	public static final ConfigurableEnumUtils<String,ColorMode> COLOR_MODE_UTILS = ConfigurableEnumUtils.getInstance(String.class, ColorMode.class);
	public static final ConfigurableEnumUtils<String,Currency> CURRENCY_UTILS = ConfigurableEnumUtils.getInstance(String.class, Currency.class);
	public static final ConfigurableEnumUtils<String,Language> LANGUAGE_UTILS = ConfigurableEnumUtils.getInstance(String.class, Language.class);
	public static final ConfigurableEnumUtils<String,PdfDocumentPageFormat> PDF_DOCUMENT_PAGE_FORMAT_UTILS = ConfigurableEnumUtils.getInstance(String.class, PdfDocumentPageFormat.class);
	public static final ConfigurableEnumUtils<Integer,TablePageSize> TABLE_PAGE_SIZE_UTILS = ConfigurableEnumUtils.getInstance(Integer.class, TablePageSize.class);
	public static final ConfigurableEnumUtils<String,TableSortingDirection> TABLE_SORTING_DIRECTION_UTILS = ConfigurableEnumUtils.getInstance(String.class, TableSortingDirection.class);
	public static final ConfigurableEnumUtils<String,UserInterfaceFramework> USER_INTERFACE_FRAMEWORK_UTILS = ConfigurableEnumUtils.getInstance(String.class, UserInterfaceFramework.class);

	public static final TemporalUtils<LocalDate> DATE_UTILS = DateUtils.getInstance();
	public static final TemporalUtils<LocalDateTime> DATE_TIME_UTILS = DateTimeUtils.getInstance();

	public static final String SESSION_AUTH_USER_ID = "sessionAuthUserId";
	public static final String SESSION_ACCOUNT_DELETED = "sessionAccountDeleted";

	public static final String DELETE_ACCOUNT_SUCCESS_MESSAGE_NAME = "deleteAuthUser.successMsg";
	public static final String DELETE_ACCOUNT_SUCCESS_MESSAGE = "deleteAccountSuccessMsg";

	public static final Long ANONYMOUS_AUTH_USER_ID = 1L;

	public static final Long NO_SELECTED_LOGO_ID = 0L;
	public static final String NO_SELECTED_LOGO_FILE_PATH = "no-logo.png";

	public static final String DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD = Constants.HYPHEN;
	public static final String DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING = Constants.HYPHEN;

	public static final String HOME_PATH = "/";
	public static final String LOGIN_PATH = "/login";

	public static final String INSTANTIATION_NOT_ALLOWED = "Cannot instantiate class ";
}

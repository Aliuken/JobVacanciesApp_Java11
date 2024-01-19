package com.aliuken.jobvacanciesapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aliuken.jobvacanciesapp.util.javase.stream.ParallelStreamUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.SequentialStreamUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.superclass.StreamUtils;
import com.aliuken.jobvacanciesapp.util.javase.time.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.javase.time.DateUtils;
import com.aliuken.jobvacanciesapp.util.javase.time.superinterface.TemporalUtils;

public class Constants {

	public static final String EMPTY_STRING = "";

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	public static final TemporalUtils<LocalDate> DATE_UTILS = DateUtils.getInstance();
	public static final TemporalUtils<LocalDateTime> DATE_TIME_UTILS = DateTimeUtils.getInstance();

	public static final StreamUtils SEQUENTIAL_STREAM_UTILS = SequentialStreamUtils.getInstance();
	public static final StreamUtils PARALLEL_STREAM_UTILS = ParallelStreamUtils.getInstance();

	public static final String SESSION_AUTH_USER = "sessionAuthUser";
	public static final String SESSION_ACCOUNT_DELETED = "sessionAccountDeleted";

	public static final String DELETE_ACCOUNT_SUCCESS_MESSAGE_NAME = "deleteAuthUser.successMsg";
	public static final String DELETE_ACCOUNT_SUCCESS_MESSAGE = "deleteAccountSuccessMsg";

	public static final Long ANONYMOUS_AUTH_USER_ID = 1L;

	public static final Long NO_SELECTED_LOGO_ID = 0L;
	public static final String NO_SELECTED_LOGO_FILE_PATH = "no-logo.png";

	public static final String DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD = "-";
	public static final String DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING = "-";

	public static final String HOME_PATH = "/";
	public static final String LOGIN_PATH = "/login";

}

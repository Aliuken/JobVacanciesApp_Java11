package com.aliuken.jobvacanciesapp;

import java.time.format.DateTimeFormatter;

public class Constants {

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static final String SESSION_AUTH_USER = "sessionAuthUser";
    
    public static final String ANONYMOUS_AUTH_USER = "anonymousAuthUser";
    
    public static final Long ANONYMOUS_AUTH_USER_ID = 0L;
    
    public static final Long NO_SELECTED_LOGO_ID = 0L;
    
    public static final String NO_SELECTED_LOGO_FILE_PATH = "no-logo.png";

    public static final String DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD = "-";
    
    public static final String DEFAULT_VALUE_WHEN_SERIALIZING_NULL_STRING = "-";

}

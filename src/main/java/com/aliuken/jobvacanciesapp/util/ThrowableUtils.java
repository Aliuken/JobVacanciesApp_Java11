package com.aliuken.jobvacanciesapp.util;

public class ThrowableUtils {
	public static String getRootCauseMessage(Throwable throwable) {
		Throwable rootCause = ThrowableUtils.getRootCause(throwable);

		if(rootCause == null) {
			return null;
		}

	    String message = rootCause.getMessage();
	    return message;
	}

	public static Throwable getRootCause(Throwable throwable) {
	    if(throwable == null) {
	    	return null;
	    }

	    Throwable rootCause = throwable;
	    while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
	        rootCause = rootCause.getCause();
	    }
	    return rootCause;
	}
}

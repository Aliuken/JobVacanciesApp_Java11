package com.aliuken.jobvacanciesapp.util.javase;

import com.aliuken.jobvacanciesapp.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public class ThrowableUtils {

	private ThrowableUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	public static String getRootCauseMessage(final Throwable throwable) {
		final Throwable rootCause = ThrowableUtils.getRootCause(throwable);
		if(rootCause == null) {
			return null;
		}

		final String message = rootCause.getMessage();
		return message;
	}

	public static Throwable getRootCause(final Throwable throwable) {
		if(throwable == null) {
			return null;
		}

		Throwable rootCause = throwable;
		while(rootCause.getCause() != null && rootCause.getCause() != rootCause) {
			rootCause = rootCause.getCause();
		}
		return rootCause;
	}

	// Alternative "Exception getStackTrace expression / display / shell / terminal / inspect":
	// java.io.StringWriter stringWriter = new java.io.StringWriter(); e.printStackTrace(new java.io.PrintWriter(stringWriter)); stringWriter.toString();
	public static String getStackTrace(final Throwable throwable) {
		try(
			final StringWriter stringWriter = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(stringWriter);
		) {
			throwable.printStackTrace(printWriter);

			final String exceptionStackTrace = stringWriter.toString();
			return exceptionStackTrace;
		} catch(Exception newException) {
			final String newExceptionMessage = StringUtils.getStringJoined("An exception happened when trying to print the stack trace of the exception '", throwable.getMessage(), "'");
			log.error(newExceptionMessage, newException);
			return newExceptionMessage;
		}
	}
}

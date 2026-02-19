package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.util.Objects;

@Data
public class LambdaResultWithExceptionDTO<T> implements Serializable {
	private static final long serialVersionUID = 417055475888522040L;

	private final T returnedValue;

	private final Throwable throwable;

	public boolean hasException() {
		final boolean result = (throwable != null);
		return result;
	}

	@Override
	public @NonNull String toString() {
		final String returnedValueString = String.valueOf(returnedValue);
		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(throwable);

		final String result = StringUtils.getStringJoined("LambdaResultWithExceptionDTO [returnedValue=", returnedValueString, ", throwable=", rootCauseMessage, "]");
		return result;
	}

	@Override
	public int hashCode() {
		return Objects.hash(returnedValue, throwable);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}

		final LambdaResultWithExceptionDTO<T> other = GenericsUtils.cast(obj);

		final boolean result = Objects.equals(returnedValue, other.returnedValue) && Objects.equals(throwable, other.throwable);
		return result;
	}
}

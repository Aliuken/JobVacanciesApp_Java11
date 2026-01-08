package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.function.Function;

@Data
public class BigDecimalFromStringConversionResult implements Serializable {
	private static final long serialVersionUID = 417055475888522041L;

	private final Function<Language, String> conversionErrorFunction;
	private final BigDecimal conversionResult;

	public static @NonNull BigDecimalFromStringConversionResult getNewInstanceWithError(Function<Language, String> conversionErrorFunction) {
		return new BigDecimalFromStringConversionResult(conversionErrorFunction, null);
	}

	public static @NonNull BigDecimalFromStringConversionResult getNewInstanceWithoutError(BigDecimal conversionResult) {
		return new BigDecimalFromStringConversionResult(null, conversionResult);
	}

	@Override
	public @NonNull String toString() {
		final String conversionErrorFunctionString = String.valueOf(conversionErrorFunction);
		final String conversionResultString = String.valueOf(conversionResult);

		final String result = StringUtils.getStringJoined("BigDecimalFromStringConversionResult [conversionErrorFunction=", conversionErrorFunctionString, ", conversionResult=", conversionResultString, "]");
		return result;
	}
}

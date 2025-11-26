package com.aliuken.jobvacanciesapp.util.javase;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.dto.BigDecimalFromStringConversionResult;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;

public class NumericUtils {
	private static final Function<Language, String> FIELD_NAME_NULL_ERROR_FUNCTION = (language) -> I18nUtils.getInternationalizedMessage(language, "fieldName.isNull", null);
	private static final Function<Language, String> FIELD_NAME_BLANK_ERROR_FUNCTION = (language) -> I18nUtils.getInternationalizedMessage(language, "fieldName.isBlank", null);
	private static final BigDecimalFromStringConversionResult FIELD_NAME_NULL_CONVERSION_RESULT = BigDecimalFromStringConversionResult.getNewInstanceWithError(FIELD_NAME_NULL_ERROR_FUNCTION);
	private static final BigDecimalFromStringConversionResult FIELD_NAME_BLANK_CONVERSION_RESULT = BigDecimalFromStringConversionResult.getNewInstanceWithError(FIELD_NAME_BLANK_ERROR_FUNCTION);

	private NumericUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	public static BigDecimalFromStringConversionResult getBigDecimalFromStringConversionResult(final String fieldNameProperty, final String fieldValue, final int integerPartSize, final int fractionalPartSize) {
		if(fieldNameProperty == null) {
			return FIELD_NAME_NULL_CONVERSION_RESULT;
		}
		final String strippedFieldNameProperty = fieldNameProperty.strip();
		if(strippedFieldNameProperty.isEmpty()) {
			return FIELD_NAME_BLANK_CONVERSION_RESULT;
		} else if(fieldValue == null) {
			final Function<Language, String> conversionErrorFunction = (language) -> I18nUtils.getInternationalizedMessageWithFieldNamePropertyParameter(language, "fieldValue.isNull", new Object[]{strippedFieldNameProperty});
			return BigDecimalFromStringConversionResult.getNewInstanceWithError(conversionErrorFunction);
		} else if(integerPartSize <= 0) {
			final Function<Language, String> conversionErrorFunction = (language) -> I18nUtils.getInternationalizedMessageWithFieldNamePropertyParameter(language, "decimalFieldValue.integerPartLength.notValid", new Object[]{strippedFieldNameProperty});
			return BigDecimalFromStringConversionResult.getNewInstanceWithError(conversionErrorFunction);
		} else if(fractionalPartSize < 0) {
			final Function<Language, String> conversionErrorFunction = (language) -> I18nUtils.getInternationalizedMessageWithFieldNamePropertyParameter(language, "decimalFieldValue.fractionalPartLength.notValid", new Object[]{strippedFieldNameProperty});
			return BigDecimalFromStringConversionResult.getNewInstanceWithError(conversionErrorFunction);
		}

		final String decimalNumberRegex;
		final String minimumNumberString;
		final String maximumNumberString;
		if(fractionalPartSize == 0) {
			final String integerPartSizeString = Objects.toString(integerPartSize);
			decimalNumberRegex = StringUtils.getStringJoined("^\\d{1,", integerPartSizeString, "}$");
			minimumNumberString = "1";
			maximumNumberString = "9".repeat(integerPartSize);
		} else {
			final String integerPartSizeString = Objects.toString(integerPartSize);
			final String fractionalPartSizeString = Objects.toString(fractionalPartSize);
			decimalNumberRegex = StringUtils.getStringJoined("^\\d{1,", integerPartSizeString, "}\\.\\d{1,", fractionalPartSizeString, "}$");
			minimumNumberString = StringUtils.getStringJoined("0.", "0".repeat(fractionalPartSize - 1), "1");
			maximumNumberString = StringUtils.getStringJoined("9".repeat(integerPartSize), ".", "9".repeat(fractionalPartSize));
		}

		if(!fieldValue.matches(decimalNumberRegex)) {
			final Function<Language, String> conversionErrorFunction = (language) -> I18nUtils.getInternationalizedMessageWithFieldNamePropertyParameter(language, "decimalFieldValue.notValid", new Object[]{strippedFieldNameProperty, minimumNumberString, maximumNumberString});
			return BigDecimalFromStringConversionResult.getNewInstanceWithError(conversionErrorFunction);
		} else if(fieldValue.startsWith("00")) {
			final Function<Language, String> conversionErrorFunction = (language) -> I18nUtils.getInternationalizedMessageWithFieldNamePropertyParameter(language, "decimalFieldValue.leftZerosError", new Object[]{strippedFieldNameProperty});
			return BigDecimalFromStringConversionResult.getNewInstanceWithError(conversionErrorFunction);
		}

		final BigDecimal decimal = new BigDecimal(fieldValue);
		final BigDecimal minimumNumber = new BigDecimal(minimumNumberString);
		if(decimal.compareTo(minimumNumber) < 0) {
			final Function<Language, String> conversionErrorFunction = (language) -> I18nUtils.getInternationalizedMessageWithFieldNamePropertyParameter(language, "decimalFieldValue.tooSmall", new Object[]{strippedFieldNameProperty, minimumNumberString});
			return BigDecimalFromStringConversionResult.getNewInstanceWithError(conversionErrorFunction);
		}

		return BigDecimalFromStringConversionResult.getNewInstanceWithoutError(decimal);
	}
}

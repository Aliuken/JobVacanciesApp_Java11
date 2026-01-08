package com.aliuken.jobvacanciesapp.model.converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Currency;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter(autoApply=true)
public class CurrencyConverter implements AttributeConverter<Currency, String> {

	@Override
	public String convertToDatabaseColumn(final Currency currency) {
		if(currency == null) {
			return Currency.BY_DEFAULT.getSymbol();
		}

		final String currencySymbol = currency.getSymbol();
		return currencySymbol;
	}

	@Override
	public Currency convertToEntityAttribute(final String currencySymbol) {
		Currency currency = Currency.findBySymbol(currencySymbol);
		Objects.requireNonNull(currency, "currency cannot be null");
		return currency;
	}
}
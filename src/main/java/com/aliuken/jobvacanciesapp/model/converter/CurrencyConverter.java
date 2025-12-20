package com.aliuken.jobvacanciesapp.model.converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Currency;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
		final Currency currency;
		if(currencySymbol != null) {
			currency = Currency.findBySymbol(currencySymbol);
		} else {
			currency = Currency.BY_DEFAULT;
		}

		return currency;
	}
}
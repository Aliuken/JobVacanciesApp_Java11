package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

public enum Currency implements ConfigurableEnum<Currency> {
	BY_DEFAULT("-", "currency.byDefault"),
	US_DOLLAR ("$", "currency.usDollar"),
	EURO      ("€", "currency.euro");

	@NotNull
	private final String symbol;

	@NotNull
	private final String messageName;

	private Currency(final String symbol, final String messageName) {
		this.symbol = symbol;
		this.messageName = messageName;
	}

	public String getSymbol() {
		return symbol;
	}

	@Override
	public String getMessageName() {
		return messageName;
	}

	public static Currency findBySymbol(final String symbol) {
		final Currency currency;
		if(symbol != null) {
			currency = Constants.PARALLEL_STREAM_UTILS.ofEnum(Currency.class)
				.filter(currencyAux -> symbol.equals(currencyAux.symbol))
				.findFirst()
				.orElse(null);
		} else {
			currency = null;
		}

		return currency;
	}

	public static Currency[] valuesWithoutByDefault() {
//		final Currency[] values = Currency.values();
//		final Currency[] values = Constants.PARALLEL_STREAM_UTILS.ofEnum(Currency.class)
//			.toArray(Currency[]::new);
//
//		final Currency[] valuesWithoutByDefault = new Currency[values.length - 1];
//		for(int i = 0; i < valuesWithoutByDefault.length; i++) {
//			valuesWithoutByDefault[i] = values[i + 1];
//		}

		final List<Currency> valuesWithoutByDefaultList = Constants.ENUM_UTILS.getSpecificEnumElements(Currency.class);
		final Currency[] valuesWithoutByDefault = valuesWithoutByDefaultList.toArray(new Currency[valuesWithoutByDefaultList.size()]);
		return valuesWithoutByDefault;
	}

	@Override
	public ConfigurableEnum<Currency> getOverwrittenEnumElement(ConfigPropertiesBean configPropertiesBean) {
		return Currency.BY_DEFAULT;
	}

	@Override
	public ConfigurableEnum<Currency> getOverwritableEnumElement(ConfigPropertiesBean configPropertiesBean) {
		return Currency.BY_DEFAULT;
	}

	@Override
	public ConfigurableEnum<Currency> getFinalDefaultEnumElement() {
		return Currency.US_DOLLAR;
	}
}

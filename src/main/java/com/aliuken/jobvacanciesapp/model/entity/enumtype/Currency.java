package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

public enum Currency implements ConfigurableEnum<Currency> {
	BY_DEFAULT("-", "currency.byDefault"),
	US_DOLLAR ("$", "currency.usDollar"),
	EURO      ("€", "currency.euro");

	@Getter
	private final @NonNull String symbol;

	@Getter
	private final @NonNull String messageName;

	private Currency(final @NonNull String symbol, final @NonNull String messageName) {
		this.symbol = symbol;
		this.messageName = messageName;
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

	public static @NonNull Currency[] getSpecificEnumElements() {
		final Currency[] enumElementsWithoutByDefault = Constants.ENUM_UTILS.getSpecificEnumElements(Currency.class);
		return enumElementsWithoutByDefault;
	}

	@Override
	public @NonNull Class<Currency> getEnumClass() {
		return Currency.class;
	}

	@Override
	public @NonNull ConfigurableEnum<Currency> getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		return Currency.BY_DEFAULT;
	}

	@Override
	public @NonNull ConfigurableEnum<Currency> getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		return Currency.BY_DEFAULT;
	}

	@Override
	public @NonNull ConfigurableEnum<Currency> getFinalDefaultEnumElement() {
		return Currency.US_DOLLAR;
	}
}

package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

public enum Currency implements ConfigurableEnum<Currency> {
	BY_DEFAULT("-", "currency.byDefault"),
	US_DOLLAR ("$", "currency.usDollar"),
	EURO      ("€", "currency.euro");

	@Getter
    @NotNull
	private final String symbol;

	@Getter
	@NotNull
	private final String messageName;

	private Currency(final String symbol, final String messageName) {
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

	public static Currency[] valuesWithoutByDefault() {
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

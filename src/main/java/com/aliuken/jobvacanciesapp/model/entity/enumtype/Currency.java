package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamStaticUtils;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

public enum Currency implements ConfigurableEnum<String,Currency> {
	BY_DEFAULT("-", "currency.byDefault"),
	US_DOLLAR ("$", "currency.usDollar"),
	EURO      ("€", "currency.euro");

	@Getter
	private final @NonNull String code;

	@Getter
	private final @NonNull String messageName;

	private Currency(final @NonNull String code, final @NonNull String messageName) {
		this.code = code;
		this.messageName = messageName;
	}

	public static Currency findByCode(final String code) {
		if(LogicalUtils.isNullOrEmptyString(code)) {
			return null;
		}

		final Currency currency = StreamStaticUtils.ofEnum(Currency.class, false)
			.filter(currencyAux -> code.equals(currencyAux.code))
			.findFirst()
			.orElse(null);
		return currency;
	}

	@Override
	public @NonNull Currency getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		return Currency.BY_DEFAULT;
	}

	@Override
	public @NonNull Currency getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		return Currency.BY_DEFAULT;
	}

	@Override
	public @NonNull Currency getFinalDefaultEnumElement() {
		return Currency.US_DOLLAR;
	}
}

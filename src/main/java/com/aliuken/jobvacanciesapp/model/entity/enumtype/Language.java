package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.util.Locale;

public enum Language implements ConfigurableEnum<Language> {
	BY_DEFAULT("--", "language.byDefault", null),
	ENGLISH   ("en", "language.english",   new Locale("en")),
	SPANISH   ("es", "language.español",   new Locale("es"));

	@Getter
	private final @NonNull String code;

	@Getter
	private final @NonNull String messageName;

	@Getter
	private final Locale locale;

	private Language(final @NonNull String code, final @NonNull String messageName, final Locale locale) {
		this.code = code;
		this.messageName = messageName;
		this.locale = locale;
	}

	public static Language findByCode(final String code) {
		if(LogicalUtils.isNullOrEmptyString(code)) {
			return null;
		}

		final Language language = Constants.PARALLEL_STREAM_UTILS.ofEnum(Language.class)
			.filter(languageAux -> code.equals(languageAux.code))
			.findFirst()
			.orElse(null);
		return language;
	}

	public static @NonNull Language[] getSpecificEnumElements() {
		final Language[] enumElementsWithoutByDefault = Constants.ENUM_UTILS.getSpecificEnumElements(Language.class);
		return enumElementsWithoutByDefault;
	}

	@Override
	public @NonNull Class<Language> getEnumClass() {
		return Language.class;
	}

	@Override
	public ConfigurableEnum<Language> getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final Language language = configPropertiesBean.getDefaultLanguageOverwritten();
		return language;
	}

	@Override
	public ConfigurableEnum<Language> getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final Language language = configPropertiesBean.getDefaultLanguage();
		return language;
	}

	@Override
	public @NonNull ConfigurableEnum<Language> getFinalDefaultEnumElement() {
		return Language.ENGLISH;
	}
}
package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;

public enum Language implements ConfigurableEnum<Language> {
	BY_DEFAULT("--", "language.byDefault", null),
	ENGLISH   ("en", "language.english",   new Locale("en")),
	SPANISH   ("es", "language.español",   new Locale("es"));

	@NotNull
	private final String code;

	@NotNull
	private final String messageName;

	@NotNull
	private final Locale locale;

	private Language(final String code, final String messageName, final Locale locale) {
		this.code = code;
		this.messageName = messageName;
		this.locale = locale;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessageName() {
		return messageName;
	}

	public Locale getLocale() {
		return locale;
	}

	public static Language findByCode(final String code) {
		final Language language;
		if(code != null) {
			language = Constants.PARALLEL_STREAM_UTILS.ofEnum(Language.class)
				.filter(languageAux -> code.equals(languageAux.code))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Language code does not exist"));
		} else {
			language = null;
		}

		return language;
	}

	public static Language[] valuesWithoutByDefault() {
//		final Language[] values = Language.values();
//		final Language[] values = Constants.PARALLEL_STREAM_UTILS.ofEnum(Language.class)
//			.toArray(Language[]::new);
//
//		final Language[] valuesWithoutByDefault = new Language[values.length - 1];
//		for(int i = 0; i < valuesWithoutByDefault.length; i++) {
//			valuesWithoutByDefault[i] = values[i + 1];
//		}

		final List<Language> valuesWithoutByDefaultList = Constants.ENUM_UTILS.getSpecificEnumElements(Language.class);
		final Language[] valuesWithoutByDefault = valuesWithoutByDefaultList.toArray(new Language[valuesWithoutByDefaultList.size()]);
		return valuesWithoutByDefault;
	}

	@Override
	public ConfigurableEnum<Language> getOverwrittenEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final Language language = configPropertiesBean.getDefaultLanguageOverwritten();
		return language;
	}

	@Override
	public ConfigurableEnum<Language> getOverwritableEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final Language language = configPropertiesBean.getDefaultLanguage();
		return language;
	}

	@Override
	public ConfigurableEnum<Language> getFinalDefaultEnumElement() {
		return Language.ENGLISH;
	}

	@Override
	public ConfigurableEnum<Language>[] getEnumElements() {
		return Language.values();
	}
}
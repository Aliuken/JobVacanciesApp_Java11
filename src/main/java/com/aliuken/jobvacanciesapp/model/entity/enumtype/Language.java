package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamStaticUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamUtilsImpl;
import com.aliuken.jobvacanciesapp.util.javase.stream.superinterface.StreamUtils;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.util.Locale;

public enum Language implements ConfigurableEnum<String,Language> {
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

		final Language language = StreamStaticUtils.ofEnum(Language.class, false)
			.filter(languageAux -> code.equals(languageAux.code))
			.findFirst()
			.orElse(null);
		return language;
	}

	@Override
	public Language getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final Language language = configPropertiesBean.getDefaultLanguageOverwritten();
		return language;
	}

	@Override
	public Language getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final Language language = configPropertiesBean.getDefaultLanguage();
		return language;
	}

	@Override
	public @NonNull Language getFinalDefaultEnumElement() {
		return Language.ENGLISH;
	}
}
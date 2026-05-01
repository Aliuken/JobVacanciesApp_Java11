package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.SequentialStreamStrategy;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamStaticUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamUtilsImpl;
import com.aliuken.jobvacanciesapp.util.javase.stream.superinterface.StreamUtils;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

public enum ColorMode implements ConfigurableEnum<String,ColorMode> {
	BY_DEFAULT("-", "default", "colorMode.byDefault"),
	LIGHT     ("L", "light",   "colorMode.light"),
	DARK      ("D", "dark",    "colorMode.dark");

	@Getter
	private final @NonNull String code;

	@Getter
	private final @NonNull String value;

	@Getter
	private final @NonNull String messageName;

	private ColorMode(final @NonNull String code, final @NonNull String value, final @NonNull String messageName) {
		this.code = code;
		this.value = value;
		this.messageName = messageName;
	}

	public static ColorMode findByCode(final String code) {
		if(LogicalUtils.isNullOrEmptyString(code)) {
			return null;
		}

		final ColorMode colorMode = StreamStaticUtils.ofEnum(ColorMode.class, false)
			.filter(colorModeAux -> code.equals(colorModeAux.code))
			.findFirst()
			.orElse(null);
		return colorMode;
	}

	public static ColorMode findByValue(final String value) {
		if(LogicalUtils.isNullOrEmptyString(value)) {
			return null;
		}

		final ColorMode colorMode = StreamStaticUtils.ofEnum(ColorMode.class, false)
			.filter(colorModeAux -> value.equals(colorModeAux.value))
			.findFirst()
			.orElse(null);
		return colorMode;
	}

	@Override
	public ColorMode getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final ColorMode colorMode = configPropertiesBean.getDefaultColorModeOverwritten();
		return colorMode;
	}

	@Override
	public ColorMode getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final ColorMode colorMode = configPropertiesBean.getDefaultColorMode();
		return colorMode;
	}

	@Override
	public @NonNull ColorMode getFinalDefaultEnumElement() {
		return ColorMode.LIGHT;
	}
}

package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public enum ColorMode implements ConfigurableEnum<ColorMode> {
	BY_DEFAULT("-", "default", "colorMode.byDefault"),
	LIGHT     ("L", "light",   "colorMode.light"),
	DARK      ("D", "dark",    "colorMode.dark");

	@Getter
    @NotNull
	private final String code;

	@Getter
	@NotNull
	private final String value;

	@Getter
	@NotNull
	private final String messageName;

	private ColorMode(final String code, final String value, final String messageName) {
		this.code = code;
		this.value = value;
		this.messageName = messageName;
	}

	public static ColorMode findByCode(final String code) {
		final ColorMode colorMode;
		if(code != null) {
			colorMode = Constants.PARALLEL_STREAM_UTILS.ofEnum(ColorMode.class)
				.filter(colorModeAux -> code.equals(colorModeAux.code))
				.findFirst()
				.orElse(null);
		} else {
			colorMode = null;
		}

		return colorMode;
	}

	public static ColorMode findByValue(final String value) {
		final ColorMode colorMode;
		if(value != null) {
			colorMode = Constants.PARALLEL_STREAM_UTILS.ofEnum(ColorMode.class)
				.filter(colorModeAux -> value.equals(colorModeAux.value))
				.findFirst()
				.orElse(null);
		} else {
			colorMode = null;
		}

		return colorMode;
	}

	@Override
	public ConfigurableEnum<ColorMode> getOverwrittenEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final ColorMode colorMode = configPropertiesBean.getDefaultColorModeOverwritten();
		return colorMode;
	}

	@Override
	public ConfigurableEnum<ColorMode> getOverwritableEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final ColorMode colorMode = configPropertiesBean.getDefaultColorMode();
		return colorMode;
	}

	@Override
	public ConfigurableEnum<ColorMode> getFinalDefaultEnumElement() {
		return ColorMode.LIGHT;
	}
}

package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.Constants;

public enum ColorMode implements Serializable {
	BY_DEFAULT("-", "default", "colorMode.byDefault"),
	LIGHT("L", "light", "colorMode.light"),
	DARK("D", "dark", "colorMode.dark");

	@NotNull
	private final String code;

	@NotNull
	private final String value;

	@NotNull
	private final String messageName;

	private ColorMode(final String code, final String value, final String messageName) {
		this.code = code;
		this.value = value;
		this.messageName = messageName;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public String getMessageName() {
		return messageName;
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

	public static boolean hasASpecificValue(final ColorMode colorMode) {
		final boolean result = (colorMode != null && ColorMode.BY_DEFAULT != colorMode);
		return result;
	}

}

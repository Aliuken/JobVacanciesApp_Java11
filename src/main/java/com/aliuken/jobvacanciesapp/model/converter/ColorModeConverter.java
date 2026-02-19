package com.aliuken.jobvacanciesapp.model.converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter(autoApply=true)
public class ColorModeConverter implements AttributeConverter<ColorMode, String> {

	@Override
	public String convertToDatabaseColumn(final ColorMode colorMode) {
		if(colorMode == null) {
			return ColorMode.BY_DEFAULT.getCode();
		}

		final String colorModeCode = colorMode.getCode();
		return colorModeCode;
	}

	@Override
	public ColorMode convertToEntityAttribute(final String colorModeCode) {
		ColorMode colorMode = ColorMode.findByCode(colorModeCode);
		Objects.requireNonNull(colorMode, "colorMode cannot be null");
		return colorMode;
	}
}
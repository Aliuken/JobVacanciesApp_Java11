package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

public enum TablePageSize implements ConfigurableEnum<TablePageSize> {
	BY_DEFAULT(0,   "tablePageSize.byDefault"),
	SIZE_5    (5,   "tablePageSize.5"),
	SIZE_10   (10,  "tablePageSize.10"),
	SIZE_25   (25,  "tablePageSize.25"),
	SIZE_50   (50,  "tablePageSize.50"),
	SIZE_100  (100, "tablePageSize.100"),
	SIZE_250  (250, "tablePageSize.250"),
	SIZE_500  (500, "tablePageSize.500");

	@Getter
    private final int value;

	@Getter
	private final @NonNull String messageName;

	private TablePageSize(final int value, final @NonNull String messageName) {
		this.value = value;
		this.messageName = messageName;
	}

	public static TablePageSize findByValue(final Integer value) {
		if(value == null) {
			return null;
		}

		final int intValue = value.intValue();
		final TablePageSize tablePageSize = Constants.PARALLEL_STREAM_UTILS.ofEnum(TablePageSize.class)
			.filter(tablePageSizeAux -> tablePageSizeAux.value == intValue)
			.findFirst()
			.orElse(null);
		return tablePageSize;
	}

	public static @NonNull TablePageSize[] getSpecificEnumElements() {
		final TablePageSize[] enumElementsWithoutByDefault = Constants.ENUM_UTILS.getSpecificEnumElements(TablePageSize.class);
		return enumElementsWithoutByDefault;
	}

	@Override
	public @NonNull Class<TablePageSize> getEnumClass() {
		return TablePageSize.class;
	}

	@Override
	public ConfigurableEnum<TablePageSize> getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final TablePageSize tablePageSize = configPropertiesBean.getDefaultInitialTablePageSizeOverwritten();
		return tablePageSize;
	}

	@Override
	public ConfigurableEnum<TablePageSize> getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final TablePageSize tablePageSize = configPropertiesBean.getDefaultInitialTablePageSize();
		return tablePageSize;
	}

	@Override
	public @NonNull ConfigurableEnum<TablePageSize> getFinalDefaultEnumElement() {
		return TablePageSize.SIZE_5;
	}
}

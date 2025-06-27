package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;

public enum TablePageSize implements ConfigurableEnum<TablePageSize> {
	BY_DEFAULT(0,   "tablePageSize.byDefault"),
	SIZE_5    (5,   "tablePageSize.5"),
	SIZE_10   (10,  "tablePageSize.10"),
	SIZE_25   (25,  "tablePageSize.25"),
	SIZE_50   (50,  "tablePageSize.50"),
	SIZE_100  (100, "tablePageSize.100"),
	SIZE_250  (250, "tablePageSize.250"),
	SIZE_500  (500, "tablePageSize.500");

	private final int value;

	@NotNull
	private final String messageName;

	private TablePageSize(final int value, final String messageName) {
		this.value = value;
		this.messageName = messageName;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String getMessageName() {
		return messageName;
	}

	public static TablePageSize findByValue(final Integer value) {
		final TablePageSize tablePageSize;
		if(value != null) {
			tablePageSize = Constants.PARALLEL_STREAM_UTILS.ofEnum(TablePageSize.class)
				.filter(tablePageSizeAux -> tablePageSizeAux.value == value.intValue())
				.findFirst()
				.orElse(null);
		} else {
			tablePageSize = null;
		}

		return tablePageSize;
	}

	public static TablePageSize[] valuesWithoutByDefault() {
//		final TablePageSize[] values = TablePageSize.values();
//		final TablePageSize[] values = Constants.PARALLEL_STREAM_UTILS.ofEnum(TablePageSize.class)
//			.toArray(TablePageSize[]::new);
//
//		final TablePageSize[] valuesWithoutByDefault = new TablePageSize[values.length - 1];
//		for(int i = 0; i < valuesWithoutByDefault.length; i++) {
//			valuesWithoutByDefault[i] = values[i + 1];
//		}

		final List<TablePageSize> valuesWithoutByDefaultList = Constants.ENUM_UTILS.getSpecificEnumElements(TablePageSize.class);
		final TablePageSize[] valuesWithoutByDefault = valuesWithoutByDefaultList.toArray(new TablePageSize[valuesWithoutByDefaultList.size()]);
		return valuesWithoutByDefault;
	}

	@Override
	public ConfigurableEnum<TablePageSize> getOverwrittenEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final TablePageSize tablePageSize = configPropertiesBean.getDefaultInitialTablePageSizeOverwritten();
		return tablePageSize;
	}

	@Override
	public ConfigurableEnum<TablePageSize> getOverwritableEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final TablePageSize tablePageSize = configPropertiesBean.getDefaultInitialTablePageSize();
		return tablePageSize;
	}

	@Override
	public ConfigurableEnum<TablePageSize> getFinalDefaultEnumElement() {
		return TablePageSize.SIZE_5;
	}
}

package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

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
	@NotNull
	private final String messageName;

	private TablePageSize(final int value, final String messageName) {
		this.value = value;
		this.messageName = messageName;
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

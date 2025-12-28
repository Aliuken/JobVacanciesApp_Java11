package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Sort;

public enum TableSortingDirection implements ConfigurableEnum<TableSortingDirection> {
	BY_DEFAULT("---",  "tableSortingDirection.byDefault", null),
	ASC       ("asc",  "tableSortingDirection.asc",       Sort.Direction.ASC),
	DESC      ("desc", "tableSortingDirection.desc",      Sort.Direction.DESC);

	@Getter
	private final @NonNull String code;

	@Getter
	private final @NonNull String messageName;

	@Getter
	private final Sort.Direction sortDirection;

	private TableSortingDirection(final @NonNull String code, final @NonNull String messageName, final Sort.Direction sortDirection) {
		this.code = code;
		this.messageName = messageName;
		this.sortDirection = sortDirection;
	}

	public static TableSortingDirection findByCode(final String code) {
		if(LogicalUtils.isNullOrEmptyString(code)) {
			return null;
		}

		final TableSortingDirection tableSortingDirection = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableSortingDirection.class)
			.filter(value -> value.code.equals(code))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("TableSortingDirection code does not exist"));

		return tableSortingDirection;
	}

	public static @NonNull TableSortingDirection[] getSpecificEnumElements() {
		final TableSortingDirection[] enumElementsWithoutByDefault = Constants.ENUM_UTILS.getSpecificEnumElements(TableSortingDirection.class);
		return enumElementsWithoutByDefault;
	}

	@Override
	public @NonNull Class<TableSortingDirection> getEnumClass() {
		return TableSortingDirection.class;
	}

	@Override
	public ConfigurableEnum<TableSortingDirection> getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final TableSortingDirection tableSortingDirection = configPropertiesBean.getDefaultInitialTableSortingDirectionOverwritten();
		return tableSortingDirection;
	}

	@Override
	public ConfigurableEnum<TableSortingDirection> getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final TableSortingDirection tableSortingDirection = configPropertiesBean.getDefaultInitialTableSortingDirection();
		return tableSortingDirection;
	}

	@Override
	public @NonNull ConfigurableEnum<TableSortingDirection> getFinalDefaultEnumElement() {
		return TableSortingDirection.ASC;
	}
}

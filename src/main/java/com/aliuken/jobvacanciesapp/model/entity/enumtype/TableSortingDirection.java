package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.util.List;

public enum TableSortingDirection implements ConfigurableEnum<TableSortingDirection> {
	BY_DEFAULT("---",  "tableSortingDirection.byDefault", null),
	ASC       ("asc",  "tableSortingDirection.asc",       Sort.Direction.ASC),
	DESC      ("desc", "tableSortingDirection.desc",      Sort.Direction.DESC);

	@Getter
    @NotNull
	private final String code;

	@Getter
	@NotNull
	private final String messageName;

	@Getter
	private final Sort.Direction sortDirection;

	private TableSortingDirection(final String code, final String messageName, final Sort.Direction sortDirection) {
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

	public static TableSortingDirection[] valuesWithoutByDefault() {
		final List<TableSortingDirection> valuesWithoutByDefaultList = Constants.ENUM_UTILS.getSpecificEnumElements(TableSortingDirection.class);
		final TableSortingDirection[] valuesWithoutByDefault = valuesWithoutByDefaultList.toArray(new TableSortingDirection[valuesWithoutByDefaultList.size()]);
		return valuesWithoutByDefault;
	}

	@Override
	public ConfigurableEnum<TableSortingDirection> getOverwrittenEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final TableSortingDirection tableSortingDirection = configPropertiesBean.getDefaultInitialTableSortingDirectionOverwritten();
		return tableSortingDirection;
	}

	@Override
	public ConfigurableEnum<TableSortingDirection> getOverwritableEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final TableSortingDirection tableSortingDirection = configPropertiesBean.getDefaultInitialTableSortingDirection();
		return tableSortingDirection;
	}

	@Override
	public ConfigurableEnum<TableSortingDirection> getFinalDefaultEnumElement() {
		return TableSortingDirection.ASC;
	}
}

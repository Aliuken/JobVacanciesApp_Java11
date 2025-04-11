package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Sort;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.superinterface.Internationalizable;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;

public enum TableSortingDirection implements Serializable, Internationalizable {
	ASC("asc", "tableSortingDirection.asc", Sort.Direction.ASC),
	DESC("desc", "tableSortingDirection.desc", Sort.Direction.DESC);

	@NotNull
	private final String code;

	@NotNull
	private final String messageName;

	@NotNull
	private final Sort.Direction sortDirection;

	private TableSortingDirection(final String code, final String messageName, final Sort.Direction sortDirection) {
		this.code = code;
		this.messageName = messageName;
		this.sortDirection = sortDirection;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessageName() {
		return messageName;
	}

	public Sort.Direction getSortDirection() {
		return sortDirection;
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
}

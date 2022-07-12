package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;
import java.util.EnumSet;

import javax.validation.constraints.NotNull;

public enum TablePageSize implements Serializable {
	SIZE_5("5"),
	SIZE_10("10"),
	SIZE_25("25"),
	SIZE_50("50"),
	SIZE_100("100");

    @NotNull
	private final String code;

	private TablePageSize(final String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static TablePageSize findByCode(final String code) {
		if(code == null || code.isEmpty()) {
			return null;
		}

		final TablePageSize tablePageSize = EnumSet.allOf(TablePageSize.class).stream().parallel()
				.filter(value -> value.code.equals(code))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("TablePageSize code does not exist"));

		return tablePageSize;
	}

}

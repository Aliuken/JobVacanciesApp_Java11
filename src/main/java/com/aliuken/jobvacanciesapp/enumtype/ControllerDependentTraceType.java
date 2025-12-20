package com.aliuken.jobvacanciesapp.enumtype;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public enum ControllerDependentTraceType implements Serializable {
	DATABASE_TRACE                    ("DB "),
	ENTITY_MANAGER_CACHE_INPUT_TRACE  (">> "),
	ENTITY_MANAGER_CACHE_OUTPUT_TRACE ("<< "),
	ENTITY_MANAGER_CACHE_SUMMARY_TRACE("-- ");

	@Getter
	@NotNull
	private final String traceInsideController;

	@Getter
	@NotNull
	private final String traceOutsideController;

	private ControllerDependentTraceType(@NotNull final String initialTrace) {
		this.traceInsideController = StringUtils.getStringJoined("  ", initialTrace);
		this.traceOutsideController = StringUtils.getStringJoined(initialTrace, "  ");
	}
}

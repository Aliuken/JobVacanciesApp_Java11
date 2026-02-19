package com.aliuken.jobvacanciesapp.enumtype;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;

public enum ControllerDependentTraceType implements Serializable {
	DATABASE_TRACE                    ("DB "),
	ENTITY_MANAGER_CACHE_INPUT_TRACE  (">> "),
	ENTITY_MANAGER_CACHE_OUTPUT_TRACE ("<< "),
	ENTITY_MANAGER_CACHE_SUMMARY_TRACE("-- ");

	@Getter
	private final @NonNull String traceInsideController;

	@Getter
	private final @NonNull String traceOutsideController;

	private ControllerDependentTraceType(final @NonNull String initialTrace) {
		this.traceInsideController = StringUtils.getStringJoined("  ", initialTrace);
		this.traceOutsideController = StringUtils.getStringJoined(initialTrace, "  ");
	}
}

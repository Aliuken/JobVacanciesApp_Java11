package com.aliuken.jobvacanciesapp.model;

import java.io.Serializable;
import java.util.EnumSet;

import javax.validation.constraints.NotNull;

public enum JobVacancyStatus implements Serializable {
	CREATED("C", "jobVacancyStatus.created"),
	APPROVED("A", "jobVacancyStatus.approved"),
	DELETED("D", "jobVacancyStatus.deleted");

	@NotNull
	private final String code;

	@NotNull
	private final String messageName;

	private JobVacancyStatus(final String code, @NotNull final String messageName) {
		this.code = code;
		this.messageName = messageName;
	}

	public String getCode() {
		return code;
	}

	public String getMessageName() {
		return messageName;
	}

	public static JobVacancyStatus findByCode(final String code) {
		if(code == null || code.isEmpty()) {
			return null;
		}

		final JobVacancyStatus jobVacancyStatus = EnumSet.allOf(JobVacancyStatus.class).stream().parallel()
				.filter(value -> value.code.equals(code))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("JobVacancyStatus code does not exist"));

		return jobVacancyStatus;
	}

}

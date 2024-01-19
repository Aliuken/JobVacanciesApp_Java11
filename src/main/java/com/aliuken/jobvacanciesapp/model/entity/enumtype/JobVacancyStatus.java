package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;

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
		if(LogicalUtils.isNullOrEmpty(code)) {
			return null;
		}

		final JobVacancyStatus jobVacancyStatus = Constants.PARALLEL_STREAM_UTILS.ofEnum(JobVacancyStatus.class)
			.filter(value -> value.code.equals(code))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("JobVacancyStatus code does not exist"));

		return jobVacancyStatus;
	}

}

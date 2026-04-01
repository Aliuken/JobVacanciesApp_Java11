package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.superinterface.Internationalizable;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamStaticUtils;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;

public enum JobVacancyStatus implements Serializable, Internationalizable {
	CREATED ("C", "jobVacancyStatus.created"),
	APPROVED("A", "jobVacancyStatus.approved"),
	DELETED ("D", "jobVacancyStatus.deleted");

	@Getter
	private final @NonNull String code;

	@Getter
	private final @NonNull String messageName;

	private JobVacancyStatus(final @NonNull String code, final @NonNull String messageName) {
		this.code = code;
		this.messageName = messageName;
	}

	public static JobVacancyStatus findByCode(final String code) {
		if(LogicalUtils.isNullOrEmptyString(code)) {
			return null;
		}

		final JobVacancyStatus jobVacancyStatus = StreamStaticUtils.ofEnum(JobVacancyStatus.class, false)
			.filter(value -> value.code.equals(code))
			.findFirst()
			.orElse(null);
		return jobVacancyStatus;
	}
}

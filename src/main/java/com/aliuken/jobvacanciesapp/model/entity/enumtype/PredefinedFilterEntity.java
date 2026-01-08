package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public enum PredefinedFilterEntity implements Serializable {
	AUTH_USER   ("AuthUser"),
	JOB_CATEGORY("JobCategory"),
	JOB_COMPANY ("JobCompany"),
	JOB_VACANCY ("JobVacancy");

	@Getter
	@NotNull
	private final String upperCasedEntityName;

	@Getter
	@NotNull
	private final String lowerCasedEntityName;

	private PredefinedFilterEntity(@NotNull final String upperCasedEntityName) {
		this.upperCasedEntityName = upperCasedEntityName;
		this.lowerCasedEntityName = StringUtils.lowerCaseFirstCharacter(upperCasedEntityName);
	}

	public static String getLowerCasedEntityNameByEntityName(final String entityName) {
		final PredefinedFilterEntity predefinedFilterEntity = PredefinedFilterEntity.findByEntityName(entityName);

		final String result;
		if(predefinedFilterEntity != null) {
			result = predefinedFilterEntity.lowerCasedEntityName;
		} else {
			result = null;
		}
		return result;
	}

	public static PredefinedFilterEntity findByEntityName(final String entityName) {
		if(LogicalUtils.isNullOrEmptyString(entityName)) {
			return null;
		}

		final PredefinedFilterEntity predefinedFilterEntity = Constants.PARALLEL_STREAM_UTILS.ofEnum(PredefinedFilterEntity.class)
			.filter(predefinedFilterEntityAux -> entityName.equals(predefinedFilterEntityAux.upperCasedEntityName))
			.findFirst()
			.orElse(null);
		return predefinedFilterEntity;
	}
}

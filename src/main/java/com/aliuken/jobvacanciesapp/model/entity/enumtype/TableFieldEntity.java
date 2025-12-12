package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public enum TableFieldEntity implements Serializable {
	AUTH_USER   ("AuthUser"),
	JOB_COMPANY ("JobCompany");

	@Getter
	@NotNull
	private final String upperCasedEntityName;

	@Getter
    @NotNull
	private final String lowerCasedEntityName;

	private TableFieldEntity(@NotNull final String upperCasedEntityName) {
		this.upperCasedEntityName = upperCasedEntityName;
		this.lowerCasedEntityName = StringUtils.lowerCaseFirstCharacter(upperCasedEntityName);
	}

    public static String getLowerCasedEntityNameByEntityName(final String entityName) {
		final TableFieldEntity tableFieldEntity = TableFieldEntity.findByEntityName(entityName);

		final String result;
		if(tableFieldEntity != null) {
			result = tableFieldEntity.lowerCasedEntityName;
		} else {
			result = null;
		}
		return result;
	}

	public static TableFieldEntity findByEntityName(final String entityName) {
		final TableFieldEntity tableFieldEntity;
		if(entityName != null) {
			tableFieldEntity = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableFieldEntity.class)
				.filter(tableFieldEntityAux -> entityName.equals(tableFieldEntityAux.upperCasedEntityName))
				.findFirst()
				.orElse(null);
		} else {
			tableFieldEntity = null;
		}
		return tableFieldEntity;
	}
}

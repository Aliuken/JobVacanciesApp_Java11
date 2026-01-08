package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.PredefinedFilterEntity;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;

@Data
public class PredefinedFilterDTO implements Serializable {
	private static final long serialVersionUID = -4084683709653433040L;

	private final String predefinedFilterName;
	private final String predefinedFilterValue;

	public PredefinedFilterDTO(final String predefinedFilterName, final String predefinedFilterValue) {
		this.predefinedFilterName = predefinedFilterName;
		this.predefinedFilterValue = predefinedFilterValue;
	}

	public PredefinedFilterEntity getPredefinedFilterEntity() {
		final PredefinedFilterEntity predefinedFilterEntity = PredefinedFilterEntity.findByEntityName(predefinedFilterName);
		return predefinedFilterEntity;
	}

	@Override
	public @NonNull String toString() {
		final PredefinedFilterEntity predefinedFilterEntity = this.getPredefinedFilterEntity();
		final String predefinedFilterEntityName = String.valueOf(predefinedFilterEntity);

		final String result = StringUtils.getStringJoined("PredefinedFilterDTO [",
			", predefinedFilterName=", predefinedFilterName, ", predefinedFilterEntityName=", predefinedFilterEntityName, ", predefinedFilterValue=", predefinedFilterValue,
			"]");
		return result;
	}
}

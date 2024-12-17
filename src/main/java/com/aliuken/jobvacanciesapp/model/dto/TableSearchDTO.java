package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PredefinedFilterEntity;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

import lombok.Data;

@Data
public class TableSearchDTO implements Serializable {
	private static final long serialVersionUID = -4084683709653433040L;

	private static final TableSearchDTO NO_ARGS_INSTANCE = new TableSearchDTO(null, null, null, null, null, null, null, null);

	@NotEmpty(message="{language.notEmpty}")
	@Size(min=2, max=2, message="{language.minAndMaxSize}")
	private final String languageParam;

	private final String predefinedFilterName;

	private final String predefinedFilterValue;

	@NotNull(message="{filterName.notEmpty}")
	private final String filterName;

	@NotNull(message="{filterValue.notEmpty}")
	private final String filterValue;

	@NotEmpty(message="{tableSortingCode.notEmpty}")
	private final String tableSortingCode;

	@NotNull(message="{pageSize.notEmpty}")
	private final Integer pageSize;

	@NotNull(message="{pageNumber.notEmpty}")
	private final Integer pageNumber;

	public TableSearchDTO(final String languageParam, final String predefinedFilterName, final String predefinedFilterValue, final String filterName, final String filterValue, final String tableSortingCode, final Integer pageSize, final Integer pageNumber) {
		if(languageParam != null) {
			this.languageParam = languageParam;
		} else {
			this.languageParam = Language.ENGLISH.getCode();
		}

		this.predefinedFilterName = predefinedFilterName;
		this.predefinedFilterValue = predefinedFilterValue;
		this.filterName = filterName;
		this.filterValue = filterValue;
		this.tableSortingCode = tableSortingCode;
		this.pageSize = pageSize;

		if(pageNumber != null) {
			this.pageNumber = pageNumber;
		} else {
			this.pageNumber = 0;
		}
	}

	public static TableSearchDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	public PredefinedFilterEntity getPredefinedFilterEntity() {
		final PredefinedFilterEntity predefinedFilterEntity = PredefinedFilterEntity.findByEntityName(predefinedFilterName);
		return predefinedFilterEntity;
	}

	//If not all pagination URL parameters -> empty table (in Java)
	public boolean hasAllParameters() {
		final boolean hasAllParameters = (LogicalUtils.isNotNullNorEmptyString(languageParam) && !Language.BY_DEFAULT.getCode().equals(languageParam) && filterName != null && filterValue != null && LogicalUtils.isNotNullNorEmptyString(tableSortingCode) && pageSize != null && pageNumber != null);
		return hasAllParameters;
	}

	@Override
	public String toString() {
		final PredefinedFilterEntity predefinedFilterEntity = this.getPredefinedFilterEntity();
		final String predefinedFilterEntityName = Objects.toString(predefinedFilterEntity);
		final String pageSizeString = Objects.toString(pageSize);
		final String pageNumberString = Objects.toString(pageNumber);
		final String result = StringUtils.getStringJoined("TableSearchDTO [languageParam=", languageParam, ", predefinedFilterName=", predefinedFilterName, ", predefinedFilterEntityName=", predefinedFilterEntityName, ", predefinedFilterValue=", predefinedFilterValue, ", filterName=", filterName, ", filterValue=", filterValue, ", tableSortingCode=", tableSortingCode, ", pageSize=", pageSizeString, ", pageNumber=", pageNumberString, "]");
		return result;
	}
}

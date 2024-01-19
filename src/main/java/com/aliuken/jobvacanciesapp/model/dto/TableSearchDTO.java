package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

import lombok.Data;

@Data
public class TableSearchDTO implements Serializable {
	private static final long serialVersionUID = -4084683709653433040L;

	private static final TableSearchDTO NO_ARGS_INSTANCE = new TableSearchDTO(null, null, null, null, null, null);

	@NotEmpty(message="{language.notEmpty}")
	@Size(min=2, max=2, message="{language.minAndMaxSize}")
	private final String languageParam;

	@NotNull(message="{tableFieldCode.notEmpty}")
	private final String tableFieldCode;

	@NotNull(message="{tableFieldValue.notEmpty}")
	private final String tableFieldValue;

	@NotEmpty(message="{tableOrderCode.notEmpty}")
	private final String tableOrderCode;

	@NotEmpty(message="{pageSize.notEmpty}")
	private final String pageSize;

	@NotEmpty(message="{pageNumber.notEmpty}")
	private final String pageNumber;

	public TableSearchDTO(final String languageParam, final String tableFieldCode, final String tableFieldValue, final String tableOrderCode, final String pageSize, final String pageNumber) {
		if(languageParam != null) {
			this.languageParam = languageParam;
		} else {
			this.languageParam = Language.ENGLISH.getCode();
		}

		this.tableFieldCode = tableFieldCode;
		this.tableFieldValue = tableFieldValue;
		this.tableOrderCode = tableOrderCode;
		this.pageSize = pageSize;

		if(pageNumber != null) {
			this.pageNumber = pageNumber;
		} else {
			this.pageNumber = String.valueOf(0);
		}
	}

	public static TableSearchDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	public boolean hasEmptyAttribute() {
		final boolean hasEmptyAttribute = (LogicalUtils.isNullOrEmpty(languageParam) || tableFieldCode == null || tableFieldValue == null || LogicalUtils.isNullOrEmpty(tableOrderCode) || LogicalUtils.isNullOrEmpty(pageSize) || LogicalUtils.isNullOrEmpty(pageNumber));
		return hasEmptyAttribute;
	}

	@Override
	public String toString() {
		final String result = StringUtils.getStringJoined("TableSearchDTO [languageParam=", languageParam, ", tableFieldCode=", tableFieldCode, ", tableFieldValue=", tableFieldValue, ", tableOrderCode=", tableOrderCode, ", pageSize=", pageSize, ", pageNumber=", pageNumber, "]");
		return result;
	}

}

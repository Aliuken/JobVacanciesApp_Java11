package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;

import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Data
public class TableSearchDTO implements Serializable {

	private static final long serialVersionUID = -4084683709653433040L;

	private String tableFieldCode;
	private String tableFieldValue;
	private String tableOrderCode;
	private String size;

	public TableSearchDTO() {
		super();
	}

	@Override
	public String toString() {
		final String result = StringUtils.getStringJoined("TableSearchDTO [tableFieldCode=", tableFieldCode, ", tableFieldValue=", tableFieldValue, ", tableOrderCode=", tableOrderCode, ", size=", size, "]");

		return result;
	}

}

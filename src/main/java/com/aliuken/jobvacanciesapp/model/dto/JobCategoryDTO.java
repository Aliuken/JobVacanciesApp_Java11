package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;

import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Data
public class JobCategoryDTO implements Serializable {

	private static final long serialVersionUID = 6033880602495154721L;

	private Long id;

	public JobCategoryDTO() {
		super();
	}

	public JobCategoryDTO(JobCategory jobCategory) {
		if(jobCategory != null) {
			this.id = jobCategory.getId();
		}
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(id);

		final String result = StringUtils.getStringJoined("JobCategoryDTO [id=", idString, "]");

		return result;
	}

}

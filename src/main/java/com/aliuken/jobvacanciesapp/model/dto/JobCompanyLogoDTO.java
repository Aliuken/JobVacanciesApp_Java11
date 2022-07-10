package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;

import com.aliuken.jobvacanciesapp.model.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Data
public class JobCompanyLogoDTO implements Serializable {

	private static final long serialVersionUID = 4433301684411887321L;

	private Long id;
	private String fileName;
	private String filePath;
	private String selectionName;

	public JobCompanyLogoDTO() {
		super();
	}

	public JobCompanyLogoDTO(JobCompanyLogo jobCompanyLogo) {
		if(jobCompanyLogo != null) {
			this.id = jobCompanyLogo.getId();
			this.fileName = jobCompanyLogo.getFileName();
			this.filePath = jobCompanyLogo.getFilePath();
			this.selectionName = jobCompanyLogo.getSelectionName();
		}
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(id);

		final String result = StringUtils.getStringJoined("JobCompanyLogoDTO [id=", idString, ", fileName=", fileName, ", filePath=", filePath, ", selectionName=", selectionName, "]");

		return result;
	}

}

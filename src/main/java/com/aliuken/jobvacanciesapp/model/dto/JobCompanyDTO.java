package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.model.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.StreamUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Data
public class JobCompanyDTO implements Serializable {

	private static final long serialVersionUID = 5328929981541165382L;

	private Long id;
	private String name;
	private String description;
	private boolean isSelectedLogo;
	private Long selectedLogoId;
	private String selectedLogoFilePath;
	private LocalDateTime firstRegistrationDateTime;
	private String firstRegistrationAuthUserEmail;
	private Set<JobCompanyLogoDTO> jobCompanyLogos;

	public JobCompanyDTO() {
		super();
		this.isSelectedLogo = false;
		this.selectedLogoId = null;
		this.selectedLogoFilePath = Constants.NO_SELECTED_LOGO_FILE_PATH;
	}

	public JobCompanyDTO(JobCompany jobCompany) {
		if(jobCompany != null) {
			this.id = jobCompany.getId();
			this.name = jobCompany.getName();
			this.description = jobCompany.getDescription();
			
			JobCompanyLogo selectedJobCompanyLogo = jobCompany.getSelectedJobCompanyLogo();
			if(selectedJobCompanyLogo != null) {
				this.isSelectedLogo = true;
				this.selectedLogoId = selectedJobCompanyLogo.getId();
				this.selectedLogoFilePath = selectedJobCompanyLogo.getFilePath();
			} else {
				this.isSelectedLogo = false;
				this.selectedLogoId = Constants.NO_SELECTED_LOGO_ID;
				this.selectedLogoFilePath = Constants.NO_SELECTED_LOGO_FILE_PATH;
			}
			
			this.firstRegistrationDateTime = jobCompany.getFirstRegistrationDateTime();
			this.firstRegistrationAuthUserEmail = jobCompany.getFirstRegistrationAuthUserEmail();
			
			Set<JobCompanyLogo> jobCompanyLogos = jobCompany.getJobCompanyLogos();
			if(jobCompanyLogos != null) {
				this.jobCompanyLogos = new LinkedHashSet<>();
				for(JobCompanyLogo jobCompanyLogo : jobCompanyLogos) {
					JobCompanyLogoDTO jobCompanyDTOLogo = new JobCompanyLogoDTO(jobCompanyLogo);
					this.jobCompanyLogos.add(jobCompanyDTOLogo);
				}
			} else {
				this.jobCompanyLogos = null;
			}
		} else {
			this.isSelectedLogo = false;
			this.selectedLogoId = Constants.NO_SELECTED_LOGO_ID;
			this.selectedLogoFilePath = Constants.NO_SELECTED_LOGO_FILE_PATH;
		}
	}

	public List<String> getJobCompanyLogoFilePaths() {
		final List<String> jobCompanyLogoFilePaths = StreamUtils.ofNullableCollectionParallel(jobCompanyLogos)
				.map(jcl -> jcl.getFilePath())
				.collect(Collectors.toList());

		return jobCompanyLogoFilePaths;
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(id);
		final String isSelectedLogoString = String.valueOf(isSelectedLogo);
		final String selectedLogoIdString = String.valueOf(selectedLogoId);
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(firstRegistrationDateTime);
		final String jobCompanyLogoFilePaths = this.getJobCompanyLogoFilePaths().toString();

		final String result = StringUtils.getStringJoined("JobCompanyDTO [id=", idString, ", name=", name, ", description=", description, ", isSelectedLogo=", isSelectedLogoString, ", selectedLogoId=", selectedLogoIdString, ", selectedLogoFilePath=", selectedLogoFilePath, 
				", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUserEmail=", firstRegistrationAuthUserEmail, ", jobCompanyLogos=", jobCompanyLogoFilePaths, "]");

		return result;
	}

}

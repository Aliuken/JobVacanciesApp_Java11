package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Data
public class JobVacancyDTO implements Serializable {

	private static final long serialVersionUID = -3670738813828834458L;
	
	private Long id;
	private String name;
	private String description;
	private JobCategoryDTO jobCategory;
	private JobCompanyDTO jobCompany;
	private String status;
	private LocalDateTime publicationDateTime;
	private BigDecimal salary;
	private Boolean highlighted;
	private String details;
	private LocalDateTime firstRegistrationDateTime;
	private String firstRegistrationAuthUserEmail;

	public JobVacancyDTO() {
		super();
	}

	public JobVacancyDTO(JobVacancy jobVacancy) {
		if(jobVacancy != null) {
			this.id = jobVacancy.getId();
			this.name = jobVacancy.getName();
			this.description = jobVacancy.getDescription();
			
			JobCategory jobCategory = jobVacancy.getJobCategory();
			this.jobCategory = new JobCategoryDTO(jobCategory);
			
			JobCompany jobCompany = jobVacancy.getJobCompany();
			this.jobCompany = new JobCompanyDTO(jobCompany);
			
			this.status = jobVacancy.getStatus().name();
			this.publicationDateTime = jobVacancy.getPublicationDateTime();
			this.salary = jobVacancy.getSalary();
			this.highlighted = jobVacancy.getHighlighted();
			this.details = jobVacancy.getDetails();
			this.firstRegistrationDateTime = jobCompany.getFirstRegistrationDateTime();
			this.firstRegistrationAuthUserEmail = jobCompany.getFirstRegistrationAuthUserEmail();
		}
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(id);
		final String jobCategoryIdString = (jobCategory != null) ? String.valueOf(jobCategory.getId()) : null;
		final String jobCompanyIdString = (jobCompany != null) ? String.valueOf(jobCompany.getId()) : null;
		final String publicationDateTimeString = DateTimeUtils.convertToString(publicationDateTime);
		final String salaryString = (salary != null) ? salary.toString() : null;
		final String highlightedString = highlighted.toString();
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(firstRegistrationDateTime);

		final String result = StringUtils.getStringJoined("JobVacancyDTO [id=", idString, ", name=", name, ", description=", description, 
				", jobCategory=", jobCategoryIdString, ", jobCompany=", jobCompanyIdString, 
				", status=", status, ", publicationDateTime=", publicationDateTimeString, 
				", salary=", salaryString, ", highlighted=", highlightedString, ", details=", details, 
				", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUserEmail=", firstRegistrationAuthUserEmail, "]");

		return result;
	}

}

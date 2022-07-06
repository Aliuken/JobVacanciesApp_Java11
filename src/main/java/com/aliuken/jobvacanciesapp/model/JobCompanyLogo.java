package com.aliuken.jobvacanciesapp.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Entity
@Table(name="job_company_logo", indexes={
		@Index(name="job_company_logo_unique_key_1", columnList="job_company_id, file_name", unique=true),
		@Index(name="job_company_logo_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="job_company_logo_key_2", columnList="last_modification_auth_user_id"),
		@Index(name="job_company_logo_key_3", columnList="job_company_id")})
@Data
public class JobCompanyLogo extends AbstractEntity {

	private static final long serialVersionUID = 3937298767687586305L;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="job_company_id", nullable=false)
	private JobCompany jobCompany;

	@NotNull
    @Size(max=255)
    @Column(name="file_name", length=255, nullable=false)
	private String fileName;

	public JobCompanyLogo() {
		super();
	}

	public String getFilePath() {
		final String jobCompanyIdString;
		if(jobCompany != null) {
			Long jobCompanyId = jobCompany.getId();
			jobCompanyIdString = (jobCompanyId != null) ? jobCompanyId.toString() : "temp";
		} else {
			jobCompanyIdString = "temp";
		}
		
		final String filePath = StringUtils.getStringJoined(jobCompanyIdString, "/", fileName);
		return filePath;
	}
	
	public String getSelectionName() {
		final String idString = String.valueOf(this.getId());
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(this.getFirstRegistrationDateTime());
		final String selectionName = StringUtils.getStringJoined("Logo ", idString, " ", firstRegistrationDateTimeString);
		return selectionName;
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(this.getId());
		final String jobCompanyName = jobCompany.getName();
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(this.getFirstRegistrationDateTime());
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = DateTimeUtils.convertToString(this.getLastModificationDateTime());
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("JobCompanyLogo [id=", idString, ", jobCompany=", jobCompanyName, ", fileName=", fileName, 
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(jobCompany, fileName);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		JobCompanyLogo other = (JobCompanyLogo) obj;
		return Objects.equals(jobCompany, other.jobCompany) && Objects.equals(fileName, other.fileName);
	}

}

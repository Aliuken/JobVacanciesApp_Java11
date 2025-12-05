package com.aliuken.jobvacanciesapp.model.entity;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntityWithJobCompany;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="job_company_logo", indexes={
		@Index(name="job_company_logo_unique_key_1", columnList="job_company_id, file_name", unique=true),
		@Index(name="job_company_logo_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="job_company_logo_key_2", columnList="last_modification_auth_user_id"),
		@Index(name="job_company_logo_key_3", columnList="job_company_id")})
@Getter
@Setter
public class JobCompanyLogo extends AbstractEntityWithJobCompany<JobCompanyLogo> {
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
		final String idString = this.getIdString();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String selectionName = StringUtils.getStringJoined("Logo ", idString, Constants.SPACE, firstRegistrationDateTimeString);
		return selectionName;
	}

	@Override
	public boolean isPrintableEntity() {
		return true;
	}

	@Override
	public String getKeyFields() {
		final String idString = this.getIdString();
		final String jobCompanyIdString = (jobCompany != null) ? jobCompany.getIdString() : null;

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("id: "), idString, Constants.NEWLINE,
			StyleApplier.getBoldString("[jobCompanyId, fileName]: "), "[", jobCompanyIdString, ", ", fileName, "]");
		return result;
	}

	@Override
	public String getAuthUserFields() {
		return Constants.EMPTY_STRING;
	}

	@Override
	public String getOtherFields() {
		final String jobCompanyName = this.getJobCompanyName();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("jobCompanyName: "), jobCompanyName);
		return result;
	}

	@Override
	public String toString() {
		final String idString = this.getIdString();
		final String jobCompanyName = this.getJobCompanyName();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("JobCompanyLogo [id=", idString, ", jobCompany=", jobCompanyName, ", fileName=", fileName,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}
}

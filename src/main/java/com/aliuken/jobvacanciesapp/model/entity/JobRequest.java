package com.aliuken.jobvacanciesapp.model.entity;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntityWithAuthUserAndJobCompany;
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
@Table(name="job_request", indexes={
		@Index(name="job_request_unique_key_1", columnList="auth_user_id,job_vacancy_id", unique=true),
		@Index(name="job_request_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="job_request_key_2", columnList="last_modification_auth_user_id"),
		@Index(name="job_request_key_3", columnList="auth_user_id"),
		@Index(name="job_request_key_4", columnList="job_vacancy_id"),
		@Index(name="job_request_key_5", columnList="auth_user_id,curriculum_file_name")})
@Getter
@Setter
public class JobRequest extends AbstractEntityWithAuthUserAndJobCompany<JobRequest> {
	private static final long serialVersionUID = 8508562505523280587L;

	@NotNull
	@ManyToOne
	@JoinColumn(name="auth_user_id", nullable=false)
	private AuthUser authUser;

	@NotNull
	@ManyToOne
	@JoinColumn(name="job_vacancy_id", nullable=false)
	private JobVacancy jobVacancy;

	@NotNull
	@Size(max=1000)
	@Column(name="comments", length=1000, nullable=false)
	private String comments;

	@NotNull
	@Size(max=255)
	@Column(name="curriculum_file_name", length=255, nullable=false)
	private String curriculumFileName;

	public JobRequest() {
		super();
	}

	@Override
	public JobCompany getJobCompany() {
		final JobCompany jobCompany = (jobVacancy != null) ? jobVacancy.getJobCompany() : null;
		return jobCompany;
	}

	@Override
	public void setJobCompany(JobCompany jobCompany) {
		if(jobVacancy != null) {
			jobVacancy.setJobCompany(jobCompany);
		}
	}

	public String getJobVacancyName() {
		final String jobVacancyName = (jobVacancy != null) ? jobVacancy.getName() : null;
		return jobVacancyName;
	}

	public AuthUserCurriculum getAuthUserCurriculum() {
		final AuthUser authUser = this.getAuthUser();
		if(authUser == null || curriculumFileName == null) {
			return null;
		}

		final AuthUserCurriculum authUserCurriculum = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUser.getAuthUserCurriculums())
				.filter(auc -> curriculumFileName.equals(auc.getFileName()))
				.findFirst()
				.orElse(null);

		return authUserCurriculum;
	}

	public Long getAuthUserCurriculumId() {
		final AuthUser authUser = this.getAuthUser();
		if(authUser == null || curriculumFileName == null) {
			return null;
		}

		final Long authUserCurriculumId = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUser.getAuthUserCurriculums())
				.filter(auc -> curriculumFileName.equals(auc.getFileName()))
				.map(auc -> auc.getId())
				.findFirst()
				.orElse(null);

		return authUserCurriculumId;
	}

	public String getAuthUserCurriculumFilePath() {
		final AuthUser authUser = this.getAuthUser();
		if(authUser == null || curriculumFileName == null) {
			return null;
		}

		final String authUserCurriculumFilePath = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUser.getAuthUserCurriculums())
				.filter(auc -> curriculumFileName.equals(auc.getFileName()))
				.map(auc -> auc.getFilePath())
				.findFirst()
				.orElse(null);

		return authUserCurriculumFilePath;
	}

	public String getAuthUserCurriculumSelectionName() {
		final AuthUser authUser = this.getAuthUser();
		if(authUser == null || curriculumFileName == null) {
			return null;
		}

		final String authUserCurriculumSelectionName = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUser.getAuthUserCurriculums())
				.filter(auc -> curriculumFileName.equals(auc.getFileName()))
				.map(auc -> auc.getSelectionName())
				.findFirst()
				.orElse(null);

		return authUserCurriculumSelectionName;
	}

	@Override
	public boolean isPrintableEntity() {
		return true;
	}

	@Override
	public String getKeyFields() {
		final String idString = this.getIdString();
		final String authUserIdString = this.getAuthUserId();
		final String jobVacancyIdString = (jobVacancy != null) ? jobVacancy.getIdString() : null;

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("id: "), idString, Constants.NEWLINE,
			StyleApplier.getBoldString("[authUserId, jobVacancyId]: "), "[", authUserIdString, ", ", jobVacancyIdString, "]");
		return result;
	}

	@Override
	public String getOtherFields() {
		final String jobVacancyName = this.getJobVacancyName();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("jobVacancyName: "), jobVacancyName, Constants.NEWLINE,
			StyleApplier.getBoldString("comments: "), comments, Constants.NEWLINE,
			StyleApplier.getBoldString("curriculumFileName: "), curriculumFileName);
		return result;
	}

	@Override
	public String toString() {
		final String idString = this.getIdString();
		final String authUserEmail = this.getAuthUserEmail();
		final String jobVacancyName = this.getJobVacancyName();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("JobRequest [id=", idString, ", authUser=", authUserEmail, ", jobVacancy=", jobVacancyName, ", comments=", comments, ", curriculumFileName=", curriculumFileName,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}
}

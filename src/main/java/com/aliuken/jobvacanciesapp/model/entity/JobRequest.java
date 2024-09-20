package com.aliuken.jobvacanciesapp.model.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntityWithAuthUser;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;

import lombok.Data;

@Entity
@Table(name="job_request", indexes={
		@Index(name="job_request_unique_key_1", columnList="auth_user_id,job_vacancy_id", unique=true),
		@Index(name="job_request_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="job_request_key_2", columnList="last_modification_auth_user_id"),
		@Index(name="job_request_key_3", columnList="auth_user_id"),
		@Index(name="job_request_key_4", columnList="job_vacancy_id"),
		@Index(name="job_request_key_5", columnList="auth_user_id,curriculum_file_name")})
@Data
public class JobRequest extends AbstractEntityWithAuthUser {

	private static final long serialVersionUID = 8508562505523280587L;

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
	public String getAuthUserEmail() {
		final String authUserEmail = (this.getAuthUser() != null) ? this.getAuthUser().getEmail() : null;
		return authUserEmail;
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
			StyleApplier.getBoldString("id: "), idString, "\n",
			StyleApplier.getBoldString("[authUserId, jobVacancyId]: "), "[", authUserIdString, ", ", jobVacancyIdString, "]");
		return result;
	}

	@Override
	public String getOtherFields() {
		final String jobVacancyName = this.getJobVacancyName();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("jobVacancyName: "), jobVacancyName, "\n",
			StyleApplier.getBoldString("comments: "), comments, "\n",
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(jobVacancy, comments, curriculumFileName);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		JobRequest other = (JobRequest) obj;
		return Objects.equals(jobVacancy, other.jobVacancy) && Objects.equals(comments, other.comments) && Objects.equals(curriculumFileName, other.curriculumFileName);
	}
}

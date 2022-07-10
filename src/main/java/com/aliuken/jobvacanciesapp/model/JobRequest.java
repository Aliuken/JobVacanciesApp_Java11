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
import com.aliuken.jobvacanciesapp.util.StreamUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Entity
@Table(name="job_request", indexes={
		@Index(name="job_request_unique_key_1", columnList="auth_user_id,job_vacancy_id", unique=true),
		@Index(name="job_request_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="job_request_key_2", columnList="last_modification_auth_user_id"),
		@Index(name="job_request_key_3", columnList="auth_user_id"),
		@Index(name="job_request_key_4", columnList="job_vacancy_id")})
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

	public AuthUserCurriculum getAuthUserCurriculum() {
		final AuthUser authUser = this.getAuthUser();

		final AuthUserCurriculum authUserCurriculum;
		if(authUser != null && curriculumFileName != null) {
			authUserCurriculum = StreamUtils.ofNullableCollectionParallel(authUser.getAuthUserCurriculums())
					.filter(auc -> curriculumFileName.equals(auc.getFileName()))
					.findFirst()
					.orElse(null);
		} else {
			authUserCurriculum = null;
		}

		return authUserCurriculum;
	}

	public Long getAuthUserCurriculumId() {
		final AuthUser authUser = this.getAuthUser();

		final Long authUserCurriculumId;
		if(authUser != null && curriculumFileName != null) {
			authUserCurriculumId = StreamUtils.ofNullableCollectionParallel(authUser.getAuthUserCurriculums())
					.filter(auc -> curriculumFileName.equals(auc.getFileName()))
					.map(auc -> auc.getId())
					.findFirst()
					.orElse(null);
		} else {
			authUserCurriculumId = null;
		}

		return authUserCurriculumId;
	}

	public String getAuthUserCurriculumFilePath() {
		final AuthUser authUser = this.getAuthUser();

		final String authUserCurriculumFilePath;
		if(authUser != null && curriculumFileName != null) {
			authUserCurriculumFilePath = StreamUtils.ofNullableCollectionParallel(authUser.getAuthUserCurriculums())
					.filter(auc -> curriculumFileName.equals(auc.getFileName()))
					.map(auc -> auc.getFilePath())
					.findFirst()
					.orElse(null);
		} else {
			authUserCurriculumFilePath = null;
		}

		return authUserCurriculumFilePath;
	}

	public String getAuthUserCurriculumSelectionName() {
		final AuthUser authUser = this.getAuthUser();

		final String authUserCurriculumSelectionName;
		if(authUser != null && curriculumFileName != null) {
			authUserCurriculumSelectionName = StreamUtils.ofNullableCollectionParallel(authUser.getAuthUserCurriculums())
					.filter(auc -> curriculumFileName.equals(auc.getFileName()))
					.map(auc -> auc.getSelectionName())
					.findFirst()
					.orElse(null);
		} else {
			authUserCurriculumSelectionName = null;
		}

		return authUserCurriculumSelectionName;
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(this.getId());
		final String authUserEmail = (this.getAuthUser() != null) ? this.getAuthUser().getEmail() : null;
		final String jobVacancyName = (jobVacancy != null) ? jobVacancy.getName() : null;
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(this.getFirstRegistrationDateTime());
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = DateTimeUtils.convertToString(this.getLastModificationDateTime());
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
		if (!super.equals(obj)) {
			return false;
		}
		JobRequest other = (JobRequest) obj;
		return Objects.equals(jobVacancy, other.jobVacancy) && Objects.equals(comments, other.comments) && Objects.equals(curriculumFileName, other.curriculumFileName);
	}

}

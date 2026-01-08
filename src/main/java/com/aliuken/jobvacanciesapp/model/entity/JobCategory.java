package com.aliuken.jobvacanciesapp.model.entity;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.annotation.LazyEntityRelationGetter;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.jspecify.annotations.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="job_category", indexes={
		@Index(name="job_category_unique_key_1", columnList="name", unique=true),
		@Index(name="job_category_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="job_category_key_2", columnList="last_modification_auth_user_id")})
@Getter
@Setter
public class JobCategory extends AbstractEntity<JobCategory> {
	private static final long serialVersionUID = -1716013269189038906L;

	@Size(max=35)
	@Column(name="name", length=35, nullable=false)
	private @NonNull String name;

	@Size(max=500)
	@Column(name="description", length=500, nullable=false)
	private @NonNull String description;

	@OneToMany(mappedBy="jobCategory", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("id DESC")
	private @NonNull Set<JobVacancy> jobVacancies = new LinkedHashSet<>();

	public JobCategory() {
		super();
	}

	@LazyEntityRelationGetter
	public @NonNull Set<JobVacancy> getJobVacancies() {
		return jobVacancies;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<Long> getJobVacancyIds() {
		final Set<Long> jobVacancyIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobVacancies)
			.map(jobVacancy -> jobVacancy.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancyIds;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<String> getJobVacancyNames() {
		final Set<String> jobVacancyNames = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobVacancies)
			.map(jobVacancy -> jobVacancy.getName())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancyNames;
	}

	@Override
	public boolean isPrintableEntity() {
		return true;
	}

	@Override
	public @NonNull String getKeyFields() {
		final String idString = this.getIdString();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("id: "), idString, Constants.NEWLINE,
			StyleApplier.getBoldString("name: "), name);
		return result;
	}

	@Override
	public @NonNull String getAuthUserFields() {
		return Constants.EMPTY_STRING;
	}

	@Override
	public @NonNull String getOtherFields() {
		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("description: "), description);
		return result;
	}

	@Override
	public @NonNull String toString() {
		final String idString = this.getIdString();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();
		final String jobVacancyNames = this.getJobVacancyNames().toString();

		final String result = StringUtils.getStringJoined("JobCategory [id=", idString, ", name=", name, ", description=", description,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail,
			", jobVacancies=", jobVacancyNames, "]");

		return result;
	}
}

package com.aliuken.jobvacanciesapp.model;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.StreamUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Entity
@Table(name="job_category", indexes={
		@Index(name="job_category_unique_key_1", columnList="name", unique=true),
		@Index(name="job_category_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="job_category_key_2", columnList="last_modification_auth_user_id")})
@Data
public class JobCategory extends AbstractEntity {

	private static final long serialVersionUID = -1716013269189038906L;

    @NotNull
    @Size(max=35)
    @Column(name="name", length=35, nullable=false)
	private String name;

    @NotNull
	@Size(max=500)
    @Column(name="description", length=500, nullable=false)
	private String description;

	@OneToMany(mappedBy="jobCategory", fetch=FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("id DESC")
	private Set<JobVacancy> jobVacancies;

	public JobCategory() {
		super();
	}

	public Set<Long> getJobVacancyIds() {
		final Set<Long> jobVacancyIds = StreamUtils.ofNullableCollectionParallel(this.getJobVacancies())
				.map(jv -> jv.getId())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancyIds;
	}

	public Set<String> getJobVacancyNames() {
		final Set<String> jobVacancyNames = StreamUtils.ofNullableCollectionParallel(this.getJobVacancies())
				.map(jv -> jv.getName())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancyNames;
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(this.getId());
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(this.getFirstRegistrationDateTime());
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = DateTimeUtils.convertToString(this.getLastModificationDateTime());
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();
		final String jobVacancyNames = this.getJobVacancyNames().toString();

		final String result = StringUtils.getStringJoined("JobCategory [id=", idString, ", name=", name, ", description=", description,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail,
			", jobVacancies=", jobVacancyNames, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(name, description);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		JobCategory other = (JobCategory) obj;
		return Objects.equals(name, other.name) && Objects.equals(description, other.description);
	}

}

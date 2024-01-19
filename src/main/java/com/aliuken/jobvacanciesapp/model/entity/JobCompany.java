package com.aliuken.jobvacanciesapp.model.entity;

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

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.annotation.LazyEntityRelationGetter;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

import lombok.Data;

@Entity
@Table(name="job_company", indexes={
		@Index(name="job_company_unique_key_1", columnList="name", unique=true),
		@Index(name="job_company_unique_key_2", columnList="id,selected_logo_file_name", unique=true),
		@Index(name="job_company_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="job_company_key_2", columnList="last_modification_auth_user_id")})
@Data
public class JobCompany extends AbstractEntity {

	private static final long serialVersionUID = 3938985347825170805L;

	@NotNull
	@Size(max=35)
	@Column(name="name", length=35, nullable=false)
	private String name;

	@NotNull
	@Size(max=500)
	@Column(name="description", length=500, nullable=false)
	private String description;

	@Size(max=255)
	@Column(name="selected_logo_file_name", length=255)
	private String selectedLogoFileName;

	@OneToMany(mappedBy="jobCompany", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("id DESC")
	private Set<JobCompanyLogo> jobCompanyLogos;

	@OneToMany(mappedBy="jobCompany", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("id DESC")
	private Set<JobVacancy> jobVacancies;

	public JobCompany() {
		super();
	}

	@LazyEntityRelationGetter
	public Set<JobCompanyLogo> getJobCompanyLogos() {
		return jobCompanyLogos;
	}

	@LazyEntityRelationGetter
	public Set<JobVacancy> getJobVacancies() {
		return jobVacancies;
	}

	@LazyEntityRelationGetter
	public JobCompanyLogo getSelectedJobCompanyLogo() {
		if(selectedLogoFileName == null) {
			return null;
		}

		final JobCompanyLogo selectedJobCompanyLogo = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobCompanyLogos)
				.filter(jcl -> selectedLogoFileName.equals(jcl.getFileName()))
				.findFirst()
				.orElse(null);

		return selectedJobCompanyLogo;
	}

	@LazyEntityRelationGetter
	public Long getSelectedJobCompanyLogoId() {
		if(selectedLogoFileName == null) {
			return null;
		}

		final Long selectedJobCompanyLogoId = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobCompanyLogos)
				.filter(jcl -> selectedLogoFileName.equals(jcl.getFileName()))
				.map(jcl -> jcl.getId())
				.findFirst()
				.orElse(null);

		return selectedJobCompanyLogoId;
	}

	@LazyEntityRelationGetter
	public String getSelectedJobCompanyLogoFilePath() {
		if(selectedLogoFileName == null) {
			return Constants.NO_SELECTED_LOGO_FILE_PATH;
		}

		final String selectedJobCompanyLogoFilePath = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobCompanyLogos)
				.filter(jcl -> selectedLogoFileName.equals(jcl.getFileName()))
				.map(jcl -> jcl.getFilePath())
				.findFirst()
				.orElse(Constants.NO_SELECTED_LOGO_FILE_PATH);

		return selectedJobCompanyLogoFilePath;
	}

	@LazyEntityRelationGetter
	public String getSelectedJobCompanyLogoSelectionName() {
		if(selectedLogoFileName == null) {
			return null;
		}

		final String selectedJobCompanyLogoSelectionName = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobCompanyLogos)
				.filter(jcl -> selectedLogoFileName.equals(jcl.getFileName()))
				.map(jcl -> jcl.getSelectionName())
				.findFirst()
				.orElse(null);

		return selectedJobCompanyLogoSelectionName;
	}

	@LazyEntityRelationGetter
	public Set<Long> getJobCompanyLogoIds() {
		final Set<Long> jobCompanyLogoIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobCompanyLogos)
				.map(jcl -> jcl.getId())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobCompanyLogoIds;
	}

	@LazyEntityRelationGetter
	public Set<String> getJobCompanyLogoSelectionNames() {
		final Set<String> jobCompanyLogoSelectionNames = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobCompanyLogos)
				.map(jcl -> jcl.getSelectionName())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobCompanyLogoSelectionNames;
	}

	@LazyEntityRelationGetter
	public Set<Long> getJobVacancyIds() {
		final Set<Long> jobVacancyIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobVacancies)
				.map(jv -> jv.getId())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancyIds;
	}

	@LazyEntityRelationGetter
	public Set<String> getJobVacancyNames() {
		final Set<String> jobVacancyNames = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobVacancies)
				.map(jv -> jv.getName())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancyNames;
	}

	@Override
	public String toString() {
		final String idString = Objects.toString(this.getId());
		final String firstRegistrationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(this.getFirstRegistrationDateTime());
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(this.getLastModificationDateTime());
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();
		final String jobCompanyLogoSelectionNames = this.getJobCompanyLogoSelectionNames().toString();
		final String jobVacancyNames = this.getJobVacancyNames().toString();

		final String result = StringUtils.getStringJoined("JobCompany [id=", idString, ", name=", name, ", description=", description, ", selectedLogoFileName=", selectedLogoFileName,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail,
			 ", jobCompanyLogos=", jobCompanyLogoSelectionNames, ", jobVacancies=", jobVacancyNames, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(name, description, selectedLogoFileName);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		JobCompany other = (JobCompany) obj;
		return Objects.equals(name, other.name) && Objects.equals(description, other.description) && Objects.equals(selectedLogoFileName, other.selectedLogoFileName);
	}

}

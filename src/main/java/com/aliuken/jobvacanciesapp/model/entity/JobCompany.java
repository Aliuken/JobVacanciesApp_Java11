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
@Table(name="job_company", indexes={
		@Index(name="job_company_unique_key_1", columnList="name", unique=true),
		@Index(name="job_company_unique_key_2", columnList="id,selected_logo_file_name", unique=true),
		@Index(name="job_company_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="job_company_key_2", columnList="last_modification_auth_user_id")})
@Getter
@Setter
public class JobCompany extends AbstractEntity<JobCompany> {
	private static final long serialVersionUID = 3938985347825170805L;

	@Size(max=35)
	@Column(name="name", length=35, nullable=false)
	private @NonNull String name;

	@Size(max=500)
	@Column(name="description", length=500, nullable=false)
	private @NonNull String description;

	@Size(max=255)
	@Column(name="selected_logo_file_name", length=255)
	private String selectedLogoFileName;

	@OneToMany(mappedBy="jobCompany", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("id DESC")
	private @NonNull Set<JobCompanyLogo> jobCompanyLogos = new LinkedHashSet<>();

	@OneToMany(mappedBy="jobCompany", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("id DESC")
	private @NonNull Set<JobVacancy> jobVacancies = new LinkedHashSet<>();

	public JobCompany() {
		super();
	}

	@LazyEntityRelationGetter
	public @NonNull Set<JobCompanyLogo> getJobCompanyLogos() {
		return jobCompanyLogos;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<JobVacancy> getJobVacancies() {
		return jobVacancies;
	}

	@LazyEntityRelationGetter
	public JobCompanyLogo getSelectedJobCompanyLogo() {
		if(selectedLogoFileName == null) {
			return null;
		}

		final JobCompanyLogo selectedJobCompanyLogo = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobCompanyLogos)
			.filter(jobCompanyLogo -> selectedLogoFileName.equals(jobCompanyLogo.getFileName()))
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
			.filter(jobCompanyLogo -> selectedLogoFileName.equals(jobCompanyLogo.getFileName()))
			.findFirst()
			.map(jobCompanyLogo -> jobCompanyLogo.getId())
			.orElse(null);

		return selectedJobCompanyLogoId;
	}

	@LazyEntityRelationGetter
	public String getSelectedJobCompanyLogoFilePath() {
		if(selectedLogoFileName == null) {
			return Constants.NO_SELECTED_LOGO_FILE_PATH;
		}

		final String selectedJobCompanyLogoFilePath = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobCompanyLogos)
			.filter(jobCompanyLogo -> selectedLogoFileName.equals(jobCompanyLogo.getFileName()))
			.findFirst()
			.map(jobCompanyLogo -> jobCompanyLogo.getFilePath())
			.orElse(Constants.NO_SELECTED_LOGO_FILE_PATH);

		return selectedJobCompanyLogoFilePath;
	}

	@LazyEntityRelationGetter
	public String getSelectedJobCompanyLogoSelectionName() {
		if(selectedLogoFileName == null) {
			return null;
		}

		final String selectedJobCompanyLogoSelectionName = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobCompanyLogos)
			.filter(jobCompanyLogo -> selectedLogoFileName.equals(jobCompanyLogo.getFileName()))
			.findFirst()
			.map(jobCompanyLogo -> jobCompanyLogo.getSelectionName())
			.orElse(null);

		return selectedJobCompanyLogoSelectionName;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<Long> getJobCompanyLogoIds() {
		final Set<Long> jobCompanyLogoIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobCompanyLogos)
			.map(jobCompanyLogo -> jobCompanyLogo.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobCompanyLogoIds;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<String> getJobCompanyLogoSelectionNames() {
		final Set<String> jobCompanyLogoSelectionNames = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobCompanyLogos)
			.map(jobCompanyLogo -> jobCompanyLogo.getSelectionName())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobCompanyLogoSelectionNames;
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
			StyleApplier.getBoldString("name: "), name, Constants.NEWLINE,
			StyleApplier.getBoldString("[id, selectedLogoFileName]: "), "[", idString, ", ", selectedLogoFileName, "]");
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
		final String jobCompanyLogoSelectionNames = this.getJobCompanyLogoSelectionNames().toString();
		final String jobVacancyNames = this.getJobVacancyNames().toString();

		final String result = StringUtils.getStringJoined("JobCompany [id=", idString, ", name=", name, ", description=", description, ", selectedLogoFileName=", selectedLogoFileName,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail,
			 ", jobCompanyLogos=", jobCompanyLogoSelectionNames, ", jobVacancies=", jobVacancyNames, "]");

		return result;
	}
}

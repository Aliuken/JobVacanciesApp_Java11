package com.aliuken.jobvacanciesapp.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SortComparator;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.annotation.LazyEntityRelationGetter;
import com.aliuken.jobvacanciesapp.model.comparator.JobRequestAuthUserFullNameComparator;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;

import lombok.Data;

@Entity
@Table(name="job_vacancy", indexes={
		@Index(name="job_vacancy_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="job_vacancy_key_2", columnList="last_modification_auth_user_id"),
		@Index(name="job_vacancy_key_3", columnList="status"),
		@Index(name="job_vacancy_key_4", columnList="job_company_id"),
		@Index(name="job_vacancy_key_5", columnList="job_category_id")})
@Data
public class JobVacancy extends AbstractEntity {

	private static final long serialVersionUID = 6062234886735475157L;

	@NotNull
	@Size(max=120)
	@Column(name="name", length=120, nullable=false)
	private String name;

	@NotNull
	@Size(max=500)
	@Column(name="description", length=500, nullable=false)
	private String description;

	@Digits(integer=10, fraction=2)
	@Column(name="salary", precision=10, scale=2)
	private BigDecimal salary;

	@NotNull
	@Column(name="status", nullable=false)
	private JobVacancyStatus status;

	@NotNull
	@Column(name="highlighted", length=1, nullable=false)
	private Boolean highlighted;

	@NotNull
	@ManyToOne
	@JoinColumn(name="job_company_id", nullable=false)
	private JobCompany jobCompany;

	@NotNull
	@ManyToOne
	@JoinColumn(name="job_category_id", nullable=false)
	private JobCategory jobCategory;

	@NotNull
	@Column(name="publication_date_time", nullable=false)
	private LocalDateTime publicationDateTime;

	@NotNull
	@Size(max=10000)
	@Column(name="details", length=10000, nullable=false)
	private String details;

	@OneToMany(mappedBy="jobVacancy", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@SortComparator(JobRequestAuthUserFullNameComparator.class)
	private Set<JobRequest> jobRequests;

	public JobVacancy() {
		super();
	}

	public boolean isVerified() {
		boolean isVerified = (JobVacancyStatus.APPROVED == status && Boolean.TRUE.equals(highlighted));
		return isVerified;
	}

	public boolean isVerifiable() {
		boolean isVerifiable = (JobVacancyStatus.DELETED != status && !this.isVerified());
		return isVerifiable;
	}

	public String getSalaryString() {
		final String salaryString = Objects.toString(salary);
		return salaryString;
	}

	public String getStatusName() {
		final String colorModeName = Objects.toString(status);
		return colorModeName;
	}

	public String getHighlightedString() {
		final String highlightedString = Objects.toString(highlighted);
		return highlightedString;
	}

	public String getJobCompanyName() {
		final String jobCompanyName = (jobCompany != null) ? jobCompany.getName() : null;
		return jobCompanyName;
	}

	public String getJobCategoryName() {
		final String jobCategoryName = (jobCategory != null) ? jobCategory.getName() : null;
		return jobCategoryName;
	}

	@LazyEntityRelationGetter
	public Set<JobRequest> getJobRequests() {
		return jobRequests;
	}

	@LazyEntityRelationGetter
	public Set<Long> getJobRequestIds() {
		final Set<Long> jobRequestIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobRequests)
				.map(jr -> jr.getId())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobRequestIds;
	}

	@LazyEntityRelationGetter
	public Set<AuthUser> getAuthUsers() {
		final Set<AuthUser> authUsers = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobRequests)
				.map(jr -> jr.getAuthUser())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUsers;
	}

	@LazyEntityRelationGetter
	public Set<Long> getAuthUserIds() {
		final Set<Long> authUserIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobRequests)
				.map(jr -> jr.getAuthUser())
				.map(jv -> jv.getId())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserIds;
	}

	@LazyEntityRelationGetter
	public Set<String> getAuthUserEmails() {
		final Set<String> authUserEmails = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobRequests)
				.map(jr -> jr.getAuthUser())
				.map(jv -> jv.getEmail())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserEmails;
	}

	@Override
	public boolean isPrintableEntity() {
		return true;
	}

	@Override
	public String getKeyFields() {
		final String idString = this.getIdString();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("id: "), idString);
		return result;
	}

	@Override
	public String getAuthUserFields() {
		return Constants.EMPTY_STRING;
	}

	@Override
	public String getOtherFields() {
		final String salaryString = this.getSalaryString();
		final String statusName = this.getStatusName();
		final String highlightedString = this.getHighlightedString();
		final String jobCompanyName = this.getJobCompanyName();
		final String jobCategoryName = this.getJobCategoryName();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("name: "), name, "\n",
			StyleApplier.getBoldString("description: "), description, "\n",
			StyleApplier.getBoldString("salary: "), salaryString, "\n",
			StyleApplier.getBoldString("status: "), statusName, "\n",
			StyleApplier.getBoldString("highlighted: "), highlightedString, "\n",
			StyleApplier.getBoldString("jobCompanyName: "), jobCompanyName, "\n",
			StyleApplier.getBoldString("jobCategoryName: "), jobCategoryName);
		return result;
	}

	@Override
	public String toString() {
		final String idString = this.getIdString();
		final String salaryString = this.getSalaryString();
		final String statusName = this.getStatusName();
		final String highlightedString = this.getHighlightedString();
		final String jobCompanyName = this.getJobCompanyName();
		final String jobCategoryName = this.getJobCategoryName();
		final String publicationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(this.getPublicationDateTime());
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();
		final String authUserEmails = this.getAuthUserEmails().toString();

		final String result = StringUtils.getStringJoined("JobVacancy [id=", idString, ", name=", name, ", description=", description, ", salary=", salaryString, ", status=", statusName, ", highlighted=", highlightedString, ", jobCompany=", jobCompanyName, ", jobCategory=", jobCategoryName, ", publicationDateTime=", publicationDateTimeString, ", details=", details,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail,
			", users=", authUserEmails, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(name, description, salary, status, highlighted, jobCompany, jobCategory, publicationDateTime, details);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		JobVacancy other = (JobVacancy) obj;
		return Objects.equals(name, other.name) && Objects.equals(description, other.description)
			&& Objects.equals(salary, other.salary) && Objects.equals(status, other.status)
			&& Objects.equals(highlighted, other.highlighted) && Objects.equals(jobCompany, other.jobCompany)
			&& Objects.equals(jobCategory, other.jobCategory) && Objects.equals(publicationDateTime, other.publicationDateTime)
			&& Objects.equals(details, other.details);
	}
}

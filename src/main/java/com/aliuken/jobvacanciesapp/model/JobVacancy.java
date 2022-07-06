package com.aliuken.jobvacanciesapp.model;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.StreamUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

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
    @Size(max=255)
    @Column(name="description", length=255, nullable=false)
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
    @Size(max=10000)
    @Column(name="details", length=10000, nullable=false)
	private String details;

	@NotNull
	@ManyToOne
	@JoinColumn(name="job_category_id", nullable=false)
	private JobCategory jobCategory;

	@NotNull
	@Column(name="publication_date_time", nullable=false)
	private LocalDateTime publicationDateTime;

	@OneToMany(mappedBy="jobVacancy", fetch=FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("id DESC")
	private Set<JobRequest> jobRequests;

	public JobVacancy() {
		super();
	}
	
	public boolean isVerified() {
		boolean isVerified = (JobVacancyStatus.APPROVED.equals(status) && Boolean.TRUE.equals(highlighted));
		return isVerified;
	}
	
	public boolean isVerifiable() {
		boolean isVerifiable = (!JobVacancyStatus.DELETED.equals(status) && !this.isVerified());
		return isVerifiable;
	}

	public Set<AuthUser> getAuthUsers() {
		final Set<AuthUser> authUsers = StreamUtils.ofNullableCollectionParallel(this.getJobRequests())
				.map(jr -> jr.getAuthUser())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUsers;
	}
	
	public Set<Long> getAuthUserIds() {
		final Set<Long> authUserIds = StreamUtils.ofNullableCollectionParallel(this.getJobRequests())
				.map(jr -> jr.getAuthUser())
				.map(jv -> jv.getId())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserIds;
	}

	public Set<String> getAuthUserEmails() {
		final Set<String> authUserEmails = StreamUtils.ofNullableCollectionParallel(this.getJobRequests())
				.map(jr -> jr.getAuthUser())
				.map(jv -> jv.getEmail())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserEmails;
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(this.getId());
		final String salaryString = (salary != null) ? salary.toString() : null;
		final String statusCode = status.getCode() + "";
		final String highlightedString = highlighted.toString();
		final String jobCompanyName = jobCompany.getName();
		final String jobCategoryName = jobCategory.getName();
		final String publicationDateTimeString = DateTimeUtils.convertToString(this.getPublicationDateTime());
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(this.getFirstRegistrationDateTime());
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = DateTimeUtils.convertToString(this.getLastModificationDateTime());
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();
		final String authUserEmails = this.getAuthUserEmails().toString();

		final String result = StringUtils.getStringJoined("JobVacancy [id=", idString, ", name=", name, ", description=", description, ", salary=", salaryString, ", status=", statusCode, ", highlighted=", highlightedString, ", jobCompany=", jobCompanyName, ", details=", details, ", jobCategory=", jobCategoryName, ", publicationDateTime=", publicationDateTimeString, 
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, 
			", users=", authUserEmails, "]");

		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(name, description, salary, status, highlighted, jobCompany, details, jobCategory, publicationDateTime);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		JobVacancy other = (JobVacancy) obj;
		return Objects.equals(name, other.name) && Objects.equals(description, other.description)
			&& Objects.equals(salary, other.salary) && Objects.equals(status, other.status)
			&& Objects.equals(highlighted, other.highlighted) && Objects.equals(jobCompany, other.jobCompany)
			&& Objects.equals(details, other.details) && Objects.equals(jobCategory, other.jobCategory)
			&& Objects.equals(publicationDateTime, other.publicationDateTime);
	}

}

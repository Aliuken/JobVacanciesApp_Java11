package com.aliuken.jobvacanciesapp.model.entity;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.annotation.LazyEntityRelationGetter;
import com.aliuken.jobvacanciesapp.model.comparator.AuthUserRoleAuthRolePriorityComparator;
import com.aliuken.jobvacanciesapp.model.comparator.JobRequestJobVacancyPublicationDateTimeComparator;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Currency;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SortComparator;
import org.jspecify.annotations.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="auth_user", indexes={
		@Index(name="auth_user_unique_key_1", columnList="email", unique=true),
		@Index(name="auth_user_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="auth_user_key_2", columnList="last_modification_auth_user_id"),
		@Index(name="auth_user_key_3", columnList="enabled")})
@Getter
@Setter
public class AuthUser extends AbstractEntity<AuthUser> {
	private static final long serialVersionUID = -2992782217868515621L;

	@Size(max=255)
	@Column(name="email", length=255, nullable=false, unique=true)
	@Email(message="Email is not in a valid format")
	private @NonNull String email;

	@Size(max=25)
	@Column(name="name", length=25, nullable=false)
	private @NonNull String name;

	@Size(max=35)
	@Column(name="surnames", length=35, nullable=false)
	private @NonNull String surnames;

	@Column(name="language", nullable=false)
	private @NonNull Language language;

	@Column(name="enabled", nullable=false)
	private @NonNull Boolean enabled;

	@Column(name="color_mode", nullable=false)
	private @NonNull ColorMode colorMode;

	@Column(name="initial_currency", nullable=false)
	private @NonNull Currency initialCurrency;

	@Column(name="initial_table_sorting_direction", nullable=false)
	private @NonNull TableSortingDirection initialTableSortingDirection;

	@Column(name="initial_table_page_size", nullable=false)
	private @NonNull TablePageSize initialTablePageSize;

	@Column(name = "pdf_document_page_format", nullable = false)
	private @NonNull PdfDocumentPageFormat pdfDocumentPageFormat;

	@OneToMany(mappedBy="authUser", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@SortComparator(AuthUserRoleAuthRolePriorityComparator.class)
	private @NonNull Set<AuthUserRole> authUserRoles = new LinkedHashSet<>();

	@OneToMany(mappedBy="authUser", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@SortComparator(JobRequestJobVacancyPublicationDateTimeComparator.class)
	private @NonNull Set<JobRequest> jobRequests = new LinkedHashSet<>();

	@OneToMany(mappedBy="authUser", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("id DESC")
	private @NonNull Set<AuthUserCurriculum> authUserCurriculums = new LinkedHashSet<>();

	@OneToMany(mappedBy="authUser", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("firstRegistrationDateTime DESC")
	private @NonNull Set<AuthUserEntityQuery> authUserEntityQueries = new LinkedHashSet<>();

	public AuthUser() {
		super();
	}

	public @NonNull String getFullName() {
		String fullName = StringUtils.getStringJoined(name, Constants.SPACE, surnames);
		return fullName;
	}

	public @NonNull String getLanguageName() {
		final String languageName = String.valueOf(language);
		return languageName;
	}

	public @NonNull String getEnabledString() {
		final String enabledString = String.valueOf(enabled);
		return enabledString;
	}

	public @NonNull String getColorModeName() {
		final String colorModeName = String.valueOf(colorMode);
		return colorModeName;
	}

	public String getInitialCurrencySymbol() {
		final String initialCurrencySymbol = (initialCurrency != null) ? initialCurrency.getSymbol() : null;
		return initialCurrencySymbol;
	}

	public @NonNull String getInitialTableSortingDirectionName() {
		final String initialTableSortingDirectionName = String.valueOf(initialTableSortingDirection);
		return initialTableSortingDirectionName;
	}

	public @NonNull String getInitialTablePageSizeName() {
		final String initialTablePageSizeName = String.valueOf(initialTablePageSize);
		return initialTablePageSizeName;
	}

	public @NonNull String getPdfDocumentPageFormatName() {
		final String pdfDocumentPageFormatName = String.valueOf(pdfDocumentPageFormat);
		return pdfDocumentPageFormatName;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<AuthUserRole> getAuthUserRoles() {
		return authUserRoles;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<JobRequest> getJobRequests() {
		return jobRequests;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<AuthUserCurriculum> getAuthUserCurriculums() {
		return authUserCurriculums;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<AuthUserEntityQuery> getAuthUserEntityQueries() {
		return authUserEntityQueries;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<Long> getAuthUserRoleIds() {
		final Set<Long> authUserRoleIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.map(authUserRole -> authUserRole.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserRoleIds;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<AuthRole> getAuthRoles() {
		final Set<AuthRole> authRoles = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.map(authUserRole -> authUserRole.getAuthRole())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authRoles;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<Long> getAuthRoleIds() {
		final Set<Long> authRoleIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.map(authUserRole -> authUserRole.getAuthRole())
			.map(authRole -> authRole.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authRoleIds;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<String> getAuthRoleNames() {
		final Set<String> authRoleNames = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.map(authUserRole -> authUserRole.getAuthRole())
			.map(authRole -> authRole.getName())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authRoleNames;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<Long> getJobRequestIds() {
		final Set<Long> jobRequestIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobRequests)
			.map(jobRequest -> jobRequest.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobRequestIds;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<JobVacancy> getJobVacancies() {
		final Set<JobVacancy> jobVacancies = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobRequests)
			.map(jobRequest -> jobRequest.getJobVacancy())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancies;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<Long> getJobVacancyIds() {
		final Set<Long> jobVacancyIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobRequests)
			.map(jobRequest -> jobRequest.getJobVacancy())
			.map(jobVacancy -> jobVacancy.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancyIds;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<String> getJobVacancyNames() {
		final Set<String> jobVacancyNames = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobRequests)
			.map(jobRequest -> jobRequest.getJobVacancy())
			.map(jobVacancy -> jobVacancy.getName())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancyNames;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<Long> getAuthUserCurriculumIds() {
		final Set<Long> authUserCurriculumIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserCurriculums)
			.map(authUserCurriculum -> authUserCurriculum.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserCurriculumIds;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<String> getAuthUserCurriculumSelectionNames() {
		final Set<String> authUserCurriculumSelectionNames = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserCurriculums)
			.map(authUserCurriculum -> authUserCurriculum.getSelectionName())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserCurriculumSelectionNames;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<Long> getAuthUserEntityQueryIds() {
		final Set<Long> authUserEntityQueryIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserEntityQueries)
			.map(authUserEntityQuery -> authUserEntityQuery.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserEntityQueryIds;
	}

	@LazyEntityRelationGetter
	public AuthRole getMaxPriorityAuthRole() {
		if(LogicalUtils.isNullOrEmpty(authUserRoles)) {
			return null;
		}

		final AuthRole authRole = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.findFirst()
			.map(authUserRole -> authUserRole.getAuthRole())
			.orElse(null);

		return authRole;
	}

	@LazyEntityRelationGetter
	public Long getMaxPriorityAuthRoleId() {
		if(LogicalUtils.isNullOrEmpty(authUserRoles)) {
			return null;
		}

		final Long authRoleId = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.findFirst()
			.map(authUserRole -> authUserRole.getAuthRole())
			.map(authRole -> authRole.getId())
			.orElse(null);

		return authRoleId;
	}

	@LazyEntityRelationGetter
	public String getMaxPriorityAuthRoleName() {
		if(LogicalUtils.isNullOrEmpty(authUserRoles)) {
			return null;
		}

		final String authRoleName = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.findFirst()
			.map(authUserRole -> authUserRole.getAuthRole())
			.map(authRole -> authRole.getName())
			.orElse(null);

		return authRoleName;
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
			StyleApplier.getBoldString("email: "), email);
		return result;
	}

	@Override
	public @NonNull String getAuthUserFields() {
		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("email: "), email, Constants.NEWLINE,
			StyleApplier.getBoldString("name: "), name, Constants.NEWLINE,
			StyleApplier.getBoldString("surnames: "), surnames);
		return result;
	}

	@Override
	public @NonNull String getOtherFields() {
		final String languageName = this.getLanguageName();
		final String enabledString = this.getEnabledString();
		final String initialCurencySymbol = this.getInitialCurrencySymbol();
		final String initialTableSortingDirectionName = this.getInitialTableSortingDirectionName();
		final String initialTablePageSizeName = this.getInitialTablePageSizeName();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("language: "), languageName, Constants.NEWLINE,
			StyleApplier.getBoldString("enabled: "), enabledString, Constants.NEWLINE,
			StyleApplier.getBoldString("colorMode: "), this.getColorModeName(), Constants.NEWLINE,
			StyleApplier.getBoldString("initialCurrency: "), initialCurencySymbol, Constants.NEWLINE,
			StyleApplier.getBoldString("initialTableSortingDirection: "), initialTableSortingDirectionName, Constants.NEWLINE,
			StyleApplier.getBoldString("initialTablePageSize: "), initialTablePageSizeName);
		return result;
	}

	@Override
	public @NonNull String toString() {
		final String idString = this.getIdString();
		final String languageName = this.getLanguageName();
		final String enabledString = this.getEnabledString();
		final String colorModeName = this.getColorModeName();
		final String initialCurrencySymbol = this.getInitialCurrencySymbol();
		final String initialTableSortingDirectionName = this.getInitialTableSortingDirectionName();
		final String initialTablePageSizeName = this.getInitialTablePageSizeName();
		final String pdfDocumentPageFormatName = this.getPdfDocumentPageFormatName();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();
		final String authRoles = this.getAuthRoleNames().toString();
		final String jobVacancies = this.getJobVacancyNames().toString();

		final String result = StringUtils.getStringJoined("AuthUser [id=", idString, ", email=", email, ", name=", name, ", surnames=", surnames, ", language=", languageName, ", enabled=", enabledString, ", colorMode=", colorModeName,
			", initialCurrency=", initialCurrencySymbol, ", initialTableSortingDirection=", initialTableSortingDirectionName, ", initialTablePageSize=", initialTablePageSizeName, ", pdfDocumentPageFormat=", pdfDocumentPageFormatName,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail,
			", authRoles=", authRoles, ", jobVacancies=", jobVacancies, "]");

		return result;
	}
}

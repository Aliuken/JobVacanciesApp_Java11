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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedHashSet;
import java.util.Objects;
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
public class AuthUser extends AbstractEntity<AuthUser> implements Externalizable {
	private static final long serialVersionUID = -2992782217868515621L;

	@NotNull
	@Size(max=255)
	@Column(name="email", length=255, nullable=false, unique=true)
	@Email(message="Email is not in a valid format")
	private String email;

	@NotNull
	@Size(max=25)
	@Column(name="name", length=25, nullable=false)
	private String name;

	@NotNull
	@Size(max=35)
	@Column(name="surnames", length=35, nullable=false)
	private String surnames;

	@NotNull
	@Column(name="language", nullable=false)
	private Language language;

	@NotNull
	@Column(name="enabled", nullable=false)
	private Boolean enabled;

	@NotNull
	@Column(name="color_mode", nullable=false)
	private ColorMode colorMode;

	@NotNull
	@Column(name="initial_currency", nullable=false)
	private Currency initialCurrency;

	@NotNull
	@Column(name="initial_table_sorting_direction", nullable=false)
	private TableSortingDirection initialTableSortingDirection;

	@NotNull
	@Column(name="initial_table_page_size", nullable=false)
	private TablePageSize initialTablePageSize;

	@NotNull
	@Column(name = "pdf_document_page_format", nullable = false)
	private PdfDocumentPageFormat pdfDocumentPageFormat;

	@OneToMany(mappedBy="authUser", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@SortComparator(AuthUserRoleAuthRolePriorityComparator.class)
	private Set<AuthUserRole> authUserRoles;

	@OneToMany(mappedBy="authUser", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@SortComparator(JobRequestJobVacancyPublicationDateTimeComparator.class)
	private Set<JobRequest> jobRequests;

	@OneToMany(mappedBy="authUser", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("id DESC")
	private Set<AuthUserCurriculum> authUserCurriculums;

	@OneToMany(mappedBy="authUser", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("firstRegistrationDateTime DESC")
	private Set<AuthUserEntityQuery> authUserEntityQueries;

	public AuthUser() {
		super();
	}

	@NotNull
	public String getFullName() {
		String fullName = StringUtils.getStringJoined(name, Constants.SPACE, surnames);
		return fullName;
	}

	public String getLanguageName() {
		final String languageName = Objects.toString(language);
		return languageName;
	}

	public String getEnabledString() {
		final String enabledString = Objects.toString(enabled);
		return enabledString;
	}

	public String getColorModeName() {
		final String colorModeName = Objects.toString(colorMode);
		return colorModeName;
	}

	public String getInitialCurrencySymbol() {
		final String initialCurrencySymbol = (initialCurrency != null) ? initialCurrency.getSymbol() : null;
		return initialCurrencySymbol;
	}

	public String getInitialTableSortingDirectionName() {
		final String initialTableSortingDirectionName = Objects.toString(initialTableSortingDirection);
		return initialTableSortingDirectionName;
	}

	public String getInitialTablePageSizeName() {
		final String initialTablePageSizeName = Objects.toString(initialTablePageSize);
		return initialTablePageSizeName;
	}

	public String getPdfDocumentPageFormatName() {
		final String pdfDocumentPageFormatName = Objects.toString(pdfDocumentPageFormat);
		return pdfDocumentPageFormatName;
	}

	@LazyEntityRelationGetter
	public Set<AuthUserRole> getAuthUserRoles() {
		return authUserRoles;
	}

	@LazyEntityRelationGetter
	public Set<JobRequest> getJobRequests() {
		return jobRequests;
	}

	@LazyEntityRelationGetter
	public Set<AuthUserCurriculum> getAuthUserCurriculums() {
		return authUserCurriculums;
	}

	@LazyEntityRelationGetter
	public Set<AuthUserEntityQuery> getAuthUserEntityQueries() {
		return authUserEntityQueries;
	}

	@LazyEntityRelationGetter
	public Set<Long> getAuthUserRoleIds() {
		final Set<Long> authUserRoleIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.map(aur -> aur.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserRoleIds;
	}

	@LazyEntityRelationGetter
	public Set<AuthRole> getAuthRoles() {
		final Set<AuthRole> authRoles = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.map(aur -> aur.getAuthRole())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authRoles;
	}

	@LazyEntityRelationGetter
	public Set<Long> getAuthRoleIds() {
		final Set<Long> authRoleIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.map(aur -> aur.getAuthRole())
			.map(ar -> ar.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authRoleIds;
	}

	@LazyEntityRelationGetter
	public Set<String> getAuthRoleNames() {
		final Set<String> authRoleNames = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.map(aur -> aur.getAuthRole())
			.map(ar -> ar.getName())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authRoleNames;
	}

	@LazyEntityRelationGetter
	public Set<Long> getJobRequestIds() {
		final Set<Long> jobRequestIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobRequests)
			.map(jr -> jr.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobRequestIds;
	}

	@LazyEntityRelationGetter
	public Set<JobVacancy> getJobVacancies() {
		final Set<JobVacancy> jobVacancies = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobRequests)
			.map(jr -> jr.getJobVacancy())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancies;
	}

	@LazyEntityRelationGetter
	public Set<Long> getJobVacancyIds() {
		final Set<Long> jobVacancyIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobRequests)
			.map(jr -> jr.getJobVacancy())
			.map(jv -> jv.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancyIds;
	}

	@LazyEntityRelationGetter
	public Set<String> getJobVacancyNames() {
		final Set<String> jobVacancyNames = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobRequests)
			.map(jr -> jr.getJobVacancy())
			.map(jv -> jv.getName())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancyNames;
	}

	@LazyEntityRelationGetter
	public Set<Long> getAuthUserCurriculumIds() {
		final Set<Long> authUserCurriculumIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserCurriculums)
			.map(auc -> auc.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserCurriculumIds;
	}

	@LazyEntityRelationGetter
	public Set<String> getAuthUserCurriculumSelectionNames() {
		final Set<String> authUserCurriculumSelectionNames = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserCurriculums)
			.map(auc -> auc.getSelectionName())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserCurriculumSelectionNames;
	}

	@LazyEntityRelationGetter
	public Set<Long> getAuthUserEntityQueryIds() {
		final Set<Long> authUserEntityQueryIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserEntityQueries)
			.map(aueq -> aueq.getId())
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
			.map(aur -> aur.getAuthRole())
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
			.map(aur -> aur.getAuthRole())
			.map(ar -> ar.getId())
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
			.map(aur -> aur.getAuthRole())
			.map(ar -> ar.getName())
			.orElse(null);

		return authRoleName;
	}

	@Override
	public boolean isPrintableEntity() {
		return true;
	}

	@Override
	public String getKeyFields() {
		final String idString = this.getIdString();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("id: "), idString, Constants.NEWLINE,
			StyleApplier.getBoldString("email: "), email);
		return result;
	}

	@Override
	public String getAuthUserFields() {
		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("email: "), email, Constants.NEWLINE,
			StyleApplier.getBoldString("name: "), name, Constants.NEWLINE,
			StyleApplier.getBoldString("surnames: "), surnames);
		return result;
	}

	@Override
	public String getOtherFields() {
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
	public String toString() {
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

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(getId());

		objectOutput.writeUTF(email);
		objectOutput.writeUTF(name);
		objectOutput.writeUTF(surnames);

		final String languageCode = language.getCode();
		objectOutput.writeUTF(languageCode);

		objectOutput.writeBoolean(enabled);

		final String colorModeCode = colorMode.getCode();
		objectOutput.writeUTF(colorModeCode);

		final String initialCurrencySymbol = initialCurrency.getSymbol();
		objectOutput.writeUTF(initialCurrencySymbol);

		final String initialTableSortingDirectionCode = initialTableSortingDirection.getCode();
		objectOutput.writeUTF(initialTableSortingDirectionCode);

		final int initialTablePageSizeValue = initialTablePageSize.getValue();
		objectOutput.writeInt(initialTablePageSizeValue);

		final String pdfDocumentPageFormatCode = pdfDocumentPageFormat.getCode();
		objectOutput.writeUTF(pdfDocumentPageFormatCode);

		final String firstRegistrationDateTimeString = Constants.DATE_TIME_UTILS.convertToStringForSerialization(getFirstRegistrationDateTime());
		objectOutput.writeUTF(firstRegistrationDateTimeString);

		objectOutput.writeObject(getFirstRegistrationAuthUser());

		final String lastModificationDateTimeString = Constants.DATE_TIME_UTILS.convertToStringForSerialization(getLastModificationDateTime());
		objectOutput.writeUTF(lastModificationDateTimeString);

		objectOutput.writeObject(getLastModificationAuthUser());
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
		setId(objectInput.readLong());

		email = objectInput.readUTF();
		name = objectInput.readUTF();
		surnames = objectInput.readUTF();

		final String languageCode = objectInput.readUTF();
		language = Language.findByCode(languageCode);

		enabled = objectInput.readBoolean();

		final String colorModeCode = objectInput.readUTF();
		colorMode = ColorMode.findByCode(colorModeCode);

		final String initialCurrencySymbol = objectInput.readUTF();
		initialCurrency = Currency.findBySymbol(initialCurrencySymbol);

		final String initialTableSortingDirectionCode = objectInput.readUTF();
		initialTableSortingDirection = TableSortingDirection.findByCode(initialTableSortingDirectionCode);

		final int initialTablePageSizeValue = objectInput.readInt();
		initialTablePageSize = TablePageSize.findByValue(initialTablePageSizeValue);

		final String pdfDocumentPageFormatCode = objectInput.readUTF();
		pdfDocumentPageFormat = PdfDocumentPageFormat.findByCode(pdfDocumentPageFormatCode);

		final String firstRegistrationDateTimeString = objectInput.readUTF();
		setFirstRegistrationDateTime(Constants.DATE_TIME_UTILS.convertFromStringForSerialization(firstRegistrationDateTimeString));

		setFirstRegistrationAuthUser((AuthUser) objectInput.readObject());

		final String lastModificationDateTimeString = objectInput.readUTF();
		setLastModificationDateTime(Constants.DATE_TIME_UTILS.convertFromStringForSerialization(lastModificationDateTimeString));

		setLastModificationAuthUser((AuthUser) objectInput.readObject());
	}
}

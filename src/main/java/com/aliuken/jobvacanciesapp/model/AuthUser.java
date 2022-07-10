package com.aliuken.jobvacanciesapp.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SortComparator;

import com.aliuken.jobvacanciesapp.model.comparator.AuthUserRoleRolePriorityComparator;
import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.StreamUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Entity
@Table(name="auth_user", indexes={
		@Index(name="auth_user_unique_key_1", columnList="email", unique=true),
		@Index(name="auth_user_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="auth_user_key_2", columnList="last_modification_auth_user_id"),
		@Index(name="auth_user_key_3", columnList="enabled")})
@Data
public class AuthUser extends AbstractEntity implements Externalizable {

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
	private AuthUserLanguage language;

    @NotNull
    @Column(name="enabled", nullable=false)
	private Boolean enabled;

    @OneToMany(mappedBy="authUser", fetch=FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
	@SortComparator(AuthUserRoleRolePriorityComparator.class)
	private Set<AuthUserRole> authUserRoles;

    @OneToMany(mappedBy="authUser", fetch=FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("id DESC")
	private Set<JobRequest> jobRequests;

    @OneToMany(mappedBy="authUser", fetch=FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("id DESC")
	private Set<AuthUserCurriculum> authUserCurriculums;

	public AuthUser() {
		super();
	}

	public String getFullName() {
		String fullName = StringUtils.getStringJoined(name, " ", surnames);
		return fullName;
	}

	public Set<Long> getAuthUserRoleIds() {
		final Set<Long> authUserRoleIds = StreamUtils.ofNullableCollectionParallel(this.getAuthUserRoles())
				.map(aur -> aur.getId())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserRoleIds;
	}

	public Set<AuthRole> getAuthRoles() {
		final Set<AuthRole> authRoles = StreamUtils.ofNullableCollectionParallel(this.getAuthUserRoles())
				.map(aur -> aur.getAuthRole())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authRoles;
	}

	public Set<Long> getAuthRoleIds() {
		final Set<Long> authRoleIds = StreamUtils.ofNullableCollectionParallel(this.getAuthUserRoles())
				.map(aur -> aur.getAuthRole())
				.map(ar -> ar.getId())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authRoleIds;
	}

	public Set<String> getAuthRoleNames() {
		final Set<String> authRoleNames = StreamUtils.ofNullableCollectionParallel(this.getAuthUserRoles())
				.map(aur -> aur.getAuthRole())
				.map(ar -> ar.getName())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authRoleNames;
	}

	public Set<Long> getJobRequestIds() {
		final Set<Long> jobRequestIds = StreamUtils.ofNullableCollectionParallel(this.getJobRequests())
				.map(jr -> jr.getId())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobRequestIds;
	}

	public Set<JobVacancy> getJobVacancies() {
		final Set<JobVacancy> jobVacancies = StreamUtils.ofNullableCollectionParallel(this.getJobRequests())
				.map(jr -> jr.getJobVacancy())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancies;
	}

	public Set<Long> getJobVacancyIds() {
		final Set<Long> jobVacancyIds = StreamUtils.ofNullableCollectionParallel(this.getJobRequests())
				.map(jr -> jr.getJobVacancy())
				.map(jv -> jv.getId())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancyIds;
	}

	public Set<String> getJobVacancyNames() {
		final Set<String> jobVacancyNames = StreamUtils.ofNullableCollectionParallel(this.getJobRequests())
				.map(jr -> jr.getJobVacancy())
				.map(jv -> jv.getName())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return jobVacancyNames;
	}

	public Set<Long> getAuthUserCurriculumIds() {
		final Set<Long> authUserCurriculumIds = StreamUtils.ofNullableCollectionParallel(this.getAuthUserCurriculums())
				.map(auc -> auc.getId())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserCurriculumIds;
	}

	public Set<String> getAuthUserCurriculumSelectionNames() {
		final Set<String> authUserCurriculumSelectionNames = StreamUtils.ofNullableCollectionParallel(this.getAuthUserCurriculums())
				.map(auc -> auc.getSelectionName())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserCurriculumSelectionNames;
	}

	public AuthRole getMaxPriorityAuthRole() {
		if (this.getAuthUserRoles() == null || this.getAuthUserRoles().isEmpty()) {
			return null;
		}

		AuthRole authRole = StreamUtils.ofNullableCollectionParallel(this.getAuthUserRoles())
				.findFirst()
				.map(aur -> aur.getAuthRole())
				.orElse(null);

		return authRole;
	}

	public Long getMaxPriorityAuthRoleId() {
		if (this.getAuthUserRoles() == null || this.getAuthUserRoles().isEmpty()) {
			return null;
		}

		Long authRoleId = StreamUtils.ofNullableCollectionParallel(this.getAuthUserRoles())
				.findFirst()
				.map(aur -> aur.getAuthRole())
				.map(ar -> ar.getId())
				.orElse(null);

		return authRoleId;
	}

	public String getMaxPriorityAuthRoleName() {
		if (this.getAuthUserRoles() == null || this.getAuthUserRoles().isEmpty()) {
			return null;
		}

		String authRoleName = StreamUtils.ofNullableCollectionParallel(this.getAuthUserRoles())
				.findFirst()
				.map(aur -> aur.getAuthRole())
				.map(ar -> ar.getName())
				.orElse(null);

		return authRoleName;
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(this.getId());
		final String languageCode = language.getCode();
		final String enabledString = enabled.toString();
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(this.getFirstRegistrationDateTime());
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = DateTimeUtils.convertToString(this.getLastModificationDateTime());
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();
		final String authRoles = this.getAuthRoleNames().toString();
		final String jobVacancies = this.getJobVacancyNames().toString();

		final String result = StringUtils.getStringJoined("AuthUser [id=", idString, ", email=", email, ", name=", name, ", surnames=", surnames, ", language=", languageCode, ", enabled=", enabledString,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail,
			", authRoles=", authRoles, ", jobVacancies=", jobVacancies, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(email, name, surnames, language, enabled);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		AuthUser other = (AuthUser) obj;
		return Objects.equals(email, other.email) && Objects.equals(name, other.name) && Objects.equals(surnames, other.surnames)
			&& Objects.equals(language, other.language) && Objects.equals(enabled, other.enabled);
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(getId());

		objectOutput.writeUTF(email);
		objectOutput.writeUTF(name);
		objectOutput.writeUTF(surnames);

		String languageCode = language.getCode();
		objectOutput.writeUTF(languageCode);

		objectOutput.writeBoolean(enabled);

		String firstRegistrationDateTimeString = DateTimeUtils.convertToStringForSerialization(getFirstRegistrationDateTime());
		objectOutput.writeUTF(firstRegistrationDateTimeString);

		objectOutput.writeObject(getFirstRegistrationAuthUser());

		String lastModificationDateTimeString = DateTimeUtils.convertToStringForSerialization(getLastModificationDateTime());
		objectOutput.writeUTF(lastModificationDateTimeString);

		objectOutput.writeObject(getLastModificationAuthUser());
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
		setId(objectInput.readLong());

		email = objectInput.readUTF();
		name = objectInput.readUTF();
		surnames = objectInput.readUTF();

		String languageCode = objectInput.readUTF();
		language = AuthUserLanguage.findByCode(languageCode);

		enabled = objectInput.readBoolean();

		String firstRegistrationDateTimeString = objectInput.readUTF();
		setFirstRegistrationDateTime(DateTimeUtils.convertFromStringForSerialization(firstRegistrationDateTimeString));

		setFirstRegistrationAuthUser((AuthUser) objectInput.readObject());

		String lastModificationDateTimeString = objectInput.readUTF();
		setLastModificationDateTime(DateTimeUtils.convertFromStringForSerialization(lastModificationDateTimeString));

		setLastModificationAuthUser((AuthUser) objectInput.readObject());
	}

}

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SortComparator;

import com.aliuken.jobvacanciesapp.model.comparator.AuthUserRoleUserFullNameComparator;
import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.StreamUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Entity
@Table(name="auth_role", indexes={
		@Index(name="auth_role_unique_key_1", columnList="name", unique=true),
		@Index(name="auth_role_unique_key_2", columnList="message_name", unique=true),
		@Index(name="auth_role_unique_key_3", columnList="priority", unique=true),
		@Index(name="auth_role_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="auth_role_key_2", columnList="last_modification_auth_user_id")})
@Data
public class AuthRole extends AbstractEntity {

    private static final long serialVersionUID = -3571531666008146592L;

    public static final String SUPERVISOR = "SUPERVISOR";
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    public static final String USER = "USER";

    @NotNull
    @Size(max=20)
    @Column(name="name", length=20, nullable=false, unique=true)
	private String name;

    @NotNull
    @Size(max=30)
    @Column(name="message_name", length=30, nullable=false, unique=true)
    private String messageName;

    @NotNull
    @Column(name="priority", nullable=false, unique=true)
    private Byte priority;

    @OneToMany(mappedBy="authRole", fetch=FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
	@SortComparator(AuthUserRoleUserFullNameComparator.class)
	private Set<AuthUserRole> authUserRoles;

	public AuthRole() {
		super();
	}

	public Set<AuthUser> getAuthUsers() {
		final Set<AuthUser> authUsers = StreamUtils.ofNullableCollectionParallel(this.getAuthUserRoles())
				.map(aup -> aup.getAuthUser())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUsers;
	}
	
	public Set<Long> getAuthUserIds() {
		final Set<Long> authUserIds = StreamUtils.ofNullableCollectionParallel(this.getAuthUserRoles())
				.map(aup -> aup.getAuthUser())
				.map(au -> au.getId())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserIds;
	}

	public Set<String> getAuthUserEmails() {
		final Set<String> authUserEmails = StreamUtils.ofNullableCollectionParallel(this.getAuthUserRoles())
				.map(aup -> aup.getAuthUser())
				.map(au -> au.getEmail())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserEmails;
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(this.getId());
		final String priorityString = priority.toString();
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(this.getFirstRegistrationDateTime());
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = DateTimeUtils.convertToString(this.getLastModificationDateTime());
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();
		final String authUserEmails = this.getAuthUserEmails().toString();

		final String result = StringUtils.getStringJoined("AuthRole [id=", idString, ", name=", name, ", messageName=", messageName, ", priority=", priorityString, 
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, 
			", users=", authUserEmails, "]");

		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(name, messageName, priority);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		AuthRole other = (AuthRole) obj;
		return Objects.equals(name, other.name) && Objects.equals(messageName, other.messageName) && Objects.equals(priority, other.priority);
	}

}

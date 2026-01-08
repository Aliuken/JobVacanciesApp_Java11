package com.aliuken.jobvacanciesapp.model.entity;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.annotation.LazyEntityRelationGetter;
import com.aliuken.jobvacanciesapp.model.comparator.AuthUserRoleAuthUserFullNameComparator;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.superinterface.Internationalizable;
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
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="auth_role", indexes={
		@Index(name="auth_role_unique_key_1", columnList="name", unique=true),
		@Index(name="auth_role_unique_key_2", columnList="message_name", unique=true),
		@Index(name="auth_role_unique_key_3", columnList="priority", unique=true),
		@Index(name="auth_role_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="auth_role_key_2", columnList="last_modification_auth_user_id")})
@Getter
@Setter
public class AuthRole extends AbstractEntity<AuthRole> implements Internationalizable {
	private static final long serialVersionUID = -3571531666008146592L;

	public static final String USER = "USER";
	public static final String SUPERVISOR = "SUPERVISOR";
	public static final String ADMINISTRATOR = "ADMINISTRATOR";

	@Size(max=20)
	@Column(name="name", length=20, nullable=false, unique=true)
	private @NonNull String name;

	@Size(max=30)
	@Column(name="message_name", length=30, nullable=false, unique=true)
	private @NonNull String messageName;

	@Column(name="priority", nullable=false, unique=true)
	private @NonNull Byte priority;

	@OneToMany(mappedBy="authRole", fetch=FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@SortComparator(AuthUserRoleAuthUserFullNameComparator.class)
	private @NonNull Set<AuthUserRole> authUserRoles = new LinkedHashSet<>();

	public AuthRole() {
		super();
	}

	public @NonNull String getPriorityString() {
		final String priorityString = String.valueOf(priority);
		return priorityString;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<AuthUserRole> getAuthUserRoles() {
		return authUserRoles;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<Long> getAuthUserRoleIds() {
		final Set<Long> authUserRoleIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.map(authUserRole -> authUserRole.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserRoleIds;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<AuthUser> getAuthUsers() {
		final Set<AuthUser> authUsers = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.map(authUserRole -> authUserRole.getAuthUser())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUsers;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<Long> getAuthUserIds() {
		final Set<Long> authUserIds = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.map(authUserRole -> authUserRole.getAuthUser())
			.map(authUser -> authUser.getId())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserIds;
	}

	@LazyEntityRelationGetter
	public @NonNull Set<String> getAuthUserEmails() {
		final Set<String> authUserEmails = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(authUserRoles)
			.map(authUserRole -> authUserRole.getAuthUser())
			.map(authUser -> authUser.getEmail())
			.collect(Collectors.toCollection(LinkedHashSet::new));

		return authUserEmails;
	}

	@Override
	public boolean isPrintableEntity() {
		return true;
	}

	@Override
	public @NonNull String getKeyFields() {
		final String idString = this.getIdString();
		final String priorityString = this.getPriorityString();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("id: "), idString, Constants.NEWLINE,
			StyleApplier.getBoldString("name: "), name, Constants.NEWLINE,
			StyleApplier.getBoldString("messageName: "), messageName, Constants.NEWLINE,
			StyleApplier.getBoldString("priority: "), priorityString);
		return result;
	}

	@Override
	public @NonNull String getAuthUserFields() {
		return Constants.EMPTY_STRING;
	}

	@Override
	public @NonNull String getOtherFields() {
		return Constants.EMPTY_STRING;
	}

	@Override
	public @NonNull String toString() {
		final String idString = this.getIdString();
		final String priorityString = this.getPriorityString();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();
		final String authUserEmails = this.getAuthUserEmails().toString();

		final String result = StringUtils.getStringJoined("AuthRole [id=", idString, ", name=", name, ", messageName=", messageName, ", priority=", priorityString,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail,
			", users=", authUserEmails, "]");

		return result;
	}
}

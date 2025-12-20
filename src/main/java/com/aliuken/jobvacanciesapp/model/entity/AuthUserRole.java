package com.aliuken.jobvacanciesapp.model.entity;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntityWithAuthUser;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="auth_user_role", indexes={
		@Index(name="auth_user_role_unique_key_1", columnList="auth_user_id,auth_role_id", unique=true),
		@Index(name="auth_user_role_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="auth_user_role_key_2", columnList="last_modification_auth_user_id"),
		@Index(name="auth_user_role_key_3", columnList="auth_user_id"),
		@Index(name="auth_user_role_key_4", columnList="auth_role_id")})
@Getter
@Setter
public class AuthUserRole extends AbstractEntityWithAuthUser<AuthUserRole> {
	private static final long serialVersionUID = -7984070191950848318L;

	@NotNull
	@ManyToOne
	@JoinColumn(name="auth_user_id", nullable=false)
	private AuthUser authUser;

	@NotNull
	@ManyToOne
	@JoinColumn(name="auth_role_id", nullable=false)
	private AuthRole authRole;

	public AuthUserRole() {
		super();
	}

	public String getAuthRoleName() {
		final String authRoleName = (authRole != null) ? authRole.getName() : null;
		return authRoleName;
	}

	@Override
	public boolean isPrintableEntity() {
		return true;
	}

	@Override
	public String getKeyFields() {
		final String idString = this.getIdString();
		final String authUserIdString = this.getAuthUserId();
		final String authRoleIdString = (authRole != null) ? authRole.getIdString() : null;

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("id: "), idString, Constants.NEWLINE,
			StyleApplier.getBoldString("[authUserId, authRoleId]: "), "[", authUserIdString, ", ", authRoleIdString, "]");
		return result;
	}

	@Override
	public String getOtherFields() {
		final String authRoleName = this.getAuthRoleName();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("authRoleName: "), authRoleName);
		return result;
	}

	@Override
	public String toString() {
		final String idString = this.getIdString();
		final String authUserEmail = this.getAuthUserEmail();
		final String authRoleName = this.getAuthRoleName();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AuthUserRole [id=", idString, ", authUser=", authUserEmail, ", authRoleName=", authRoleName,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}
}

package com.aliuken.jobvacanciesapp.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Entity
@Table(name="auth_user_role", indexes={
		@Index(name="auth_user_role_unique_key_1", columnList="auth_user_id,auth_role_id", unique=true),
		@Index(name="auth_user_role_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="auth_user_role_key_2", columnList="last_modification_auth_user_id"),
		@Index(name="auth_user_role_key_3", columnList="auth_user_id"),
		@Index(name="auth_user_role_key_4", columnList="auth_role_id")})
@Data
public class AuthUserRole extends AbstractEntityWithAuthUser {

	private static final long serialVersionUID = -7984070191950848318L;

    @NotNull
	@ManyToOne
	@JoinColumn(name="auth_role_id", nullable=false)
	private AuthRole authRole;

	public AuthUserRole() {
		super();
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(this.getId());
		final String authUserEmail = (this.getAuthUser() != null) ? this.getAuthUser().getEmail() : null;
		final String authRoleName = (authRole != null) ? authRole.getName() : null;
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(this.getFirstRegistrationDateTime());
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = DateTimeUtils.convertToString(this.getLastModificationDateTime());
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AuthUserRole [id=", idString, ", authUser=", authUserEmail, ", authRoleName=", authRoleName, 
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(authRole);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		AuthUserRole other = (AuthUserRole) obj;
		return Objects.equals(authRole, other.authRole);
	}

}

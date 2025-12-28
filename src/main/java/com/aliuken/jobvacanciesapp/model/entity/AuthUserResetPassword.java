package com.aliuken.jobvacanciesapp.model.entity;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="auth_user_reset_password", indexes={
		@Index(name="auth_user_reset_password_unique_key_1", columnList="email", unique=true),
		@Index(name="auth_user_reset_password_unique_key_2", columnList="email,uuid", unique=true),
		@Index(name="auth_user_reset_password_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="auth_user_reset_password_key_2", columnList="last_modification_auth_user_id")})
@Getter
@Setter
public class AuthUserResetPassword extends AbstractEntity<AuthUserResetPassword> {
	private static final long serialVersionUID = -3538038698636350939L;

	@NotNull
	@Size(max=255)
	@Column(name="email", length=255, nullable=false, unique=true)
	@Email(message="Email is not in a valid format")
	private String email;

	@NotNull
	@Size(max=36)
	@Column(name="uuid", length=36, nullable=false)
	private String uuid;

	public AuthUserResetPassword() {
		super();
	}

	@Override
	public boolean isPrintableEntity() {
		return false;
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
			StyleApplier.getBoldString("email: "), email);
		return result;
	}

	@Override
	public @NonNull String getOtherFields() {
		return Constants.EMPTY_STRING;
	}

	@Override
	public @NonNull String toString() {
		final String idString = this.getIdString();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AuthUserResetPassword [id=", idString, ", email=", email, ", uuid=", uuid,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}
}
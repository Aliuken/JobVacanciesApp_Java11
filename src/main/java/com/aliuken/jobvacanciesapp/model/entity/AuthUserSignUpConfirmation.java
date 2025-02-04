package com.aliuken.jobvacanciesapp.model.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;

import lombok.Data;

@Entity
@Table(name="auth_user_signup_confirmation", indexes={
		@Index(name="auth_user_signup_confirmation_unique_key_1", columnList="email", unique=true),
		@Index(name="auth_user_signup_confirmation_unique_key_2", columnList="email,uuid", unique=true),
		@Index(name="auth_user_signup_confirmation_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="auth_user_signup_confirmation_key_2", columnList="last_modification_auth_user_id")})
@Data
public class AuthUserSignUpConfirmation extends AbstractEntity {
	private static final long serialVersionUID = -3817688436517437137L;

	@NotNull
	@Size(max=255)
	@Column(name="email", length=255, nullable=false, unique=true)
	@Email(message="Email is not in a valid format")
	private String email;

	@NotNull
	@Size(max=36)
	@Column(name="uuid", length=36, nullable=false)
	private String uuid;

	public AuthUserSignUpConfirmation() {
		super();
	}

	@Override
	public boolean isPrintableEntity() {
		return false;
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
			StyleApplier.getBoldString("email: "), email);
		return result;
	}

	@Override
	public String getOtherFields() {
		return Constants.EMPTY_STRING;
	}

	@Override
	public String toString() {
		final String idString = this.getIdString();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AuthUserSignUpConfirmation [id=", idString, ", email=", email, ", uuid=", uuid,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(email, uuid);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		AuthUserSignUpConfirmation other = (AuthUserSignUpConfirmation) obj;
		return Objects.equals(email, other.email) && Objects.equals(uuid, other.uuid);
	}
}
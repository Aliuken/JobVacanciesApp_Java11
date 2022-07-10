package com.aliuken.jobvacanciesapp.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Entity
@Table(name="auth_user_credentials", indexes={
		@Index(name="auth_user_credentials_unique_key_1", columnList="email", unique=true),
		@Index(name="auth_user_credentials_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="auth_user_credentials_key_2", columnList="last_modification_auth_user_id")})
@Data
public class AuthUserCredentials extends AbstractEntity {

    private static final long serialVersionUID = 1302984200214581263L;

    @NotNull
    @Size(max=255)
    @Column(name="email", length=255, nullable=false, unique=true)
    @Email(message="Email is not in a valid format")
	private String email;

    @NotNull
    @Size(max=60)
    @Column(name="encrypted_password", length=60, nullable=false)
    private String encryptedPassword;

	public AuthUserCredentials() {
		super();
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(this.getId());
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(this.getFirstRegistrationDateTime());
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = DateTimeUtils.convertToString(this.getLastModificationDateTime());
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AuthUserCredentials [id=", idString, ", email=", email,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(email, encryptedPassword);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		AuthUserCredentials other = (AuthUserCredentials) obj;
		return Objects.equals(email, other.email) && Objects.equals(encryptedPassword, other.encryptedPassword);
	}

}

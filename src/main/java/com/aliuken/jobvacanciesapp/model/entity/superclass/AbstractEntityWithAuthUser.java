package com.aliuken.jobvacanciesapp.model.entity.superclass;

import java.util.Objects;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class AbstractEntityWithAuthUser extends AbstractEntity {
	private static final long serialVersionUID = 2906355999654036448L;

	@NotNull
	@ManyToOne
	@JoinColumn(name="auth_user_id", nullable=false)
	private AuthUser authUser;

	public AbstractEntityWithAuthUser() {
		super();
	}

	public String getAuthUserId() {
		final String authUserId = (authUser != null) ? authUser.getIdString() : null;
		return authUserId;
	}

	public String getAuthUserEmail() {
		final String authUserEmail = (authUser != null) ? authUser.getEmail() : null;
		return authUserEmail;
	}

	public String getAuthUserName() {
		final String authUserName = (authUser != null) ? authUser.getName() : null;
		return authUserName;
	}

	public String getAuthUserSurnames() {
		final String authUserSurnames = (authUser != null) ? authUser.getSurnames() : null;
		return authUserSurnames;
	}

	@Override
	public String getAuthUserFields() {
		final String email = this.getAuthUserEmail();
		final String name = this.getAuthUserName();
		final String surnames = this.getAuthUserSurnames();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("email: "), email, "\n",
			StyleApplier.getBoldString("name: "), name, "\n",
			StyleApplier.getBoldString("surnames: "), surnames);
		return result;
	}

	@Override
	public String toString() {
		final String idString = this.getIdString();
		final String email = this.getAuthUserEmail();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AbstractEntity [id=", idString, ", user=", email,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(authUser);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		final AbstractEntityWithAuthUser other = (AbstractEntityWithAuthUser) obj;
		return Objects.equals(authUser, other.authUser);
	}
}

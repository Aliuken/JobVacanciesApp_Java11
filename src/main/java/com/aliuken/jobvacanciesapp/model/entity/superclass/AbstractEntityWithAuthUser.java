package com.aliuken.jobvacanciesapp.model.entity.superclass;

import java.util.Objects;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

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

	@Override
	public String toString() {
		final String idString = Objects.toString(this.getId());
		final String email = authUser.getEmail();
		final String firstRegistrationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(this.getFirstRegistrationDateTime());
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(this.getLastModificationDateTime());
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

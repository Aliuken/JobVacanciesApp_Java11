package com.aliuken.jobvacanciesapp.model.entity.superclass;

import java.util.Objects;

import javax.persistence.MappedSuperclass;

import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.superinterface.AbstractEntityWithAuthUserInterface;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class AbstractEntityWithAuthUser extends AbstractEntity implements AbstractEntityWithAuthUserInterface {
	private static final long serialVersionUID = 2906355999654036448L;

	public AbstractEntityWithAuthUser() {
		super();
	}

	@Override
	public String toString() {
		final String idString = this.getIdString();
		final String authUserEmail = this.getAuthUserEmail();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AbstractEntityWithAuthUser [id=", idString, ", authUserEmail=", authUserEmail,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		final AuthUser authUser = this.getAuthUser();

		int result = super.hashCode();
		result = prime * result + Objects.hash(authUser);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		final AuthUser authUser = this.getAuthUser();
		final AbstractEntityWithAuthUser other = (AbstractEntityWithAuthUser) obj;
		return Objects.equals(authUser, other.getAuthUser());
	}
}

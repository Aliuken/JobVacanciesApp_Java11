package com.aliuken.jobvacanciesapp.model.entity.superclass;

import com.aliuken.jobvacanciesapp.model.entity.superinterface.AbstractEntityWithAuthUserInterface;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntityWithAuthUser<T extends AbstractEntity<T>> extends AbstractEntity<T> implements AbstractEntityWithAuthUserInterface {	private static final long serialVersionUID = 2906355999654036448L;

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
}

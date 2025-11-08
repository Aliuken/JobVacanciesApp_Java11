package com.aliuken.jobvacanciesapp.model.entity.superclass;

import com.aliuken.jobvacanciesapp.model.entity.superinterface.AbstractEntityWithJobCompanyInterface;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntityWithJobCompany<T extends AbstractEntity<T>> extends AbstractEntity<T> implements AbstractEntityWithJobCompanyInterface {
	private static final long serialVersionUID = -4031746176102479533L;

	public AbstractEntityWithJobCompany() {
		super();
	}

	@Override
	public String toString() {
		final String idString = this.getIdString();
		final String jobCompanyName = this.getJobCompanyName();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AbstractEntityWithJobCompany [id=", idString, ", jobCompanyName=", jobCompanyName,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}
}

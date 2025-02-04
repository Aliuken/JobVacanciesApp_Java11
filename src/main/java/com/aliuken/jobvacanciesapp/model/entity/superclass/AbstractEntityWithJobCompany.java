package com.aliuken.jobvacanciesapp.model.entity.superclass;

import java.util.Objects;

import javax.persistence.MappedSuperclass;

import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.superinterface.AbstractEntityWithJobCompanyInterface;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class AbstractEntityWithJobCompany extends AbstractEntity implements AbstractEntityWithJobCompanyInterface {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		final JobCompany jobCompany = getJobCompany();

		int result = super.hashCode();
		result = prime * result + Objects.hash(jobCompany);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		final JobCompany jobCompany = getJobCompany();
		final AbstractEntityWithJobCompany other = (AbstractEntityWithJobCompany) obj;
		return Objects.equals(jobCompany, other.getJobCompany());
	}
}

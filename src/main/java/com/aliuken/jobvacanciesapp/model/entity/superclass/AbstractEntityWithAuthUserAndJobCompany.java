package com.aliuken.jobvacanciesapp.model.entity.superclass;

import java.util.Objects;

import javax.persistence.MappedSuperclass;

import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.superinterface.AbstractEntityWithAuthUserInterface;
import com.aliuken.jobvacanciesapp.model.entity.superinterface.AbstractEntityWithJobCompanyInterface;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class AbstractEntityWithAuthUserAndJobCompany extends AbstractEntity implements AbstractEntityWithAuthUserInterface, AbstractEntityWithJobCompanyInterface {
	private static final long serialVersionUID = -6502895741206864193L;

	public AbstractEntityWithAuthUserAndJobCompany() {
		super();
	}

	@Override
	public String toString() {
		final String idString = this.getIdString();
		final String authUserEmail = this.getAuthUserEmail();
		final String jobCompanyName = this.getJobCompanyName();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AbstractEntityWithAuthUser [id=", idString, ", authUserEmail=", authUserEmail, ", jobCompanyName=", jobCompanyName,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		final AuthUser authUser = this.getAuthUser();
		final JobCompany jobCompany = getJobCompany();

		int result = super.hashCode();
		result = prime * result + Objects.hash(authUser, jobCompany);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		final AuthUser authUser = this.getAuthUser();
		final JobCompany jobCompany = getJobCompany();
		final AbstractEntityWithAuthUserAndJobCompany other = (AbstractEntityWithAuthUserAndJobCompany) obj;
		return Objects.equals(authUser, other.getAuthUser()) && Objects.equals(jobCompany, other.getJobCompany());
	}
}

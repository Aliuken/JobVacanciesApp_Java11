package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public enum PageEntityEnum implements Serializable {
	AUTH_USER           ("authUsers"),
	AUTH_USER_CURRICULUM("authUserCurriculums"),
	JOB_CATEGORY        ("jobCategories"),
	JOB_COMPANY         ("jobCompanies"),
	JOB_COMPANY_LOGO    ("jobCompanyLogos"),
	JOB_REQUEST         ("jobRequests"),
	JOB_VACANCY         ("jobVacancies");

	@Getter
	@NotNull
	private final String value;

	private PageEntityEnum(final String value) {
		this.value = value;
	}

	public static PageEntityEnum findByValue(final String value) {
		if(LogicalUtils.isNullOrEmptyString(value)) {
			return null;
		}

		final PageEntityEnum pageEntity = Constants.PARALLEL_STREAM_UTILS.ofEnum(PageEntityEnum.class)
			.filter(pageEntityAux -> value.equals(pageEntityAux.value))
			.findFirst()
			.orElse(null);
		return pageEntity;
	}
}

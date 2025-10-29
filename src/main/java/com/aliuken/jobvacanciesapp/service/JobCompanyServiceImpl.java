package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.repository.JobCompanyRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.database.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobCompanyServiceImpl extends JobCompanyService {
	private static final ExampleMatcher NAME_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsOneField("name");

	@Autowired
	private JobCompanyRepository jobCompanyRepository;

	@Override
	public UpgradedJpaRepository<JobCompany> getEntityRepository() {
		return jobCompanyRepository;
	}

	@Override
	protected Example<JobCompany> getDefaultEntityPageExample(final TableField filterTableField, final String filterValue) {
		final Example<JobCompany> example;
		switch(filterTableField) {
			case JOB_COMPANY_NAME: {
				final JobCompany jobCompanySearch = new JobCompany();
				jobCompanySearch.setName(filterValue);
				example = Example.of(jobCompanySearch, NAME_EXAMPLE_MATCHER);
				break;
			}
			default: {
				throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", filterTableField.name(), "' not supported"));
			}
		}

		return example;
	}

	@Override
	@ServiceMethod
	public JobCompany findByName(final String jobCompanyName) {
		final JobCompany jobCompany = jobCompanyRepository.findByName(jobCompanyName);
		return jobCompany;
	}

	@Override
	public JobCompany getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final JobCompany jobCompany = new JobCompany();
		jobCompany.setId(id);
		jobCompany.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		jobCompany.setLastModificationAuthUser(lastModificationAuthUser);

		return jobCompany;
	}
}

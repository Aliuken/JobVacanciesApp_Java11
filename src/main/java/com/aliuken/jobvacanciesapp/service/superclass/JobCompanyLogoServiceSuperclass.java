package com.aliuken.jobvacanciesapp.service.superclass;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.database.DatabaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Transactional
@Slf4j
public abstract class JobCompanyLogoServiceSuperclass extends AbstractEntityWithJobCompanyServiceSuperclass<JobCompanyLogo> {

	private static final ExampleMatcher JOB_COMPANY_ID_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithExactOneField("jobCompany.id");
	private static final ExampleMatcher JOB_COMPANY_ID_AND_ID_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithExactTwoFields("jobCompany.id", "id");
	private static final ExampleMatcher JOB_COMPANY_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsTwoFields("jobCompany.id", "firstRegistrationAuthUser.email");

	@ServiceMethod
	public AbstractEntityPageWithExceptionDTO<JobCompanyLogo> getJobCompanyJobCompanyLogosPage(final long jobCompanyId, final TableSearchDTO tableSearchDTO, final Pageable pageable) {
		Page<JobCompanyLogo> page;
		Exception exception;
		try {
			if(tableSearchDTO != null) {
				final TableField filterTableField = tableSearchDTO.getFilterTableField();
				final String filterValue = tableSearchDTO.getFilterValue();
				final TableField tableSortingField = tableSearchDTO.getTableSortingField();
				final TableSortingDirection tableSortingDirection = tableSearchDTO.getTableSortingDirection();

				page = this.getJobCompanyJobCompanyLogosPage(jobCompanyId, filterTableField, filterValue, tableSortingField, tableSortingDirection, pageable);
			} else {
				final Example<JobCompanyLogo> example = this.getJobCompanyIdExample(jobCompanyId);
				page = this.findAll(example, pageable);
			}
			exception = null;
		} catch(final Exception e) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(e);
				log.error(StringUtils.getStringJoined("An exception happened when trying to get an entity page. Exception: ", stackTrace));
			}
			page = Page.empty();
			exception = e;
		}

		final AbstractEntityPageWithExceptionDTO<JobCompanyLogo> pageWithExceptionDTO = new AbstractEntityPageWithExceptionDTO<>(page, exception);
		return pageWithExceptionDTO;
	}

	private Page<JobCompanyLogo> getJobCompanyJobCompanyLogosPage(final long jobCompanyId, final TableField filterTableField, final String filterValue, final TableField tableSortingField, final TableSortingDirection tableSortingDirection, final Pageable pageable) {
		final Page<JobCompanyLogo> page;
		if(filterTableField != null && LogicalUtils.isNotNullNorEmptyString(filterValue)) {
			switch(filterTableField) {
				case ID: {
					final JobCompany jobCompany = new JobCompany();
					jobCompany.setId(jobCompanyId);

					final Long entityId;
					try {
						entityId = Long.valueOf(filterValue);
					} catch(final NumberFormatException exception) {
						if(log.isErrorEnabled()) {
							final String stackTrace = ThrowableUtils.getStackTrace(exception);
							log.error(StringUtils.getStringJoined("An exception happened when trying to get an entity page. Exception: ", stackTrace));
						}
						throw new IllegalArgumentException(StringUtils.getStringJoined("The id '", filterValue, "' is not a number"));
					}

					final JobCompanyLogo jobCompanyLogoSearch = new JobCompanyLogo();
					jobCompanyLogoSearch.setId(entityId);
					jobCompanyLogoSearch.setJobCompany(jobCompany);

					final Example<JobCompanyLogo> example = Example.of(jobCompanyLogoSearch, JOB_COMPANY_ID_AND_ID_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
					break;
				}
				case FIRST_REGISTRATION_DATE_TIME: {
					final Specification<JobCompanyLogo> specification = this.equalsJobCompanyIdAndFirstRegistrationDateTime(jobCompanyId, filterValue);
					page = this.findAll(pageable, tableSortingField, tableSortingDirection, specification);
					break;
				}
				case FIRST_REGISTRATION_AUTH_USER_EMAIL: {
					final AuthUser firstRegistrationAuthUser = new AuthUser();
					firstRegistrationAuthUser.setEmail(filterValue);

					final JobCompany jobCompany = new JobCompany();
					jobCompany.setId(jobCompanyId);

					final JobCompanyLogo jobCompanyLogoSearch = new JobCompanyLogo();
					jobCompanyLogoSearch.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
					jobCompanyLogoSearch.setJobCompany(jobCompany);

					final Example<JobCompanyLogo> example = Example.of(jobCompanyLogoSearch, JOB_COMPANY_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
					break;
				}
				default: {
					throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", filterTableField.name(), "' not supported"));
				}
			}
		} else {
			final Example<JobCompanyLogo> example = this.getJobCompanyIdExample(jobCompanyId);
			page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
		}

		return page;
	}

	private Example<JobCompanyLogo> getJobCompanyIdExample(long jobCompanyId) {
		final JobCompany jobCompany = new JobCompany();
		jobCompany.setId(jobCompanyId);

		final JobCompanyLogo jobCompanyLogoSearch = new JobCompanyLogo();
		jobCompanyLogoSearch.setJobCompany(jobCompany);

		final Example<JobCompanyLogo> example = Example.of(jobCompanyLogoSearch, JOB_COMPANY_ID_EXAMPLE_MATCHER);
		return example;
	}

	private Specification<JobCompanyLogo> equalsJobCompanyIdAndFirstRegistrationDateTime(final Long jobCompanyId, final String dateTimeString) {
		return new Specification<JobCompanyLogo>() {
			private static final long serialVersionUID = -2749244145790761484L;

			@Override
			public Predicate toPredicate(final Root<JobCompanyLogo> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
				final String entityFieldName = "jobCompany";
				final String dateTimeFieldName = "firstRegistrationDateTime";
				final Predicate predicate = DatabaseUtils.getEqualsEntityIdAndDateTimePredicate(jobCompanyId, entityFieldName, dateTimeString, dateTimeFieldName, root, criteriaQuery, criteriaBuilder);
				return predicate;
			}
		};
	}
}

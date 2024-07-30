package com.aliuken.jobvacanciesapp.service.superclass;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableOrder;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.DatabaseUtils;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
public abstract class JobRequestServiceSuperclass extends AbstractEntityWithAuthUserServiceSuperclass<JobRequest> {

	private static final ExampleMatcher JOB_VACANCY_ID_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithExactOneField("jobVacancy.id");
	private static final ExampleMatcher JOB_VACANCY_ID_AND_ID_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithExactTwoFields("jobVacancy.id", "id");
	private static final ExampleMatcher JOB_VACANCY_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsTwoFields("jobVacancy.id", "firstRegistrationAuthUser.email");
	private static final ExampleMatcher JOB_VACANCY_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsTwoFields("jobVacancy.id", "lastModificationAuthUser.email");

	@ServiceMethod
	public AbstractEntityPageWithExceptionDTO<JobRequest> getJobVacancyJobRequestsPage(final Long jobVacancyId, final TableSearchDTO tableSearchDTO, final Pageable pageable) {
		Page<JobRequest> page;
		Exception exception;
		try {
			if(tableSearchDTO != null) {
				final TableField tableField = TableField.findByCode(tableSearchDTO.getTableFieldCode());
				final String tableFieldValue = tableSearchDTO.getTableFieldValue();
				final TableOrder tableOrder = TableOrder.findByCode(tableSearchDTO.getTableOrderCode());

				page = this.getJobVacancyJobRequestsPage(jobVacancyId, tableField, tableFieldValue, tableOrder, pageable);
			} else {
				final Example<JobRequest> example = this.getJobVacancyIdExample(jobVacancyId);
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

		final AbstractEntityPageWithExceptionDTO<JobRequest> pageWithExceptionDTO = new AbstractEntityPageWithExceptionDTO<>(page, exception);
		return pageWithExceptionDTO;
	}

	private Page<JobRequest> getJobVacancyJobRequestsPage(final Long jobVacancyId, final TableField tableField, final String tableFieldValue, final TableOrder tableOrder, final Pageable pageable) {
		final Page<JobRequest> page;
		if(tableField != null && LogicalUtils.isNotNullNorEmptyString(tableFieldValue)) {
			switch(tableField) {
				case ID: {
					final JobVacancy jobVacancy = new JobVacancy();
					jobVacancy.setId(jobVacancyId);

					final Long entityId;
					try {
						entityId = Long.valueOf(tableFieldValue);
					} catch(final NumberFormatException exception) {
						if(log.isErrorEnabled()) {
							final String stackTrace = ThrowableUtils.getStackTrace(exception);
							log.error(StringUtils.getStringJoined("An exception happened when trying to get an entity page. Exception: ", stackTrace));
						}
						throw new IllegalArgumentException(StringUtils.getStringJoined("The id '", tableFieldValue, "' is not a number"));
					}

					final JobRequest jobRequestSearch = new JobRequest();
					jobRequestSearch.setId(entityId);
					jobRequestSearch.setJobVacancy(jobVacancy);

					final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_AND_ID_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableOrder);
					break;
				}
				case FIRST_REGISTRATION_DATE_TIME: {
					final Specification<JobRequest> specification = this.equalsJobVacancyIdAndFirstRegistrationDateTime(jobVacancyId, tableFieldValue);
					page = this.findAll(pageable, tableOrder, specification);
					break;
				}
				case FIRST_REGISTRATION_AUTH_USER_EMAIL: {
					final AuthUser firstRegistrationAuthUser = new AuthUser();
					firstRegistrationAuthUser.setEmail(tableFieldValue);

					final JobVacancy jobVacancy = new JobVacancy();
					jobVacancy.setId(jobVacancyId);

					final JobRequest jobRequestSearch = new JobRequest();
					jobRequestSearch.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
					jobRequestSearch.setJobVacancy(jobVacancy);

					final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableOrder);
					break;
				}
				case LAST_MODIFICATION_DATE_TIME: {
					final Specification<JobRequest> specification = this.equalsJobVacancyIdAndLastModificationDateTime(jobVacancyId, tableFieldValue);
					page = this.findAll(pageable, tableOrder, specification);
					break;
				}
				case LAST_MODIFICATION_AUTH_USER_EMAIL: {
					final AuthUser lastModificationAuthUser = new AuthUser();
					lastModificationAuthUser.setEmail(tableFieldValue);

					final JobVacancy jobVacancy = new JobVacancy();
					jobVacancy.setId(jobVacancyId);

					final JobRequest jobRequestSearch = new JobRequest();
					jobRequestSearch.setLastModificationAuthUser(lastModificationAuthUser);
					jobRequestSearch.setJobVacancy(jobVacancy);

					final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableOrder);
					break;
				}
				default: {
					throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", tableField.name(), "' not supported"));
				}
			}
		} else {
			final Example<JobRequest> example = this.getJobVacancyIdExample(jobVacancyId);
			page = this.findAll(example, pageable, tableOrder);
		}

		return page;
	}

	private Example<JobRequest> getJobVacancyIdExample(Long jobVacancyId) {
		final JobVacancy jobVacancy = new JobVacancy();
		jobVacancy.setId(jobVacancyId);

		final JobRequest jobRequestSearch = new JobRequest();
		jobRequestSearch.setJobVacancy(jobVacancy);

		final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_EXAMPLE_MATCHER);
		return example;
	}

	private Specification<JobRequest> equalsJobVacancyIdAndFirstRegistrationDateTime(final Long jobVacancyId, final String dateTimeString) {
		return new Specification<JobRequest>() {
			private static final long serialVersionUID = -2253513074973647406L;

			@Override
			public Predicate toPredicate(final Root<JobRequest> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
				final String entityFieldName = "jobVacancy";
				final String dateTimeFieldName = "firstRegistrationDateTime";
				final Predicate predicate = DatabaseUtils.getEqualsEntityIdAndDateTimePredicate(jobVacancyId, entityFieldName, dateTimeString, dateTimeFieldName, root, criteriaQuery, criteriaBuilder);
				return predicate;
			}
		};
	}

	private Specification<JobRequest> equalsJobVacancyIdAndLastModificationDateTime(final Long jobVacancyId, final String dateTimeString) {
		return new Specification<JobRequest>() {
			private static final long serialVersionUID = -5276525512105180369L;

			@Override
			public Predicate toPredicate(final Root<JobRequest> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
				final String entityFieldName = "jobVacancy";
				final String dateTimeFieldName = "lastModificationDateTime";
				final Predicate predicate = DatabaseUtils.getEqualsEntityIdAndDateTimePredicate(jobVacancyId, entityFieldName, dateTimeString, dateTimeFieldName, root, criteriaQuery, criteriaBuilder);
				return predicate;
			}
		};
	}
}

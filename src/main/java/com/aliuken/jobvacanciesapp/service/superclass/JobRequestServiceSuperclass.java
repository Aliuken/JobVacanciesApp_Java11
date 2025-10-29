package com.aliuken.jobvacanciesapp.service.superclass;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
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
public abstract class JobRequestServiceSuperclass extends AbstractEntityWithAuthUserAndJobCompanyServiceSuperclass<JobRequest> {

	private static final ExampleMatcher JOB_VACANCY_ID_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithExactOneField("jobVacancy.id");
	private static final ExampleMatcher JOB_VACANCY_ID_AND_ID_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithExactTwoFields("jobVacancy.id", "id");
	private static final ExampleMatcher JOB_VACANCY_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsTwoFields("jobVacancy.id", "firstRegistrationAuthUser.email");
	private static final ExampleMatcher JOB_VACANCY_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsTwoFields("jobVacancy.id", "lastModificationAuthUser.email");
	private static final ExampleMatcher JOB_VACANCY_ID_AND_AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsTwoFields("jobVacancy.id", "authUser.email");
	private static final ExampleMatcher JOB_VACANCY_ID_AND_AUTH_USER_NAME_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsTwoFields("jobVacancy.id", "authUser.name");
	private static final ExampleMatcher JOB_VACANCY_ID_AND_AUTH_USER_SURNAMES_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsTwoFields("jobVacancy.id", "authUser.surnames");

	@ServiceMethod
	public AbstractEntityPageWithExceptionDTO<JobRequest> getJobVacancyJobRequestsPage(final Long jobVacancyId, final TableSearchDTO tableSearchDTO, final Pageable pageable) {
		Page<JobRequest> page;
		Exception exception;
		try {
			if(tableSearchDTO != null) {
				final TableField filterTableField = tableSearchDTO.getFilterTableField();
				final String filterValue = tableSearchDTO.getFilterValue();
				final TableField tableSortingField = tableSearchDTO.getTableSortingField();
				final TableSortingDirection tableSortingDirection = tableSearchDTO.getTableSortingDirection();

				page = this.getJobVacancyJobRequestsPage(jobVacancyId, filterTableField, filterValue, tableSortingField, tableSortingDirection, pageable);
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

	private Page<JobRequest> getJobVacancyJobRequestsPage(final Long jobVacancyId, final TableField filterTableField, final String filterValue, final TableField tableSortingField, final TableSortingDirection tableSortingDirection, final Pageable pageable) {
		final Page<JobRequest> page;
		if(filterTableField != null && LogicalUtils.isNotNullNorEmptyString(filterValue)) {
			switch(filterTableField) {
				case ID: {
					final JobVacancy jobVacancy = new JobVacancy();
					jobVacancy.setId(jobVacancyId);

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

					final JobRequest jobRequestSearch = new JobRequest();
					jobRequestSearch.setId(entityId);
					jobRequestSearch.setJobVacancy(jobVacancy);

					final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_AND_ID_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
					break;
				}
				case FIRST_REGISTRATION_DATE_TIME: {
					final Specification<JobRequest> specification = this.equalsJobVacancyIdAndFirstRegistrationDateTime(jobVacancyId, filterValue);
					page = this.findAll(pageable, tableSortingField, tableSortingDirection, specification);
					break;
				}
				case FIRST_REGISTRATION_AUTH_USER_EMAIL: {
					final AuthUser firstRegistrationAuthUser = new AuthUser();
					firstRegistrationAuthUser.setEmail(filterValue);

					final JobVacancy jobVacancy = new JobVacancy();
					jobVacancy.setId(jobVacancyId);

					final JobRequest jobRequestSearch = new JobRequest();
					jobRequestSearch.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
					jobRequestSearch.setJobVacancy(jobVacancy);

					final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
					break;
				}
				case LAST_MODIFICATION_DATE_TIME: {
					final Specification<JobRequest> specification = this.equalsJobVacancyIdAndLastModificationDateTime(jobVacancyId, filterValue);
					page = this.findAll(pageable, tableSortingField, tableSortingDirection, specification);
					break;
				}
				case LAST_MODIFICATION_AUTH_USER_EMAIL: {
					final AuthUser lastModificationAuthUser = new AuthUser();
					lastModificationAuthUser.setEmail(filterValue);

					final JobVacancy jobVacancy = new JobVacancy();
					jobVacancy.setId(jobVacancyId);

					final JobRequest jobRequestSearch = new JobRequest();
					jobRequestSearch.setLastModificationAuthUser(lastModificationAuthUser);
					jobRequestSearch.setJobVacancy(jobVacancy);

					final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
					break;
				}
				case AUTH_USER_EMAIL: {
					final AuthUser authUser = new AuthUser();
					authUser.setEmail(filterValue);

					final JobVacancy jobVacancy = new JobVacancy();
					jobVacancy.setId(jobVacancyId);

					final JobRequest jobRequestSearch = new JobRequest();
					jobRequestSearch.setAuthUser(authUser);
					jobRequestSearch.setJobVacancy(jobVacancy);

					final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_AND_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
					break;
				}
				case AUTH_USER_NAME: {
					final AuthUser authUser = new AuthUser();
					authUser.setName(filterValue);

					final JobVacancy jobVacancy = new JobVacancy();
					jobVacancy.setId(jobVacancyId);

					final JobRequest jobRequestSearch = new JobRequest();
					jobRequestSearch.setAuthUser(authUser);
					jobRequestSearch.setJobVacancy(jobVacancy);

					final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_AND_AUTH_USER_NAME_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
					break;
				}
				case AUTH_USER_SURNAMES: {
					final AuthUser authUser = new AuthUser();
					authUser.setSurnames(filterValue);

					final JobVacancy jobVacancy = new JobVacancy();
					jobVacancy.setId(jobVacancyId);

					final JobRequest jobRequestSearch = new JobRequest();
					jobRequestSearch.setAuthUser(authUser);
					jobRequestSearch.setJobVacancy(jobVacancy);

					final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_AND_AUTH_USER_SURNAMES_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
					break;
				}
				default: {
					throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", filterTableField.name(), "' not supported"));
				}
			}
		} else {
			final Example<JobRequest> example = this.getJobVacancyIdExample(jobVacancyId);
			page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
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

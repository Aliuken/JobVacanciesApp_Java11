package com.aliuken.jobvacanciesapp.service.superinterface;

import java.time.LocalDateTime;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.AbstractEntityPageWithException;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.dto.TableField;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.util.StringUtils;

public interface JobRequestExtraServiceInterface extends AbstractEntityWithAuthUserExtraServiceInterface<JobRequest> {
	public static final Logger log = LoggerFactory.getLogger(JobRequestExtraServiceInterface.class);

	public static final ExampleMatcher JOB_VACANCY_ID_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("jobVacancy.id", ExampleMatcher.GenericPropertyMatchers.exact());
	public static final ExampleMatcher JOB_VACANCY_ID_AND_ID_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("jobVacancy.id", ExampleMatcher.GenericPropertyMatchers.exact()).withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact());
	public static final ExampleMatcher JOB_VACANCY_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("jobVacancy.id", ExampleMatcher.GenericPropertyMatchers.exact()).withMatcher("firstRegistrationAuthUser.email", ExampleMatcher.GenericPropertyMatchers.contains());
	public static final ExampleMatcher JOB_VACANCY_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("jobVacancy.id", ExampleMatcher.GenericPropertyMatchers.exact()).withMatcher("lastModificationAuthUser.email", ExampleMatcher.GenericPropertyMatchers.contains());

	default AbstractEntityPageWithException<JobRequest> getJobVacancyJobRequestsPage(final Long jobVacancyId, final TableSearchDTO tableSearchDTO, final Pageable pageable) {
		Page<JobRequest> page;
		Exception exception = null;
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
		} catch(Exception e) {
			if(log.isErrorEnabled()) {
				log.error("An exception happened when trying to get an entity page", e);
			}
			page = Page.empty();
			exception = e;
		}

		AbstractEntityPageWithException<JobRequest> abstractEntityPageWithException = new AbstractEntityPageWithException<>();
		abstractEntityPageWithException.setPage(page);
		abstractEntityPageWithException.setException(exception);

		return abstractEntityPageWithException;
	}

	private Page<JobRequest> getJobVacancyJobRequestsPage(final Long jobVacancyId, final TableField tableField, final String tableFieldValue, final TableOrder tableOrder, final Pageable pageable) {
		final Page<JobRequest> page;
		if(tableField != null && tableFieldValue != null && !tableFieldValue.isEmpty()) {
			switch(tableField) {
				case ID: {
					final JobVacancy jobVacancy = new JobVacancy();
					jobVacancy.setId(jobVacancyId);

					final Long entityId;
					try {
						entityId = Long.valueOf(tableFieldValue);
					} catch(NumberFormatException e) {
						if(log.isErrorEnabled()) {
							log.error("An exception happened when trying to get an entity page", e);
						}
						throw new IllegalArgumentException(StringUtils.getStringJoined("The id '", tableFieldValue, "' is not a number"));
					}

					final JobRequest jobRequestSearch = new JobRequest();
					jobRequestSearch.setId(entityId);
					jobRequestSearch.setJobVacancy(jobVacancy);

					final Example<JobRequest> example = this.getJobVacancyIdAndIdExample(jobRequestSearch);
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

					final Example<JobRequest> example = this.getJobVacancyIdAndFirstRegistrationAuthUserEmailExample(jobRequestSearch);
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

					final Example<JobRequest> example = this.getJobVacancyIdAndLastModificationAuthUserEmailExample(jobRequestSearch);
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

	default Example<JobRequest> getJobVacancyIdExample(Long jobVacancyId){
		final JobVacancy jobVacancy = new JobVacancy();
		jobVacancy.setId(jobVacancyId);

		final JobRequest jobRequestSearch = new JobRequest();
		jobRequestSearch.setJobVacancy(jobVacancy);

		final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_EXAMPLE_MATCHER);
		return example;
	}

	default Example<JobRequest> getJobVacancyIdAndIdExample(JobRequest jobRequestSearch){
		final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_AND_ID_EXAMPLE_MATCHER);
		return example;
	}

	default Example<JobRequest> getJobVacancyIdAndFirstRegistrationAuthUserEmailExample(JobRequest jobRequestSearch){
		final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
		return example;
	}

	default Example<JobRequest> getJobVacancyIdAndLastModificationAuthUserEmailExample(JobRequest jobRequestSearch){
		final Example<JobRequest> example = Example.of(jobRequestSearch, JOB_VACANCY_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
		return example;
	}

	default Specification<JobRequest> equalsJobVacancyIdAndFirstRegistrationDateTime(final Long jobVacancyId, final String dateTimeString){
        return new Specification<JobRequest>() {
			private static final long serialVersionUID = -2253513074973647406L;

			@Override
            public Predicate toPredicate(Root<JobRequest> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				final Predicate predicate;
				if(dateTimeString == null || Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD.equals(dateTimeString)) {
					final Expression<Long> jobVacancyIdExpression = root.get("jobVacancy").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(jobVacancyIdExpression, jobVacancyId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("firstRegistrationDateTime").as(LocalDateTime.class);
					Predicate predicate2 = criteriaBuilder.isNull(localDateTimeExpression);

					predicate = criteriaBuilder.and(predicate1, predicate2);
				} else {
					final Expression<Long> jobVacancyIdExpression = root.get("jobVacancy").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(jobVacancyIdExpression, jobVacancyId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("firstRegistrationDateTime").as(LocalDateTime.class);
					final Expression<String> stringExpression = criteriaBuilder.function("DATE_FORMAT", String.class, localDateTimeExpression, criteriaBuilder.literal("%d-%m-%Y %H:%i:%s"));
					Predicate predicate2 = criteriaBuilder.like(stringExpression, dateTimeString + "%");

					predicate = criteriaBuilder.and(predicate1, predicate2);
				}
                return predicate;
            }
        };
    }

	default Specification<JobRequest> equalsJobVacancyIdAndLastModificationDateTime(final Long jobVacancyId, final String dateTimeString){
        return new Specification<JobRequest>() {
			private static final long serialVersionUID = -5276525512105180369L;

			@Override
            public Predicate toPredicate(Root<JobRequest> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final Predicate predicate;
				if(dateTimeString == null || Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD.equals(dateTimeString)) {
					final Expression<Long> jobVacancyIdExpression = root.get("jobVacancy").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(jobVacancyIdExpression, jobVacancyId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("lastModificationDateTime").as(LocalDateTime.class);
					Predicate predicate2 = criteriaBuilder.isNull(localDateTimeExpression);

					predicate = criteriaBuilder.and(predicate1, predicate2);
				} else {
					final Expression<Long> jobVacancyIdExpression = root.get("jobVacancy").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(jobVacancyIdExpression, jobVacancyId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("lastModificationDateTime").as(LocalDateTime.class);
					final Expression<String> stringExpression = criteriaBuilder.function("DATE_FORMAT", String.class, localDateTimeExpression, criteriaBuilder.literal("%d-%m-%Y %H:%i:%s"));
					Predicate predicate2 = criteriaBuilder.like(stringExpression, dateTimeString + "%");

					predicate = criteriaBuilder.and(predicate1, predicate2);
				}
                return predicate;
            }
        };
    }
}

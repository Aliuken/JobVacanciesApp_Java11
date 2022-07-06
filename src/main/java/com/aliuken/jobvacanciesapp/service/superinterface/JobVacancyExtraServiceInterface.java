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
import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.dto.TableField;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.util.StringUtils;

public interface JobVacancyExtraServiceInterface extends AbstractEntityExtraServiceInterface<JobVacancy> {
	public static final Logger log = LoggerFactory.getLogger(JobVacancyExtraServiceInterface.class);
	
	public static final ExampleMatcher JOB_CATEGORY_ID_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("jobCategory.id", ExampleMatcher.GenericPropertyMatchers.exact());
	public static final ExampleMatcher JOB_CATEGORY_ID_AND_ID_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("jobCategory.id", ExampleMatcher.GenericPropertyMatchers.exact()).withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact());
	public static final ExampleMatcher JOB_CATEGORY_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("jobCategory.id", ExampleMatcher.GenericPropertyMatchers.exact()).withMatcher("firstRegistrationAuthUser.email", ExampleMatcher.GenericPropertyMatchers.contains());
	public static final ExampleMatcher JOB_CATEGORY_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("jobCategory.id", ExampleMatcher.GenericPropertyMatchers.exact()).withMatcher("lastModificationAuthUser.email", ExampleMatcher.GenericPropertyMatchers.contains());

	public static final ExampleMatcher JOB_COMPANY_ID_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("jobCompany.id", ExampleMatcher.GenericPropertyMatchers.exact());
	public static final ExampleMatcher JOB_COMPANY_ID_AND_ID_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("jobCompany.id", ExampleMatcher.GenericPropertyMatchers.exact()).withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact());
	public static final ExampleMatcher JOB_COMPANY_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("jobCompany.id", ExampleMatcher.GenericPropertyMatchers.exact()).withMatcher("firstRegistrationAuthUser.email", ExampleMatcher.GenericPropertyMatchers.contains());
	public static final ExampleMatcher JOB_COMPANY_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("jobCompany.id", ExampleMatcher.GenericPropertyMatchers.exact()).withMatcher("lastModificationAuthUser.email", ExampleMatcher.GenericPropertyMatchers.contains());
	
	default AbstractEntityPageWithException<JobVacancy> getJobCategoryJobVacanciesPage(final long jobCategoryId, final TableSearchDTO tableSearchDTO, final Pageable pageable) {
		Page<JobVacancy> page;
		Exception exception = null;
		try {
			if(tableSearchDTO != null) {
				final TableField tableField = TableField.findByCode(tableSearchDTO.getTableFieldCode());
				final String tableFieldValue = tableSearchDTO.getTableFieldValue();
				final TableOrder tableOrder = TableOrder.findByCode(tableSearchDTO.getTableOrderCode());

				page = this.getJobCategoryJobVacanciesPage(jobCategoryId, tableField, tableFieldValue, tableOrder, pageable);
			} else {
				final Example<JobVacancy> example = this.getJobCategoryIdExample(jobCategoryId);
				page = this.findAll(pageable, example);
			}
		} catch(Exception e) {
			if(log.isErrorEnabled()) {
				log.error("An exception happened when trying to get an entity page", e);
			}
			page = Page.empty();
			exception = e;
		}

		AbstractEntityPageWithException<JobVacancy> abstractEntityPageWithException = new AbstractEntityPageWithException<>();
		abstractEntityPageWithException.setPage(page);
		abstractEntityPageWithException.setException(exception);

		return abstractEntityPageWithException;
	}
	
	private Page<JobVacancy> getJobCategoryJobVacanciesPage(final long jobCategoryId, final TableField tableField, final String tableFieldValue, final TableOrder tableOrder, final Pageable pageable) {
		final Page<JobVacancy> page;
		if(tableField != null && tableFieldValue != null && !tableFieldValue.isEmpty()) {
			switch(tableField) {
				case ID: {
					final JobCategory jobCategory = new JobCategory();
					jobCategory.setId(jobCategoryId);

					final Long entityId;
					try {
						entityId = Long.valueOf(tableFieldValue);
					} catch(NumberFormatException e) {
						if(log.isErrorEnabled()) {
							log.error("An exception happened when trying to get an entity page", e);
						}
						throw new IllegalArgumentException(StringUtils.getStringJoined("The id '", tableFieldValue, "' is not a number"));
					}

					final JobVacancy jobVacancySearch = new JobVacancy();
					jobVacancySearch.setId(entityId);
					jobVacancySearch.setJobCategory(jobCategory);

					final Example<JobVacancy> example = this.getJobCategoryIdAndIdExample(jobVacancySearch);
					page = this.findAll(pageable, tableOrder, example);
					break;
				}
				case FIRST_REGISTRATION_DATE_TIME: {
					final Specification<JobVacancy> specification = this.equalsJobCategoryIdAndFirstRegistrationDateTime(jobCategoryId, tableFieldValue);
					page = this.findAll(pageable, tableOrder, specification);
					break;
				}
				case FIRST_REGISTRATION_AUTH_USER_EMAIL: {
					final AuthUser firstRegistrationAuthUser = new AuthUser();
					firstRegistrationAuthUser.setEmail(tableFieldValue);
					
					final JobCategory jobCategory = new JobCategory();
					jobCategory.setId(jobCategoryId);
					
					final JobVacancy jobVacancySearch = new JobVacancy();
					jobVacancySearch.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
					jobVacancySearch.setJobCategory(jobCategory);

					final Example<JobVacancy> example = this.getJobCategoryIdAndFirstRegistrationAuthUserEmailExample(jobVacancySearch);
					page = this.findAll(pageable, tableOrder, example);
					break;
				}
				case LAST_MODIFICATION_DATE_TIME: {
					final Specification<JobVacancy> specification = this.equalsJobCategoryIdAndLastModificationDateTime(jobCategoryId, tableFieldValue);
					page = this.findAll(pageable, tableOrder, specification);
					break;
				}
				case LAST_MODIFICATION_AUTH_USER_EMAIL: {
					final AuthUser lastModificationAuthUser = new AuthUser();
					lastModificationAuthUser.setEmail(tableFieldValue);
					
					final JobCategory jobCategory = new JobCategory();
					jobCategory.setId(jobCategoryId);
					
					final JobVacancy jobVacancySearch = new JobVacancy();
					jobVacancySearch.setLastModificationAuthUser(lastModificationAuthUser);
					jobVacancySearch.setJobCategory(jobCategory);

					final Example<JobVacancy> example = this.getJobCategoryIdAndLastModificationAuthUserEmailExample(jobVacancySearch);
					page = this.findAll(pageable, tableOrder, example);
					break;
				}
				default: {
					throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", tableField.name(), "' not supported"));
				}
			}
		} else {
			final Example<JobVacancy> example = this.getJobCategoryIdExample(jobCategoryId);
			page = this.findAll(pageable, tableOrder, example);
		}
		
		return page;
	}
	
	default AbstractEntityPageWithException<JobVacancy> getJobCompanyJobVacanciesPage(final long jobCompanyId, final TableSearchDTO tableSearchDTO, final Pageable pageable) {
		Page<JobVacancy> page;
		Exception exception = null;
		try {
			if(tableSearchDTO != null) {
				final TableField tableField = TableField.findByCode(tableSearchDTO.getTableFieldCode());
				final String tableFieldValue = tableSearchDTO.getTableFieldValue();
				final TableOrder tableOrder = TableOrder.findByCode(tableSearchDTO.getTableOrderCode());

				page = this.getJobCompanyJobVacanciesPage(jobCompanyId, tableField, tableFieldValue, tableOrder, pageable);
			} else {
				final Example<JobVacancy> example = this.getJobCompanyIdExample(jobCompanyId);
				page = this.findAll(pageable, example);
			}
		} catch(Exception e) {
			if(log.isErrorEnabled()) {
				log.error("An exception happened when trying to get an entity page", e);
			}
			page = Page.empty();
			exception = e;
		}

		AbstractEntityPageWithException<JobVacancy> abstractEntityPageWithException = new AbstractEntityPageWithException<>();
		abstractEntityPageWithException.setPage(page);
		abstractEntityPageWithException.setException(exception);

		return abstractEntityPageWithException;
	}
	
	private Page<JobVacancy> getJobCompanyJobVacanciesPage(final long jobCompanyId, final TableField tableField, final String tableFieldValue, final TableOrder tableOrder, final Pageable pageable) {
		final Page<JobVacancy> page;
		if(tableField != null && tableFieldValue != null && !tableFieldValue.isEmpty()) {
			switch(tableField) {
				case ID: {
					final JobCompany jobCompany = new JobCompany();
					jobCompany.setId(jobCompanyId);

					final Long entityId;
					try {
						entityId = Long.valueOf(tableFieldValue);
					} catch(NumberFormatException e) {
						if(log.isErrorEnabled()) {
							log.error("An exception happened when trying to get an entity page", e);
						}
						throw new IllegalArgumentException(StringUtils.getStringJoined("The id '", tableFieldValue, "' is not a number"));
					}

					final JobVacancy jobVacancySearch = new JobVacancy();
					jobVacancySearch.setId(entityId);
					jobVacancySearch.setJobCompany(jobCompany);

					final Example<JobVacancy> example = this.getJobCompanyIdAndIdExample(jobVacancySearch);
					page = this.findAll(pageable, tableOrder, example);
					break;
				}
				case FIRST_REGISTRATION_DATE_TIME: {
					final Specification<JobVacancy> specification = this.equalsJobCompanyIdAndFirstRegistrationDateTime(jobCompanyId, tableFieldValue);
					page = this.findAll(pageable, tableOrder, specification);
					break;
				}
				case FIRST_REGISTRATION_AUTH_USER_EMAIL: {
					final AuthUser firstRegistrationAuthUser = new AuthUser();
					firstRegistrationAuthUser.setEmail(tableFieldValue);
					
					final JobCompany jobCompany = new JobCompany();
					jobCompany.setId(jobCompanyId);
					
					final JobVacancy jobVacancySearch = new JobVacancy();
					jobVacancySearch.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
					jobVacancySearch.setJobCompany(jobCompany);

					final Example<JobVacancy> example = this.getJobCompanyIdAndFirstRegistrationAuthUserEmailExample(jobVacancySearch);
					page = this.findAll(pageable, tableOrder, example);
					break;
				}
				case LAST_MODIFICATION_DATE_TIME: {
					final Specification<JobVacancy> specification = this.equalsJobCompanyIdAndLastModificationDateTime(jobCompanyId, tableFieldValue);
					page = this.findAll(pageable, tableOrder, specification);
					break;
				}
				case LAST_MODIFICATION_AUTH_USER_EMAIL: {
					final AuthUser lastModificationAuthUser = new AuthUser();
					lastModificationAuthUser.setEmail(tableFieldValue);
					
					final JobCompany jobCompany = new JobCompany();
					jobCompany.setId(jobCompanyId);
					
					final JobVacancy jobVacancySearch = new JobVacancy();
					jobVacancySearch.setLastModificationAuthUser(lastModificationAuthUser);
					jobVacancySearch.setJobCompany(jobCompany);

					final Example<JobVacancy> example = this.getJobCompanyIdAndLastModificationAuthUserEmailExample(jobVacancySearch);
					page = this.findAll(pageable, tableOrder, example);
					break;
				}
				default: {
					throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", tableField.name(), "' not supported"));
				}
			}
		} else {
			final Example<JobVacancy> example = this.getJobCompanyIdExample(jobCompanyId);
			page = this.findAll(pageable, tableOrder, example);
		}
		
		return page;
	}

	default Example<JobVacancy> getJobCategoryIdExample(long jobCategoryId){
		final JobCategory jobCategory = new JobCategory();
		jobCategory.setId(jobCategoryId);

		final JobVacancy jobVacancySearch = new JobVacancy();
		jobVacancySearch.setJobCategory(jobCategory);
		
		final Example<JobVacancy> example = Example.of(jobVacancySearch, JOB_CATEGORY_ID_EXAMPLE_MATCHER);
		return example;
	}

	default Example<JobVacancy> getJobCategoryIdAndIdExample(JobVacancy jobVacancySearch){
		final Example<JobVacancy> example = Example.of(jobVacancySearch, JOB_CATEGORY_ID_AND_ID_EXAMPLE_MATCHER);
		return example;
	}
	
	default Example<JobVacancy> getJobCategoryIdAndFirstRegistrationAuthUserEmailExample(JobVacancy jobVacancySearch){
		final Example<JobVacancy> example = Example.of(jobVacancySearch, JOB_CATEGORY_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
		return example;
	}
	
	default Example<JobVacancy> getJobCategoryIdAndLastModificationAuthUserEmailExample(JobVacancy jobVacancySearch){
		final Example<JobVacancy> example = Example.of(jobVacancySearch, JOB_CATEGORY_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
		return example;
	}
	
	default Example<JobVacancy> getJobCompanyIdExample(long jobCompanyId){
		final JobCompany jobCompany = new JobCompany();
		jobCompany.setId(jobCompanyId);

		final JobVacancy jobVacancySearch = new JobVacancy();
		jobVacancySearch.setJobCompany(jobCompany);
		
		final Example<JobVacancy> example = Example.of(jobVacancySearch, JOB_COMPANY_ID_EXAMPLE_MATCHER);
		return example;
	}

	default Example<JobVacancy> getJobCompanyIdAndIdExample(JobVacancy jobVacancySearch){
		final Example<JobVacancy> example = Example.of(jobVacancySearch, JOB_COMPANY_ID_AND_ID_EXAMPLE_MATCHER);
		return example;
	}
	
	default Example<JobVacancy> getJobCompanyIdAndFirstRegistrationAuthUserEmailExample(JobVacancy jobVacancySearch){
		final Example<JobVacancy> example = Example.of(jobVacancySearch, JOB_COMPANY_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
		return example;
	}
	
	default Example<JobVacancy> getJobCompanyIdAndLastModificationAuthUserEmailExample(JobVacancy jobVacancySearch){
		final Example<JobVacancy> example = Example.of(jobVacancySearch, JOB_COMPANY_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
		return example;
	}

	default Specification<JobVacancy> equalsJobCategoryIdAndFirstRegistrationDateTime(final Long jobCategoryId, final String dateTimeString){
        return new Specification<JobVacancy>() {
			private static final long serialVersionUID = 5483339128935088308L;

			@Override
            public Predicate toPredicate(Root<JobVacancy> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				final Predicate predicate;
				if(dateTimeString == null || Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD.equals(dateTimeString)) {
					final Expression<Long> jobCategoryIdExpression = root.get("jobCategory").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(jobCategoryIdExpression, jobCategoryId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("firstRegistrationDateTime").as(LocalDateTime.class);
					Predicate predicate2 = criteriaBuilder.isNull(localDateTimeExpression);

					predicate = criteriaBuilder.and(predicate1, predicate2);
				} else {
					final Expression<Long> jobCategoryIdExpression = root.get("jobCategory").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(jobCategoryIdExpression, jobCategoryId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("firstRegistrationDateTime").as(LocalDateTime.class);
					final Expression<String> stringExpression = criteriaBuilder.function("DATE_FORMAT", String.class, localDateTimeExpression, criteriaBuilder.literal("%d-%m-%Y %H:%i:%s"));
					Predicate predicate2 = criteriaBuilder.like(stringExpression, dateTimeString + "%");

					predicate = criteriaBuilder.and(predicate1, predicate2);
				}
                return predicate;
            }
        };
    }

	default Specification<JobVacancy> equalsJobCategoryIdAndLastModificationDateTime(final Long jobCategoryId, final String dateTimeString){
        return new Specification<JobVacancy>() {
			private static final long serialVersionUID = 8689777778809761337L;

			@Override
            public Predicate toPredicate(Root<JobVacancy> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final Predicate predicate;
				if(dateTimeString == null || Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD.equals(dateTimeString)) {
					final Expression<Long> jobCategoryIdExpression = root.get("jobCategory").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(jobCategoryIdExpression, jobCategoryId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("lastModificationDateTime").as(LocalDateTime.class);
					Predicate predicate2 = criteriaBuilder.isNull(localDateTimeExpression);

					predicate = criteriaBuilder.and(predicate1, predicate2);
				} else {
					final Expression<Long> jobCategoryIdExpression = root.get("jobCategory").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(jobCategoryIdExpression, jobCategoryId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("lastModificationDateTime").as(LocalDateTime.class);
					final Expression<String> stringExpression = criteriaBuilder.function("DATE_FORMAT", String.class, localDateTimeExpression, criteriaBuilder.literal("%d-%m-%Y %H:%i:%s"));
					Predicate predicate2 = criteriaBuilder.like(stringExpression, dateTimeString + "%");

					predicate = criteriaBuilder.and(predicate1, predicate2);
				}
                return predicate;
            }
        };
    }

	default Specification<JobVacancy> equalsJobCompanyIdAndFirstRegistrationDateTime(final Long jobCompanyId, final String dateTimeString){
        return new Specification<JobVacancy>() {
			private static final long serialVersionUID = 5483339128935088308L;

			@Override
            public Predicate toPredicate(Root<JobVacancy> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				final Predicate predicate;
				if(dateTimeString == null || Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD.equals(dateTimeString)) {
					final Expression<Long> jobCompanyIdExpression = root.get("jobCompany").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(jobCompanyIdExpression, jobCompanyId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("firstRegistrationDateTime").as(LocalDateTime.class);
					Predicate predicate2 = criteriaBuilder.isNull(localDateTimeExpression);

					predicate = criteriaBuilder.and(predicate1, predicate2);
				} else {
					final Expression<Long> jobCompanyIdExpression = root.get("jobCompany").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(jobCompanyIdExpression, jobCompanyId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("firstRegistrationDateTime").as(LocalDateTime.class);
					final Expression<String> stringExpression = criteriaBuilder.function("DATE_FORMAT", String.class, localDateTimeExpression, criteriaBuilder.literal("%d-%m-%Y %H:%i:%s"));
					Predicate predicate2 = criteriaBuilder.like(stringExpression, dateTimeString + "%");

					predicate = criteriaBuilder.and(predicate1, predicate2);
				}
                return predicate;
            }
        };
    }

	default Specification<JobVacancy> equalsJobCompanyIdAndLastModificationDateTime(final Long jobCompanyId, final String dateTimeString){
        return new Specification<JobVacancy>() {
			private static final long serialVersionUID = 8689777778809761337L;

			@Override
            public Predicate toPredicate(Root<JobVacancy> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final Predicate predicate;
				if(dateTimeString == null || Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD.equals(dateTimeString)) {
					final Expression<Long> jobCompanyIdExpression = root.get("jobCompany").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(jobCompanyIdExpression, jobCompanyId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("lastModificationDateTime").as(LocalDateTime.class);
					Predicate predicate2 = criteriaBuilder.isNull(localDateTimeExpression);

					predicate = criteriaBuilder.and(predicate1, predicate2);
				} else {
					final Expression<Long> jobCompanyIdExpression = root.get("jobCompany").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(jobCompanyIdExpression, jobCompanyId);

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

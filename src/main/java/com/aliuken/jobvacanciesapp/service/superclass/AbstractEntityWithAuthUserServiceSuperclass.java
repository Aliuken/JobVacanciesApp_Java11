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
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSorting;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntityWithAuthUser;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.database.DatabaseUtils;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
public abstract class AbstractEntityWithAuthUserServiceSuperclass<T extends AbstractEntityWithAuthUser> extends AbstractEntityServiceSuperclass<T> {

	private static final ExampleMatcher AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsOneField("authUser.email");
	private static final ExampleMatcher AUTH_USER_NAME_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsOneField("authUser.name");
	private static final ExampleMatcher AUTH_USER_SURNAMES_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsOneField("authUser.surnames");

	private static final ExampleMatcher AUTH_USER_ID_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithExactOneField("authUser.id");
	private static final ExampleMatcher AUTH_USER_ID_AND_ID_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithExactTwoFields("authUser.id", "id");
	private static final ExampleMatcher AUTH_USER_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsTwoFields("authUser.id", "firstRegistrationAuthUser.email");
	private static final ExampleMatcher AUTH_USER_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsTwoFields("authUser.id", "lastModificationAuthUser.email");

	public abstract T getNewEntityWithAuthUserEmail(String authUserEmail);
	public abstract T getNewEntityWithAuthUserName(String authUserName);
	public abstract T getNewEntityWithAuthUserSurnames(String authUserSurnames);

	@Override
	@ServiceMethod
	public AbstractEntityPageWithExceptionDTO<T> getEntityPage(final TableSearchDTO tableSearchDTO, final Pageable pageable) {
		Page<T> page;
		Exception exception;
		try {
			if(tableSearchDTO != null) {
				final TableField tableField = TableField.findByCode(tableSearchDTO.getTableFieldCode());
				final String tableFieldValue = tableSearchDTO.getTableFieldValue();
				final TableSorting tableSorting = TableSorting.findByCode(tableSearchDTO.getTableSortingCode());

				page = this.getEntityPage(tableField, tableFieldValue, tableSorting, pageable);
			} else {
				page = this.findAll(pageable);
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

		final AbstractEntityPageWithExceptionDTO<T> pageWithExceptionDTO = new AbstractEntityPageWithExceptionDTO<>(page, exception);
		return pageWithExceptionDTO;
	}

	private Page<T> getEntityPage(final TableField tableField, final String tableFieldValue, final TableSorting tableSorting, final Pageable pageable) {
		final Page<T> page;
		if(tableField != null && LogicalUtils.isNotNullNorEmptyString(tableFieldValue)) {
			switch(tableField) {
				case ID: {
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

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(entityId, null, null);
					final Example<T> example = Example.of(abstractEntitySearch, ID_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSorting);
					break;
				}
				case FIRST_REGISTRATION_DATE_TIME: {
					final Specification<T> specification = this.equalsFirstRegistrationDateTime(tableFieldValue);
					page = this.findAll(pageable, tableSorting, specification);
					break;
				}
				case FIRST_REGISTRATION_AUTH_USER_EMAIL: {
					final AuthUser authUserSearch = new AuthUser();
					authUserSearch.setEmail(tableFieldValue);

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(null, authUserSearch, null);
					final Example<T> example = Example.of(abstractEntitySearch, FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSorting);
					break;
				}
				case LAST_MODIFICATION_DATE_TIME: {
					final Specification<T> specification = this.equalsLastModificationDateTime(tableFieldValue);
					page = this.findAll(pageable, tableSorting, specification);
					break;
				}
				case LAST_MODIFICATION_AUTH_USER_EMAIL: {
					final AuthUser authUserSearch = new AuthUser();
					authUserSearch.setEmail(tableFieldValue);

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(null, null, authUserSearch);
					final Example<T> example = Example.of(abstractEntitySearch, LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSorting);
					break;
				}
				case EMAIL: {
					final T abstractEntitySearch = this.getNewEntityWithAuthUserEmail(tableFieldValue);
					final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSorting);
					break;
				}
				case NAME: {
					final T abstractEntitySearch = this.getNewEntityWithAuthUserName(tableFieldValue);
					final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_NAME_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSorting);
					break;
				}
				case SURNAMES: {
					final T abstractEntitySearch = this.getNewEntityWithAuthUserSurnames(tableFieldValue);
					final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_SURNAMES_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSorting);
					break;
				}
				default: {
					throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", tableField.name(), "' not supported"));
				}
			}
		} else {
			page = this.findAll(pageable, tableSorting);
		}

		return page;
	}

	@ServiceMethod
	public AbstractEntityPageWithExceptionDTO<T> getAuthUserEntityPage(final Long authUserId, final TableSearchDTO tableSearchDTO, final Pageable pageable) {
		Page<T> page;
		Exception exception;
		try {
			if(tableSearchDTO != null) {
				final TableField tableField = TableField.findByCode(tableSearchDTO.getTableFieldCode());
				final String tableFieldValue = tableSearchDTO.getTableFieldValue();
				final TableSorting tableSorting = TableSorting.findByCode(tableSearchDTO.getTableSortingCode());

				page = this.getAuthUserEntityPage(authUserId, tableField, tableFieldValue, tableSorting, pageable);
			} else {
				final Example<T> example = this.getAuthUserIdExample(authUserId);
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

		final AbstractEntityPageWithExceptionDTO<T> pageWithExceptionDTO = new AbstractEntityPageWithExceptionDTO<>(page, exception);
		return pageWithExceptionDTO;
	}

	private Page<T> getAuthUserEntityPage(final Long authUserId, final TableField tableField, final String tableFieldValue, final TableSorting tableSorting, final Pageable pageable) {
		final Page<T> page;
		if(tableField != null && LogicalUtils.isNotNullNorEmptyString(tableFieldValue)) {
			switch(tableField) {
				case ID: {
					final AuthUser authUser = new AuthUser();
					authUser.setId(authUserId);

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

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(entityId, null, null);
					abstractEntitySearch.setAuthUser(authUser);

					final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_ID_AND_ID_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSorting);
					break;
				}
				case FIRST_REGISTRATION_DATE_TIME: {
					final Specification<T> specification = this.equalsAuthUserIdAndFirstRegistrationDateTime(authUserId, tableFieldValue);
					page = this.findAll(pageable, tableSorting, specification);
					break;
				}
				case FIRST_REGISTRATION_AUTH_USER_EMAIL: {
					final AuthUser firstRegistrationAuthUser = new AuthUser();
					firstRegistrationAuthUser.setEmail(tableFieldValue);

					final AuthUser authUser = new AuthUser();
					authUser.setId(authUserId);

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(null, firstRegistrationAuthUser, null);
					abstractEntitySearch.setAuthUser(authUser);

					final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSorting);
					break;
				}
				case LAST_MODIFICATION_DATE_TIME: {
					final Specification<T> specification = this.equalsAuthUserIdAndLastModificationDateTime(authUserId, tableFieldValue);
					page = this.findAll(pageable, tableSorting, specification);
					break;
				}
				case LAST_MODIFICATION_AUTH_USER_EMAIL: {
					final AuthUser lastModificationAuthUser = new AuthUser();
					lastModificationAuthUser.setEmail(tableFieldValue);

					final AuthUser authUser = new AuthUser();
					authUser.setId(authUserId);

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(null, null, lastModificationAuthUser);
					abstractEntitySearch.setAuthUser(authUser);

					final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSorting);
					break;
				}
				default: {
					throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", tableField.name(), "' not supported"));
				}
			}
		} else {
			final Example<T> example = this.getAuthUserIdExample(authUserId);
			page = this.findAll(example, pageable, tableSorting);
		}

		return page;
	}

	private Example<T> getAuthUserIdExample(Long authUserId) {
		final AuthUser authUser = new AuthUser();
		authUser.setId(authUserId);

		final T abstractEntitySearch = this.getNewEntityForSearchByExample(null, null, null);
		abstractEntitySearch.setAuthUser(authUser);

		final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_ID_EXAMPLE_MATCHER);
		return example;
	}

	private Specification<T> equalsAuthUserIdAndFirstRegistrationDateTime(final Long authUserId, final String dateTimeString) {
		return new Specification<T>() {
			private static final long serialVersionUID = 1385459567336079854L;

			@Override
			public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
				final String entityFieldName = "authUser";
				final String dateTimeFieldName = "firstRegistrationDateTime";
				final Predicate predicate = DatabaseUtils.getEqualsEntityIdAndDateTimePredicate(authUserId, entityFieldName, dateTimeString, dateTimeFieldName, root, criteriaQuery, criteriaBuilder);
				return predicate;
			}
		};
	}

	private Specification<T> equalsAuthUserIdAndLastModificationDateTime(final Long authUserId, final String dateTimeString) {
		return new Specification<T>() {
			private static final long serialVersionUID = 152158213933822618L;

			@Override
			public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
				final String entityFieldName = "authUser";
				final String dateTimeFieldName = "lastModificationDateTime";
				final Predicate predicate = DatabaseUtils.getEqualsEntityIdAndDateTimePredicate(authUserId, entityFieldName, dateTimeString, dateTimeFieldName, root, criteriaQuery, criteriaBuilder);
				return predicate;
			}
		};
	}
}

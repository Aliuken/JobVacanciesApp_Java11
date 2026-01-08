package com.aliuken.jobvacanciesapp.service.superclass;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntityWithAuthUser;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.database.DatabaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
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
public abstract class AbstractEntityWithAuthUserServiceSuperclass<T extends AbstractEntityWithAuthUser<T>> extends AbstractEntityServiceSuperclass<T> {

	private static final @NonNull ExampleMatcher AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsOneField("authUser.email");
	private static final @NonNull ExampleMatcher AUTH_USER_NAME_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsOneField("authUser.name");
	private static final @NonNull ExampleMatcher AUTH_USER_SURNAMES_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsOneField("authUser.surnames");

	private static final @NonNull ExampleMatcher AUTH_USER_ID_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithExactOneField("authUser.id");
	private static final @NonNull ExampleMatcher AUTH_USER_ID_AND_ID_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithExactTwoFields("authUser.id", "id");
	private static final @NonNull ExampleMatcher AUTH_USER_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsTwoFields("authUser.id", "firstRegistrationAuthUser.email");
	private static final @NonNull ExampleMatcher AUTH_USER_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsTwoFields("authUser.id", "lastModificationAuthUser.email");

	public abstract @NonNull T getNewEntityWithAuthUserEmail(String authUserEmail);
	public abstract @NonNull T getNewEntityWithAuthUserName(String authUserName);
	public abstract @NonNull T getNewEntityWithAuthUserSurnames(String authUserSurnames);

	@Override
	@ServiceMethod
	public @NonNull AbstractEntityPageWithExceptionDTO<T> getEntityPage(final TableSearchDTO tableSearchDTO, final @NonNull Pageable pageable) {
		Page<T> page;
		Exception exception;
		try {
			if(tableSearchDTO != null) {
				final TableField filterTableField = tableSearchDTO.getFilterTableField();
				final String filterValue = tableSearchDTO.getFilterValue();
				final TableField sortingTableField = tableSearchDTO.getSortingTableField();
				final TableSortingDirection tableSortingDirection = tableSearchDTO.getTableSortingDirection();

				page = this.getEntityPage(filterTableField, filterValue, sortingTableField, tableSortingDirection, pageable);
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

	private @NonNull Page<T> getEntityPage(final TableField filterTableField, final String filterValue, final TableField sortingTableField, final TableSortingDirection tableSortingDirection, final @NonNull Pageable pageable) {
		final Page<T> page;
		if(filterTableField != null && LogicalUtils.isNotNullNorEmptyString(filterValue)) {
			switch(filterTableField) {
				case ID: {
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

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(entityId, null, null);
					final Example<T> example = Example.of(abstractEntitySearch, ID_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, sortingTableField, tableSortingDirection);
					break;
				}
				case FIRST_REGISTRATION_DATE_TIME: {
					final Specification<T> specification = this.equalsFirstRegistrationDateTime(filterValue);
					page = this.findAll(pageable, sortingTableField, tableSortingDirection, specification);
					break;
				}
				case FIRST_REGISTRATION_AUTH_USER_EMAIL: {
					final AuthUser authUserSearch = new AuthUser();
					authUserSearch.setEmail(filterValue);

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(null, authUserSearch, null);
					final Example<T> example = Example.of(abstractEntitySearch, FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, sortingTableField, tableSortingDirection);
					break;
				}
				case LAST_MODIFICATION_DATE_TIME: {
					final Specification<T> specification = this.equalsLastModificationDateTime(filterValue);
					page = this.findAll(pageable, sortingTableField, tableSortingDirection, specification);
					break;
				}
				case LAST_MODIFICATION_AUTH_USER_EMAIL: {
					final AuthUser authUserSearch = new AuthUser();
					authUserSearch.setEmail(filterValue);

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(null, null, authUserSearch);
					final Example<T> example = Example.of(abstractEntitySearch, LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, sortingTableField, tableSortingDirection);
					break;
				}
				case AUTH_USER_EMAIL: {
					final T abstractEntitySearch = this.getNewEntityWithAuthUserEmail(filterValue);
					final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, sortingTableField, tableSortingDirection);
					break;
				}
				case AUTH_USER_NAME: {
					final T abstractEntitySearch = this.getNewEntityWithAuthUserName(filterValue);
					final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_NAME_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, sortingTableField, tableSortingDirection);
					break;
				}
				case AUTH_USER_SURNAMES: {
					final T abstractEntitySearch = this.getNewEntityWithAuthUserSurnames(filterValue);
					final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_SURNAMES_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, sortingTableField, tableSortingDirection);
					break;
				}
				default: {
					throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", filterTableField.name(), "' not supported"));
				}
			}
		} else {
			page = this.findAll(pageable, sortingTableField, tableSortingDirection);
		}

		return page;
	}

	@ServiceMethod
	public @NonNull AbstractEntityPageWithExceptionDTO<T> getAuthUserEntityPage(final Long authUserId, final TableSearchDTO tableSearchDTO, final @NonNull Pageable pageable) {
		Page<T> page;
		Exception exception;
		try {
			if(tableSearchDTO != null) {
				final TableField filterTableField = tableSearchDTO.getFilterTableField();
				final String filterValue = tableSearchDTO.getFilterValue();
				final TableField sortingTableField = tableSearchDTO.getSortingTableField();
				final TableSortingDirection tableSortingDirection = tableSearchDTO.getTableSortingDirection();

				page = this.getAuthUserEntityPage(authUserId, filterTableField, filterValue, sortingTableField, tableSortingDirection, pageable);
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

	private @NonNull Page<T> getAuthUserEntityPage(final Long authUserId, final TableField filterTableField, final String filterValue, final TableField sortingTableField, final TableSortingDirection tableSortingDirection, final @NonNull Pageable pageable) {
		final Page<T> page;
		if(filterTableField != null && LogicalUtils.isNotNullNorEmptyString(filterValue)) {
			switch(filterTableField) {
				case ID: {
					final AuthUser authUser = new AuthUser();
					authUser.setId(authUserId);

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

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(entityId, null, null);
					abstractEntitySearch.setAuthUser(authUser);

					final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_ID_AND_ID_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, sortingTableField, tableSortingDirection);
					break;
				}
				case FIRST_REGISTRATION_DATE_TIME: {
					final Specification<T> specification = this.equalsAuthUserIdAndFirstRegistrationDateTime(authUserId, filterValue);
					page = this.findAll(pageable, sortingTableField, tableSortingDirection, specification);
					break;
				}
				case FIRST_REGISTRATION_AUTH_USER_EMAIL: {
					final AuthUser firstRegistrationAuthUser = new AuthUser();
					firstRegistrationAuthUser.setEmail(filterValue);

					final AuthUser authUser = new AuthUser();
					authUser.setId(authUserId);

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(null, firstRegistrationAuthUser, null);
					abstractEntitySearch.setAuthUser(authUser);

					final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, sortingTableField, tableSortingDirection);
					break;
				}
				case LAST_MODIFICATION_DATE_TIME: {
					final Specification<T> specification = this.equalsAuthUserIdAndLastModificationDateTime(authUserId, filterValue);
					page = this.findAll(pageable, sortingTableField, tableSortingDirection, specification);
					break;
				}
				case LAST_MODIFICATION_AUTH_USER_EMAIL: {
					final AuthUser lastModificationAuthUser = new AuthUser();
					lastModificationAuthUser.setEmail(filterValue);

					final AuthUser authUser = new AuthUser();
					authUser.setId(authUserId);

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(null, null, lastModificationAuthUser);
					abstractEntitySearch.setAuthUser(authUser);

					final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, sortingTableField, tableSortingDirection);
					break;
				}
				default: {
					throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", filterTableField.name(), "' not supported"));
				}
			}
		} else {
			final Example<T> example = this.getAuthUserIdExample(authUserId);
			page = this.findAll(example, pageable, sortingTableField, tableSortingDirection);
		}

		return page;
	}

	private @NonNull Example<T> getAuthUserIdExample(Long authUserId) {
		final AuthUser authUser = new AuthUser();
		authUser.setId(authUserId);

		final T abstractEntitySearch = this.getNewEntityForSearchByExample(null, null, null);
		abstractEntitySearch.setAuthUser(authUser);

		final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_ID_EXAMPLE_MATCHER);
		return example;
	}

	private @NonNull Specification<T> equalsAuthUserIdAndFirstRegistrationDateTime(final Long authUserId, final String dateTimeString) {
		return new Specification<T>() {
			private static final long serialVersionUID = 1385459567336079854L;

			@Override
			public @NonNull Predicate toPredicate(final @NonNull Root<T> root, final @NonNull CriteriaQuery<?> criteriaQuery, final @NonNull CriteriaBuilder criteriaBuilder) {
				final String entityFieldName = "authUser";
				final String dateTimeFieldName = "firstRegistrationDateTime";
				final Predicate predicate = DatabaseUtils.getEqualsEntityIdAndDateTimePredicate(authUserId, entityFieldName, dateTimeString, dateTimeFieldName, root, criteriaQuery, criteriaBuilder);
				return predicate;
			}
		};
	}

	private @NonNull Specification<T> equalsAuthUserIdAndLastModificationDateTime(final Long authUserId, final String dateTimeString) {
		return new Specification<T>() {
			private static final long serialVersionUID = 152158213933822618L;

			@Override
			public @NonNull Predicate toPredicate(final @NonNull Root<T> root, final @NonNull CriteriaQuery<?> criteriaQuery, final @NonNull CriteriaBuilder criteriaBuilder) {
				final String entityFieldName = "authUser";
				final String dateTimeFieldName = "lastModificationDateTime";
				final Predicate predicate = DatabaseUtils.getEqualsEntityIdAndDateTimePredicate(authUserId, entityFieldName, dateTimeString, dateTimeFieldName, root, criteriaQuery, criteriaBuilder);
				return predicate;
			}
		};
	}
}

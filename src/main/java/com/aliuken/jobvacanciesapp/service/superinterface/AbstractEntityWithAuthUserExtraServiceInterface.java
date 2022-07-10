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
import com.aliuken.jobvacanciesapp.model.AbstractEntityWithAuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.dto.TableField;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.util.StringUtils;

public interface AbstractEntityWithAuthUserExtraServiceInterface<T extends AbstractEntityWithAuthUser> extends AbstractEntityExtraServiceInterface<T> {
	public static final Logger log = LoggerFactory.getLogger(AbstractEntityWithAuthUserExtraServiceInterface.class);

	public static final ExampleMatcher AUTH_USER_EMAIL_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("authUser.email", ExampleMatcher.GenericPropertyMatchers.contains());
	public static final ExampleMatcher AUTH_USER_NAME_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("authUser.name", ExampleMatcher.GenericPropertyMatchers.contains());
	public static final ExampleMatcher AUTH_USER_SURNAMES_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("authUser.surnames", ExampleMatcher.GenericPropertyMatchers.contains());

	public static final ExampleMatcher AUTH_USER_ID_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("authUser.id", ExampleMatcher.GenericPropertyMatchers.exact());
	public static final ExampleMatcher AUTH_USER_ID_AND_ID_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("authUser.id", ExampleMatcher.GenericPropertyMatchers.exact()).withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact());
	public static final ExampleMatcher AUTH_USER_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("authUser.id", ExampleMatcher.GenericPropertyMatchers.exact()).withMatcher("firstRegistrationAuthUser.email", ExampleMatcher.GenericPropertyMatchers.contains());
	public static final ExampleMatcher AUTH_USER_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("authUser.id", ExampleMatcher.GenericPropertyMatchers.exact()).withMatcher("lastModificationAuthUser.email", ExampleMatcher.GenericPropertyMatchers.contains());

	public T getNewEntityWithAuthUserEmail(String authUserEmail);
	public T getNewEntityWithAuthUserName(String authUserName);
	public T getNewEntityWithAuthUserSurnames(String authUserSurnames);

	@Override
	default AbstractEntityPageWithException<T> getEntityPage(final TableSearchDTO tableSearchDTO, final Pageable pageable) {
		Page<T> page;
		Exception exception = null;
		try {
			if(tableSearchDTO != null) {
				final TableField tableField = TableField.findByCode(tableSearchDTO.getTableFieldCode());
				final String tableFieldValue = tableSearchDTO.getTableFieldValue();
				final TableOrder tableOrder = TableOrder.findByCode(tableSearchDTO.getTableOrderCode());

				page = this.getEntityPage(tableField, tableFieldValue, tableOrder, pageable);
			} else {
				page = this.findAll(pageable);
			}
		} catch(Exception e) {
			if(log.isErrorEnabled()) {
				log.error("An exception happened when trying to get an entity page", e);
			}
			page = Page.empty();
			exception = e;
		}

		AbstractEntityPageWithException<T> abstractEntityPageWithException = new AbstractEntityPageWithException<>();
		abstractEntityPageWithException.setPage(page);
		abstractEntityPageWithException.setException(exception);

		return abstractEntityPageWithException;
	}

	private Page<T> getEntityPage(final TableField tableField, final String tableFieldValue, final TableOrder tableOrder, final Pageable pageable) {
		final Page<T> page;
		if(tableField != null && tableFieldValue != null && !tableFieldValue.isEmpty()) {
			switch(tableField) {
				case ID: {
					final Long entityId;
					try {
						entityId = Long.valueOf(tableFieldValue);
					} catch(NumberFormatException e) {
						if(log.isErrorEnabled()) {
							log.error("An exception happened when trying to get an entity page", e);
						}
						throw new IllegalArgumentException(StringUtils.getStringJoined("The id '", tableFieldValue, "' is not a number"));
					}

					final T entitySearch = this.getNewEntityWithGenericData(entityId, null, null);
					final Example<T> example = this.getIdExample(entitySearch);
					page = this.findAll(example, pageable, tableOrder);
					break;
				}
				case FIRST_REGISTRATION_DATE_TIME: {
					final Specification<T> specification = this.equalsFirstRegistrationDateTime(tableFieldValue);
					page = this.findAll(pageable, tableOrder, specification);
					break;
				}
				case FIRST_REGISTRATION_AUTH_USER_EMAIL: {
					final AuthUser authUserSearch = new AuthUser();
					authUserSearch.setEmail(tableFieldValue);

					final T entitySearch = this.getNewEntityWithGenericData(null, authUserSearch, null);
					final Example<T> example = this.getFirstRegistrationAuthUserEmailExample(entitySearch);
					page = this.findAll(example, pageable, tableOrder);
					break;
				}
				case LAST_MODIFICATION_DATE_TIME: {
					final Specification<T> specification = this.equalsLastModificationDateTime(tableFieldValue);
					page = this.findAll(pageable, tableOrder, specification);
					break;
				}
				case LAST_MODIFICATION_AUTH_USER_EMAIL: {
					final AuthUser authUserSearch = new AuthUser();
					authUserSearch.setEmail(tableFieldValue);

					final T entitySearch = this.getNewEntityWithGenericData(null, null, authUserSearch);
					final Example<T> example = this.getLastModificationAuthUserEmailExample(entitySearch);
					page = this.findAll(example, pageable, tableOrder);
					break;
				}
				case EMAIL: {
					final T entitySearch = this.getNewEntityWithAuthUserEmail(tableFieldValue);
					final Example<T> example = this.getAuthUserEmailExample(entitySearch);
					page = this.findAll(example, pageable, tableOrder);
					break;
				}
				case NAME: {
					final T entitySearch = this.getNewEntityWithAuthUserName(tableFieldValue);
					final Example<T> example = this.getAuthUserNameExample(entitySearch);
					page = this.findAll(example, pageable, tableOrder);
					break;
				}
				case SURNAMES: {
					final T entitySearch = this.getNewEntityWithAuthUserSurnames(tableFieldValue);
					final Example<T> example = this.getAuthUserSurnamesExample(entitySearch);
					page = this.findAll(example, pageable, tableOrder);
					break;
				}
				default: {
					throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", tableField.name(), "' not supported"));
				}
			}
		} else {
			page = this.findAll(pageable, tableOrder);
		}

		return page;
	}

	default AbstractEntityPageWithException<T> getAuthUserEntityPage(final Long authUserId, final TableSearchDTO tableSearchDTO, final Pageable pageable) {
		Page<T> page;
		Exception exception = null;
		try {
			if(tableSearchDTO != null) {
				final TableField tableField = TableField.findByCode(tableSearchDTO.getTableFieldCode());
				final String tableFieldValue = tableSearchDTO.getTableFieldValue();
				final TableOrder tableOrder = TableOrder.findByCode(tableSearchDTO.getTableOrderCode());

				page = this.getAuthUserEntityPage(authUserId, tableField, tableFieldValue, tableOrder, pageable);
			} else {
				final Example<T> example = this.getAuthUserIdExample(authUserId);
				page = this.findAll(example, pageable);
			}
		} catch(Exception e) {
			if(log.isErrorEnabled()) {
				log.error("An exception happened when trying to get an entity page", e);
			}
			page = Page.empty();
			exception = e;
		}

		AbstractEntityPageWithException<T> abstractEntityPageWithException = new AbstractEntityPageWithException<>();
		abstractEntityPageWithException.setPage(page);
		abstractEntityPageWithException.setException(exception);

		return abstractEntityPageWithException;
	}

	private Page<T> getAuthUserEntityPage(final Long authUserId, final TableField tableField, final String tableFieldValue, final TableOrder tableOrder, final Pageable pageable) {
		final Page<T> page;
		if(tableField != null && tableFieldValue != null && !tableFieldValue.isEmpty()) {
			switch(tableField) {
				case ID: {
					final AuthUser authUser = new AuthUser();
					authUser.setId(authUserId);

					final Long entityId;
					try {
						entityId = Long.valueOf(tableFieldValue);
					} catch(NumberFormatException e) {
						if(log.isErrorEnabled()) {
							log.error("An exception happened when trying to get an entity page", e);
						}
						throw new IllegalArgumentException(StringUtils.getStringJoined("The id '", tableFieldValue, "' is not a number"));
					}

					final T entitySearch = this.getNewEntityWithGenericData(entityId, null, null);
					entitySearch.setAuthUser(authUser);

					final Example<T> example = this.getAuthUserIdAndIdExample(entitySearch);
					page = this.findAll(example, pageable, tableOrder);
					break;
				}
				case FIRST_REGISTRATION_DATE_TIME: {
					final Specification<T> specification = this.equalsAuthUserIdAndFirstRegistrationDateTime(authUserId, tableFieldValue);
					page = this.findAll(pageable, tableOrder, specification);
					break;
				}
				case FIRST_REGISTRATION_AUTH_USER_EMAIL: {
					final AuthUser firstRegistrationAuthUser = new AuthUser();
					firstRegistrationAuthUser.setEmail(tableFieldValue);

					final AuthUser authUser = new AuthUser();
					authUser.setId(authUserId);

					final T entitySearch = this.getNewEntityWithGenericData(null, firstRegistrationAuthUser, null);
					entitySearch.setAuthUser(authUser);

					final Example<T> example = this.getAuthUserIdAndFirstRegistrationAuthUserEmailExample(entitySearch);
					page = this.findAll(example, pageable, tableOrder);
					break;
				}
				case LAST_MODIFICATION_DATE_TIME: {
					final Specification<T> specification = this.equalsAuthUserIdAndLastModificationDateTime(authUserId, tableFieldValue);
					page = this.findAll(pageable, tableOrder, specification);
					break;
				}
				case LAST_MODIFICATION_AUTH_USER_EMAIL: {
					final AuthUser lastModificationAuthUser = new AuthUser();
					lastModificationAuthUser.setEmail(tableFieldValue);

					final AuthUser authUser = new AuthUser();
					authUser.setId(authUserId);

					final T entitySearch = this.getNewEntityWithGenericData(null, null, lastModificationAuthUser);
					entitySearch.setAuthUser(authUser);

					final Example<T> example = this.getAuthUserIdAndLastModificationAuthUserEmailExample(entitySearch);
					page = this.findAll(example, pageable, tableOrder);
					break;
				}
				default: {
					throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", tableField.name(), "' not supported"));
				}
			}
		} else {
			final Example<T> example = this.getAuthUserIdExample(authUserId);
			page = this.findAll(example, pageable, tableOrder);
		}

		return page;
	}

	default Example<T> getAuthUserEmailExample(T abstractEntitySearch){
		final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_EMAIL_EXAMPLE_MATCHER);
		return example;
	}

	default Example<T> getAuthUserNameExample(T abstractEntitySearch){
		final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_NAME_EXAMPLE_MATCHER);
		return example;
	}

	default Example<T> getAuthUserSurnamesExample(T abstractEntitySearch){
		final Example<T> example = Example.of(abstractEntitySearch, AUTH_USER_SURNAMES_EXAMPLE_MATCHER);
		return example;
	}

	default Example<T> getAuthUserIdExample(Long authUserId){
		final AuthUser authUser = new AuthUser();
		authUser.setId(authUserId);

		final T entitySearch = this.getNewEntityWithGenericData(null, null, null);
		entitySearch.setAuthUser(authUser);

		final Example<T> example = Example.of(entitySearch, AUTH_USER_ID_EXAMPLE_MATCHER);
		return example;
	}

	default Example<T> getAuthUserIdAndIdExample(T entitySearch){
		final Example<T> example = Example.of(entitySearch, AUTH_USER_ID_AND_ID_EXAMPLE_MATCHER);
		return example;
	}

	default Example<T> getAuthUserIdAndFirstRegistrationAuthUserEmailExample(T entitySearch){
		final Example<T> example = Example.of(entitySearch, AUTH_USER_ID_AND_FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
		return example;
	}

	default Example<T> getAuthUserIdAndLastModificationAuthUserEmailExample(T entitySearch){
		final Example<T> example = Example.of(entitySearch, AUTH_USER_ID_AND_LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
		return example;
	}

	default Specification<T> equalsAuthUserIdAndFirstRegistrationDateTime(final Long authUserId, final String dateTimeString){
        return new Specification<T>() {
			private static final long serialVersionUID = 1385459567336079854L;

			@Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				final Predicate predicate;
				if(dateTimeString == null || Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD.equals(dateTimeString)) {
					final Expression<Long> authUserIdExpression = root.get("authUser").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(authUserIdExpression, authUserId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("firstRegistrationDateTime").as(LocalDateTime.class);
					Predicate predicate2 = criteriaBuilder.isNull(localDateTimeExpression);

					predicate = criteriaBuilder.and(predicate1, predicate2);
				} else {
					final Expression<Long> authUserIdExpression = root.get("authUser").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(authUserIdExpression, authUserId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("firstRegistrationDateTime").as(LocalDateTime.class);
					final Expression<String> stringExpression = criteriaBuilder.function("DATE_FORMAT", String.class, localDateTimeExpression, criteriaBuilder.literal("%d-%m-%Y %H:%i:%s"));
					Predicate predicate2 = criteriaBuilder.like(stringExpression, dateTimeString + "%");

					predicate = criteriaBuilder.and(predicate1, predicate2);
				}
                return predicate;
            }
        };
    }

	default Specification<T> equalsAuthUserIdAndLastModificationDateTime(final Long authUserId, final String dateTimeString){
        return new Specification<T>() {
			private static final long serialVersionUID = 152158213933822618L;

			@Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final Predicate predicate;
				if(dateTimeString == null || Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD.equals(dateTimeString)) {
					final Expression<Long> authUserIdExpression = root.get("authUser").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(authUserIdExpression, authUserId);

					final Expression<LocalDateTime> localDateTimeExpression = root.get("lastModificationDateTime").as(LocalDateTime.class);
					Predicate predicate2 = criteriaBuilder.isNull(localDateTimeExpression);

					predicate = criteriaBuilder.and(predicate1, predicate2);
				} else {
					final Expression<Long> authUserIdExpression = root.get("authUser").get("id").as(Long.class);
					Predicate predicate1 = criteriaBuilder.equal(authUserIdExpression, authUserId);

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

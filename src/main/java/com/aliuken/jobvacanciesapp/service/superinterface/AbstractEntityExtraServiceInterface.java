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
import com.aliuken.jobvacanciesapp.model.AbstractEntity;
import com.aliuken.jobvacanciesapp.model.AbstractEntityPageWithException;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.dto.TableField;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.util.StringUtils;

public interface AbstractEntityExtraServiceInterface<T extends AbstractEntity> extends AbstractEntityServiceInterface<T> {
	public static final Logger log = LoggerFactory.getLogger(AbstractEntityExtraServiceInterface.class);

	public static final ExampleMatcher ID_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact());
	public static final ExampleMatcher FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("firstRegistrationAuthUser.email", ExampleMatcher.GenericPropertyMatchers.contains());
	public static final ExampleMatcher LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("lastModificationAuthUser.email", ExampleMatcher.GenericPropertyMatchers.contains());
	public static final ExampleMatcher EMAIL_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains());
	public static final ExampleMatcher NAME_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
	public static final ExampleMatcher SURNAMES_EXAMPLE_MATCHER = ExampleMatcher.matching().withMatcher("surnames", ExampleMatcher.GenericPropertyMatchers.contains());

	public T getNewEntityWithGenericData(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser);

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

		final AbstractEntityPageWithException<T> abstractEntityPageWithException = new AbstractEntityPageWithException<>();
		abstractEntityPageWithException.setPage(page);
		abstractEntityPageWithException.setException(exception);

		return abstractEntityPageWithException;
	}
	
	@SuppressWarnings("unchecked")
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
					page = this.findAll(pageable, tableOrder, example);
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
					page = this.findAll(pageable, tableOrder, example);
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
					page = this.findAll(pageable, tableOrder, example);
					break;
				}
				default: {
					if(this.getEntityClass().equals(AuthUser.class)) {
						switch(tableField) {
							case EMAIL: {
								final AuthUser authUserSearch = new AuthUser();
								authUserSearch.setEmail(tableFieldValue);

								final Example<T> example = (Example<T>) this.getEmailExample(authUserSearch);
								page = this.findAll(pageable, tableOrder, example);
								break;
							}
							case NAME: {
								final AuthUser authUserSearch = new AuthUser();
								authUserSearch.setName(tableFieldValue);

								final Example<T> example = (Example<T>) this.getNameExample(authUserSearch);
								page = this.findAll(pageable, tableOrder, example);
								break;
							}
							case SURNAMES: {
								final AuthUser authUserSearch = new AuthUser();
								authUserSearch.setSurnames(tableFieldValue);

								final Example<T> example = (Example<T>) this.getSurnamesExample(authUserSearch);
								page = this.findAll(pageable, tableOrder, example);
								break;
							}
							default: {
								throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", tableField.name(), "' not supported"));
							}
						}
					} else {
						throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", tableField.name(), "' not supported"));
					}
					break;
				}
			}
		} else {
			page = this.findAll(pageable, tableOrder);
		}
		
		return page;
	}

	default Example<T> getIdExample(T abstractEntitySearch){
		final Example<T> example = Example.of(abstractEntitySearch, ID_EXAMPLE_MATCHER);
		return example;
	}
	
	default Example<T> getFirstRegistrationAuthUserEmailExample(T abstractEntitySearch){
		final Example<T> example = Example.of(abstractEntitySearch, FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
		return example;
	}
	
	default Example<T> getLastModificationAuthUserEmailExample(T abstractEntitySearch){
		final Example<T> example = Example.of(abstractEntitySearch, LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
		return example;
	}

	default Example<AuthUser> getEmailExample(AuthUser authUserSearch){
		final Example<AuthUser> example = Example.of(authUserSearch, EMAIL_EXAMPLE_MATCHER);
		return example;
	}

	default Example<AuthUser> getNameExample(AuthUser authUserSearch){
		final Example<AuthUser> example = Example.of(authUserSearch, NAME_EXAMPLE_MATCHER);
		return example;
	}

	default Example<AuthUser> getSurnamesExample(AuthUser authUserSearch){
		final Example<AuthUser> example = Example.of(authUserSearch, SURNAMES_EXAMPLE_MATCHER);
		return example;
	}

	default Specification<T> equalsFirstRegistrationDateTime(final String dateTimeString){
        return new Specification<T>() {
            private static final long serialVersionUID = -6553680154346547347L;

			@Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				final Predicate predicate;
				if(dateTimeString == null || Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD.equals(dateTimeString)) {
					final Expression<LocalDateTime> localDateTimeExpression = root.get("firstRegistrationDateTime").as(LocalDateTime.class);
					predicate = criteriaBuilder.isNull(localDateTimeExpression);
				} else {
					final Expression<LocalDateTime> localDateTimeExpression = root.get("firstRegistrationDateTime").as(LocalDateTime.class);
					final Expression<String> stringExpression = criteriaBuilder.function("DATE_FORMAT", String.class, localDateTimeExpression, criteriaBuilder.literal("%d-%m-%Y %H:%i:%s"));
					predicate = criteriaBuilder.like(stringExpression, dateTimeString + "%");
				}
                return predicate;
            }
        };
    }

	default Specification<T> equalsLastModificationDateTime(final String dateTimeString){
        return new Specification<T>() {
            private static final long serialVersionUID = -6382431881297789908L;

			@Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final Predicate predicate;
				if(dateTimeString == null || Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD.equals(dateTimeString)) {
					final Expression<LocalDateTime> localDateTimeExpression = root.get("lastModificationDateTime").as(LocalDateTime.class);
					predicate = criteriaBuilder.isNull(localDateTimeExpression);
				} else {
					final Expression<LocalDateTime> localDateTimeExpression = root.get("lastModificationDateTime").as(LocalDateTime.class);
					final Expression<String> stringExpression = criteriaBuilder.function("DATE_FORMAT", String.class, localDateTimeExpression, criteriaBuilder.literal("%d-%m-%Y %H:%i:%s"));
					predicate = criteriaBuilder.like(stringExpression, dateTimeString + "%");
				}
                return predicate;
            }
        };
    }
}

package com.aliuken.jobvacanciesapp.service.superclass;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepositoryInterface;
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
import java.util.List;
import java.util.Map;

@Transactional
@Slf4j
public abstract class AbstractEntityServiceSuperclass<T extends AbstractEntity<T>> implements UpgradedJpaRepositoryInterface<T> {

	protected static final ExampleMatcher ID_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithExactOneField("id");
	protected static final ExampleMatcher FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsOneField("firstRegistrationAuthUser.email");
	protected static final ExampleMatcher LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsOneField("lastModificationAuthUser.email");

	public abstract UpgradedJpaRepository<T> getEntityRepository();

	public abstract T getNewEntityForSearchByExample(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser);

	@Override
	@ServiceMethod
	public <S extends T> S saveAndFlush(S entity) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		entity = upgradedJpaRepository.saveAndFlush(entity);
		return entity;
	}

	@Override
	@ServiceMethod
	public void deleteByIdAndFlush(final Long entityId) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		upgradedJpaRepository.deleteByIdAndFlush(entityId);
	}

	@Override
	@ServiceMethod
	public List<T> findAll() {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final List<T> list = upgradedJpaRepository.findAll();
		return list;
	}

	@Override
	@ServiceMethod
	public T findByIdNotOptional(final Long entityId) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final T entity = upgradedJpaRepository.findByIdNotOptional(entityId);
		return entity;
	}

	@Override
	@ServiceMethod
	public T findByIdOrNewEntity(final Long entityId) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final T entity = upgradedJpaRepository.findByIdOrNewEntity(entityId);
		return entity;
	}

	@Override
	@ServiceMethod
	public Page<T> findAll(final Pageable pageable) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final Page<T> page = upgradedJpaRepository.findAll(pageable);
		return page;
	}

	@Override
	@ServiceMethod
	public Page<T> findAll(final Pageable pageable, final TableField tableSortingField, final TableSortingDirection tableSortingDirection) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final Page<T> page = upgradedJpaRepository.findAll(pageable, tableSortingField, tableSortingDirection);
		return page;
	}

	@Override
	@ServiceMethod
	public Page<T> findAll(final Pageable pageable, final TableField tableSortingField, final TableSortingDirection tableSortingDirection, final Specification<T> specification) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final Page<T> page = upgradedJpaRepository.findAll(pageable, tableSortingField, tableSortingDirection, specification);
		return page;
	}

	@Override
	@ServiceMethod
	public Page<T> findAll(final Specification<T> specification, final Pageable pageable) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final Page<T> page = upgradedJpaRepository.findAll(specification, pageable);
		return page;
	}

	@Override
	@ServiceMethod
	public <S extends T> List<S> findAll(final Example<S> example) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final List<S> list = upgradedJpaRepository.findAll(example);
		return list;
	}

	@Override
	@ServiceMethod
	public <S extends T> Page<S> findAll(final Example<S> example, final Pageable pageable) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final Page<S> page = upgradedJpaRepository.findAll(example, pageable);
		return page;
	}

	@Override
	@ServiceMethod
	public <S extends T> Page<S> findAll(final Example<S> example, final Pageable pageable, final TableField tableSortingField, final TableSortingDirection tableSortingDirection) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final Page<S> page = upgradedJpaRepository.findAll(example, pageable, tableSortingField, tableSortingDirection);
		return page;
	}

	@Override
	@ServiceMethod
	public T refreshEntity(T entity) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		entity = upgradedJpaRepository.refreshEntity(entity);
		return entity;
	}

	@Override
	@ServiceMethod
	public T executeQuerySingleResult(final String jpqlQuery, final Map<String, Object> parameterMap) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final T entity = upgradedJpaRepository.executeQuerySingleResult(jpqlQuery, parameterMap);
		return entity;
	}

	@Override
	@ServiceMethod
	public List<T> executeQueryResultList(final String jpqlQuery, final Map<String, Object> parameterMap) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final List<T> list = upgradedJpaRepository.executeQueryResultList(jpqlQuery, parameterMap);
		return list;
	}

	@Override
	@ServiceMethod
	public int executeUpdate(final String jpqlQuery, final Map<String, Object> parameterMap) {
		final UpgradedJpaRepository<T> upgradedJpaRepository = this.getEntityRepository();
		final int rowsUpdated = upgradedJpaRepository.executeUpdate(jpqlQuery, parameterMap);
		return rowsUpdated;
	}

	@ServiceMethod
	public AbstractEntityPageWithExceptionDTO<T> getEntityPage(final TableSearchDTO tableSearchDTO, final Pageable pageable) {
		Page<T> page;
		Exception exception;
		try {
			if(tableSearchDTO != null) {
				final TableField filterTableField = tableSearchDTO.getFilterTableField();
				final String filterValue = tableSearchDTO.getFilterValue();
				final TableField tableSortingField = tableSearchDTO.getTableSortingField();
				final TableSortingDirection tableSortingDirection = tableSearchDTO.getTableSortingDirection();
				page = this.getEntityPage(filterTableField, filterValue, tableSortingField, tableSortingDirection, pageable);
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

	private Page<T> getEntityPage(final TableField filterTableField, final String filterValue, final TableField tableSortingField, final TableSortingDirection tableSortingDirection, final Pageable pageable) {
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
					page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
					break;
				}
				case FIRST_REGISTRATION_DATE_TIME: {
					final Specification<T> specification = this.equalsFirstRegistrationDateTime(filterValue);
					page = this.findAll(pageable, tableSortingField, tableSortingDirection, specification);
					break;
				}
				case FIRST_REGISTRATION_AUTH_USER_EMAIL: {
					final AuthUser authUserSearch = new AuthUser();
					authUserSearch.setEmail(filterValue);

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(null, authUserSearch, null);
					final Example<T> example = Example.of(abstractEntitySearch, FIRST_REGISTRATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
					break;
				}
				case LAST_MODIFICATION_DATE_TIME: {
					final Specification<T> specification = this.equalsLastModificationDateTime(filterValue);
					page = this.findAll(pageable, tableSortingField, tableSortingDirection, specification);
					break;
				}
				case LAST_MODIFICATION_AUTH_USER_EMAIL: {
					final AuthUser authUserSearch = new AuthUser();
					authUserSearch.setEmail(filterValue);

					final T abstractEntitySearch = this.getNewEntityForSearchByExample(null, null, authUserSearch);
					final Example<T> example = Example.of(abstractEntitySearch, LAST_MODIFICATION_AUTH_USER_EMAIL_EXAMPLE_MATCHER);
					page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
					break;
				}
				default: {
					final Example<T> example = this.getDefaultEntityPageExample(filterTableField, filterValue);
					page = this.findAll(example, pageable, tableSortingField, tableSortingDirection);
					break;
				}
			}
		} else {
			page = this.findAll(pageable, tableSortingField, tableSortingDirection);
		}

		return page;
	}

	/**
	 * This method is overridden in AuthUserServiceImpl
	 */
	protected Example<T> getDefaultEntityPageExample(final TableField filterTableField, final String filterValue) {
		throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", filterTableField.name(), "' not supported"));
	}

	protected Specification<T> equalsFirstRegistrationDateTime(final String dateTimeString) {
		return new Specification<T>() {
			private static final long serialVersionUID = -6553680154346547347L;

			@Override
			public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
				final String dateTimeFieldName = "firstRegistrationDateTime";
				final Predicate predicate = DatabaseUtils.getEqualsDateTimePredicate(dateTimeString, dateTimeFieldName, root, criteriaQuery, criteriaBuilder);
				return predicate;
			}
		};
	}

	protected Specification<T> equalsLastModificationDateTime(final String dateTimeString) {
		return new Specification<T>() {
			private static final long serialVersionUID = -6382431881297789908L;

			@Override
			public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
				final String dateTimeFieldName = "lastModificationDateTime";
				final Predicate predicate = DatabaseUtils.getEqualsDateTimePredicate(dateTimeString, dateTimeFieldName, root, criteriaQuery, criteriaBuilder);
				return predicate;
			}
		};
	}
}

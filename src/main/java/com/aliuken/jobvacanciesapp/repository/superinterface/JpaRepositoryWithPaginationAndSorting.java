package com.aliuken.jobvacanciesapp.repository.superinterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.aliuken.jobvacanciesapp.model.AbstractEntity;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.util.ApplicationContextUtil;
import com.aliuken.jobvacanciesapp.util.StringUtils;

@NoRepositoryBean
public interface JpaRepositoryWithPaginationAndSorting<T extends AbstractEntity> extends JpaRepository<T, Long> {
	public static final Map<Class<? extends AbstractEntity>, SimpleJpaRepository<? extends AbstractEntity, Long>> JPA_REPOSITORY_MAP = new HashMap<>();

	abstract Class<T> getEntityClass();

	default T findByIdNotOptional(Long id) {
		if (id == null) {
			return null;
		}

		SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if (jpaRepository == null) {
			return null;
		}

		final Optional<T> entityOptional = jpaRepository.findById(id);
		if (!entityOptional.isPresent()) {
			return null;
		}

		final T entity = entityOptional.get();

		return entity;
	}

	default void deleteByIdAndFlush(Long id) {
		SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if (jpaRepository == null) {
			return;
		}

		jpaRepository.deleteById(id);
		jpaRepository.flush();
	}

	default <S extends T> S saveAndFlush(S abstractEntity) {
		SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if (jpaRepository == null) {
			return null;
		}

		abstractEntity = jpaRepository.saveAndFlush(abstractEntity);
		return abstractEntity;
	}

	default List<T> findAll() {
		SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if (jpaRepository == null) {
			return null;
		}

		final List<T> result = jpaRepository.findAll();

		return result;
	}

	default Page<T> findAll(Pageable pageable) {
		SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if (jpaRepository == null) {
			return null;
		}

		final Page<T> page = jpaRepository.findAll(pageable);

		return page;
	}

	default Page<T> findAll(Pageable pageable, TableOrder tableOrder) {
		SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if (jpaRepository == null) {
			return null;
		}

		final Class<T> entityClass = this.getEntityClass();
		final Pageable finalPageable = this.getFinalPageable(pageable, tableOrder, entityClass);
		final Page<T> page = jpaRepository.findAll(finalPageable);

		return page;
	}

	default Page<T> findAll(Pageable pageable, TableOrder tableOrder, Specification<T> abstractEntitySpecification) {
		if (abstractEntitySpecification == null) {
			throw new IllegalArgumentException("abstractEntitySpecification cannot be null");
		}

		final Class<T> entityClass = this.getEntityClass();
		final Pageable finalPageable = this.getFinalPageable(pageable, tableOrder, entityClass);
		final Page<T> page = this.findAll(abstractEntitySpecification, finalPageable);

		return page;
	}

	private Page<T> findAll(Specification<T> abstractEntitySpecification, Pageable pageable) {
		SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if (jpaRepository == null) {
			return null;
		}

		Page<T> page = jpaRepository.findAll(abstractEntitySpecification, pageable);

		return page;
	}

	default <S extends T> List<S> findAll(Example<S> abstractEntityExample) {
		SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if (jpaRepository == null) {
			return null;
		}

		final List<S> result = jpaRepository.findAll(abstractEntityExample);

		return result;
	}

	default <S extends T> Page<S> findAll(Example<S> abstractEntityExample, Pageable pageable) {
		SimpleJpaRepository<T, Long> jpaRepository = this.getJpaRepository();
		if (jpaRepository == null) {
			return null;
		}

		final Page<S> page = jpaRepository.findAll(abstractEntityExample, pageable);

		return page;
	}

	default <S extends T> Page<S> findAll(Example<S> abstractEntityExample, Pageable pageable, TableOrder tableOrder) {
		if (abstractEntityExample == null) {
			throw new IllegalArgumentException("abstractEntityExample cannot be null");
		}

		Class<S> abstractEntityClass = abstractEntityExample.getProbeType();
		final JpaRepository<S, Long> jpaRepository = JpaRepositoryWithPaginationAndSorting.getJpaRepository(abstractEntityClass);
		final Pageable finalPageable = this.getFinalPageable(pageable, tableOrder, abstractEntityClass);
		final Page<S> page = jpaRepository.findAll(abstractEntityExample, finalPageable);

		return page;
	}

	private <S extends T> Pageable getFinalPageable(Pageable pageable, TableOrder tableOrder,
			Class<S> abstractEntityClass) {
		if (pageable == null) {
			throw new IllegalArgumentException("pageable cannot be null");
		}

		final Pageable finalPageable;
		if (tableOrder == null) {
			finalPageable = pageable;
		} else if (TableOrder.ID_ASC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));
		} else if (TableOrder.ID_DESC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
		} else if (TableOrder.FIRST_REGISTRATION_DATE_TIME_ASC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "firstRegistrationDateTime"));
		} else if (TableOrder.FIRST_REGISTRATION_DATE_TIME_DESC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "firstRegistrationDateTime"));
		} else if (TableOrder.FIRST_REGISTRATION_AUTH_USER_EMAIL_ASC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "firstRegistrationAuthUser.email"));
		} else if (TableOrder.FIRST_REGISTRATION_AUTH_USER_EMAIL_DESC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "firstRegistrationAuthUser.email"));
		} else if (TableOrder.LAST_MODIFICATION_DATE_TIME_ASC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "lastModificationDateTime"));
		} else if (TableOrder.LAST_MODIFICATION_DATE_TIME_DESC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "lastModificationDateTime"));
		} else if (TableOrder.LAST_MODIFICATION_AUTH_USER_EMAIL_ASC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "lastModificationAuthUser.email"));
		} else if (TableOrder.LAST_MODIFICATION_AUTH_USER_EMAIL_DESC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "lastModificationAuthUser.email"));
		} else if (TableOrder.EMAIL_ASC.equals(tableOrder)) {
			if (AuthUser.class.equals(abstractEntityClass)) {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "email"));
			} else {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "authUser.email"));
			}
		} else if (TableOrder.EMAIL_DESC.equals(tableOrder)) {
			if (AuthUser.class.equals(abstractEntityClass)) {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "email"));
			} else {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "authUser.email"));
			}
		} else if (TableOrder.NAME_ASC.equals(tableOrder)) {
			if (AuthUser.class.equals(abstractEntityClass)) {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "name"));
			} else {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "authUser.name"));
			}
		} else if (TableOrder.NAME_DESC.equals(tableOrder)) {
			if (AuthUser.class.equals(abstractEntityClass)) {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "name"));
			} else {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "authUser.name"));
			}
		} else if (TableOrder.SURNAMES_ASC.equals(tableOrder)) {
			if (AuthUser.class.equals(abstractEntityClass)) {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "surnames"));
			} else {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "authUser.surnames"));
			}
		} else if (TableOrder.SURNAMES_DESC.equals(tableOrder)) {
			if (AuthUser.class.equals(abstractEntityClass)) {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "surnames"));
			} else {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "authUser.surnames"));
			}
		} else {
			throw new IllegalArgumentException(StringUtils.getStringJoined("TableOrder '", tableOrder.name(), "' not supported"));
		}

		return finalPageable;
	}

	default T executeQuerySingleResult(String jpqlQuery, Map<String, Object> parameterMap) {
		final TypedQuery<T> typedQuery = getTypedQuery(jpqlQuery, parameterMap);

		T result;
		try {
			result = typedQuery.getSingleResult();
		} catch (NoResultException exception) {
			result = null;
		}

		return result;
	}

	default List<T> executeQueryResultList(String jpqlQuery, Map<String, Object> parameterMap) {
		final TypedQuery<T> typedQuery = getTypedQuery(jpqlQuery, parameterMap);
		final List<T> result = typedQuery.getResultList();

		return result;
	}

	default int executeUpdate(String jpqlQuery, Map<String, Object> parameterMap) {
		final Query query = getQuery(jpqlQuery, parameterMap);

		int rows = query.executeUpdate();

		return rows;
	}

	private TypedQuery<T> getTypedQuery(String jpqlQuery, Map<String, Object> parameterMap) {
		final Class<T> abstractEntityClass = this.getEntityClass();
		final EntityManager entityManager = JpaRepositoryWithPaginationAndSorting.getEntityManager(abstractEntityClass);
		final TypedQuery<T> typedQuery = entityManager.createQuery(jpqlQuery, abstractEntityClass);
		if (parameterMap != null) {
			for (Map.Entry<String, Object> parameterMapEntry : parameterMap.entrySet()) {
				typedQuery.setParameter(parameterMapEntry.getKey(), parameterMapEntry.getValue());
			}
		}

		return typedQuery;
	}
	
	private Query getQuery(String jpqlQuery, Map<String, Object> parameterMap) {
		final Class<T> abstractEntityClass = this.getEntityClass();
		final EntityManager entityManager = JpaRepositoryWithPaginationAndSorting.getEntityManager(abstractEntityClass);
		final Query query = entityManager.createQuery(jpqlQuery);
		if (parameterMap != null) {
			for (Map.Entry<String, Object> parameterMapEntry : parameterMap.entrySet()) {
				query.setParameter(parameterMapEntry.getKey(), parameterMapEntry.getValue());
			}
		}

		return query;
	}

	private SimpleJpaRepository<T, Long> getJpaRepository() {
		final Class<T> abstractEntityClass = this.getEntityClass();
		final SimpleJpaRepository<T, Long> jpaRepository = JpaRepositoryWithPaginationAndSorting.getJpaRepository(abstractEntityClass);

		return jpaRepository;
	}

	@SuppressWarnings("unchecked")
	public static <S extends AbstractEntity> SimpleJpaRepository<S, Long> getJpaRepository(Class<S> abstractEntityClass) {
		SimpleJpaRepository<S, Long> jpaRepository = (SimpleJpaRepository<S, Long>) JPA_REPOSITORY_MAP.get(abstractEntityClass);
		if (jpaRepository == null) {
			final EntityManager entityManager = JpaRepositoryWithPaginationAndSorting.getEntityManager(abstractEntityClass);
			jpaRepository = new SimpleJpaRepository<S, Long>(abstractEntityClass, entityManager);
			JPA_REPOSITORY_MAP.put(abstractEntityClass, jpaRepository);
		}

		return jpaRepository;
	}

	private static <S extends AbstractEntity> EntityManager getEntityManager(Class<S> abstractEntityClass) {
		final JpaContext jpaContext = ApplicationContextUtil.getBean(JpaContext.class);
		final EntityManager entityManager = jpaContext.getEntityManagerByManagedType(abstractEntityClass);

		return entityManager;
	}

}

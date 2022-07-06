package com.aliuken.jobvacanciesapp.repository.superinterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.aliuken.jobvacanciesapp.model.AbstractEntity;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.util.ApplicationContextUtil;
import com.aliuken.jobvacanciesapp.util.StringUtils;

@NoRepositoryBean
public interface JpaRepositoryWithPaginationAndSorting<T extends AbstractEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
	public static final Map<Class<? extends AbstractEntity>, SimpleJpaRepository<? extends AbstractEntity, Long>> SIMPLE_JPA_REPOSITORY_MAP = new HashMap<>();

	abstract Class<T> getEntityClass();
	
	default T findByIdNotOptional(Long id) {
		final T entity;
		if(id != null) {
			final Optional<T> entityOptional = this.findById(id);
			if (entityOptional.isPresent()) {
				entity = entityOptional.get();
			} else {
				entity = null;
			}
		} else {
			entity = null;
		}
		
		return entity;
	}
	
	default Page<T> findAll(Pageable pageable, TableOrder tableOrder) {
		final Class<T> entityClass = this.getEntityClass();
		final Pageable finalPageable = this.getFinalPageable(pageable, tableOrder, entityClass);
		final Page<T> page = findAll(finalPageable);

		return page;
	}

	default <S extends T> Page<S> findAll(Pageable pageable, TableOrder tableOrder, Example<S> abstractEntityExample) {
		if(abstractEntityExample == null) {
			throw new IllegalArgumentException("abstractEntityExample can not be null");
		}

		Class<S> abstractEntityClass = abstractEntityExample.getProbeType();
		final SimpleJpaRepository<S, Long> simpleJpaRepository = JpaRepositoryWithPaginationAndSorting.getSimpleJpaRepository(abstractEntityClass);
		final Pageable finalPageable = this.getFinalPageable(pageable, tableOrder, abstractEntityClass);
		final Page<S> page = simpleJpaRepository.findAll(abstractEntityExample, finalPageable);

		return page;
	}

	default Page<T> findAll(Pageable pageable, TableOrder tableOrder, Specification<T> abstractEntitySpecification) {
		if(abstractEntitySpecification == null) {
			throw new IllegalArgumentException("abstractEntitySpecification can not be null");
		}

		final Class<T> entityClass = this.getEntityClass();
		final Pageable finalPageable = this.getFinalPageable(pageable, tableOrder, entityClass);
		final Page<T> page = this.findAll(abstractEntitySpecification, finalPageable);

		return page;
	}

	private <S extends T> Pageable getFinalPageable(Pageable pageable, TableOrder tableOrder, Class<S> abstractEntityClass) {
		if(pageable == null) {
			throw new IllegalArgumentException("pageable can not be null");
		}

		final Pageable finalPageable;
		if(tableOrder == null) {
			finalPageable = pageable;
		} else if(TableOrder.ID_ASC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));
		} else if(TableOrder.ID_DESC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
		} else if(TableOrder.FIRST_REGISTRATION_DATE_TIME_ASC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "firstRegistrationDateTime"));
		} else if(TableOrder.FIRST_REGISTRATION_DATE_TIME_DESC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "firstRegistrationDateTime"));
		} else if(TableOrder.FIRST_REGISTRATION_AUTH_USER_EMAIL_ASC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "firstRegistrationAuthUser.email"));
		} else if(TableOrder.FIRST_REGISTRATION_AUTH_USER_EMAIL_DESC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "firstRegistrationAuthUser.email"));
		} else if(TableOrder.LAST_MODIFICATION_DATE_TIME_ASC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "lastModificationDateTime"));
		} else if(TableOrder.LAST_MODIFICATION_DATE_TIME_DESC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "lastModificationDateTime"));
		} else if(TableOrder.LAST_MODIFICATION_AUTH_USER_EMAIL_ASC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "lastModificationAuthUser.email"));
		} else if(TableOrder.LAST_MODIFICATION_AUTH_USER_EMAIL_DESC.equals(tableOrder)) {
			finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "lastModificationAuthUser.email"));
		} else if(TableOrder.EMAIL_ASC.equals(tableOrder)) {
			if(AuthUser.class.equals(abstractEntityClass)) {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "email"));
			} else {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "authUser.email"));
			}
		} else if(TableOrder.EMAIL_DESC.equals(tableOrder)) {
			if(AuthUser.class.equals(abstractEntityClass)) {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "email"));
			} else {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "authUser.email"));
			}
		} else if(TableOrder.NAME_ASC.equals(tableOrder)) {
			if(AuthUser.class.equals(abstractEntityClass)) {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "name"));
			} else {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "authUser.name"));
			}
		} else if(TableOrder.NAME_DESC.equals(tableOrder)) {
			if(AuthUser.class.equals(abstractEntityClass)) {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "name"));
			} else {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "authUser.name"));
			}
		} else if(TableOrder.SURNAMES_ASC.equals(tableOrder)) {
			if(AuthUser.class.equals(abstractEntityClass)) {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "surnames"));
			} else {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "authUser.surnames"));
			}
		} else if(TableOrder.SURNAMES_DESC.equals(tableOrder)) {
			if(AuthUser.class.equals(abstractEntityClass)) {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "surnames"));
			} else {
				finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "authUser.surnames"));
			}
		} else {
			throw new IllegalArgumentException(StringUtils.getStringJoined("TableOrder '", tableOrder.name(), "' not supported"));
		}

		return finalPageable;
	}

	@SuppressWarnings("unchecked")
	private static <S extends AbstractEntity> SimpleJpaRepository<S, Long> getSimpleJpaRepository(Class<S> abstractEntityClass) {
		SimpleJpaRepository<S, Long> simpleJpaRepository = (SimpleJpaRepository<S, Long>) SIMPLE_JPA_REPOSITORY_MAP.get(abstractEntityClass);
		if(simpleJpaRepository == null) {
			final JpaContext jpaContext = ApplicationContextUtil.getBean(JpaContext.class);
			final EntityManager entityManager = jpaContext.getEntityManagerByManagedType(abstractEntityClass);
			simpleJpaRepository = new SimpleJpaRepository<S, Long>(abstractEntityClass, entityManager);
			SIMPLE_JPA_REPOSITORY_MAP.put(abstractEntityClass, simpleJpaRepository);
		}

		return simpleJpaRepository;
	}

}

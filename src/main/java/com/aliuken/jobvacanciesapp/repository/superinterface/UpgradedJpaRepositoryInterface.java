package com.aliuken.jobvacanciesapp.repository.superinterface;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

public interface UpgradedJpaRepositoryInterface<T extends AbstractEntity<T>> {
	public abstract <S extends T> S saveAndFlush(S entity);

	public abstract void deleteByIdAndFlush(Long entityId);

	public abstract List<T> findAll();

	public abstract T findByIdNotOptional(Long entityId);

	public abstract T findByIdOrNewEntity(Long entityId);

	public abstract Page<T> findAll(Pageable pageable);

	public abstract Page<T> findAll(Pageable pageable, TableField tableSortingField, TableSortingDirection tableSortingDirection);

	public abstract Page<T> findAll(Pageable pageable, TableField tableSortingField, TableSortingDirection tableSortingDirection, Specification<T> specification);

	public abstract Page<T> findAll(Specification<T> specification, Pageable pageable);

	public abstract <S extends T> List<S> findAll(Example<S> example);

	public abstract <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

	public abstract <S extends T> Page<S> findAll(Example<S> example, Pageable pageable, TableField tableSortingField, TableSortingDirection tableSortingDirection);

	public abstract T refreshEntity(T entity);

	public abstract T executeQuerySingleResult(String jpqlQuery, Map<String, Object> parameterMap);

	public abstract List<T> executeQueryResultList(String jpqlQuery, Map<String, Object> parameterMap);

	public abstract int executeUpdate(String jpqlQuery, Map<String, Object> parameterMap);

}

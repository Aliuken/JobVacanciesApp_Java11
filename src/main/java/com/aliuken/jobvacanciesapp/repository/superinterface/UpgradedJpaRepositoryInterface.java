package com.aliuken.jobvacanciesapp.repository.superinterface;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

public interface UpgradedJpaRepositoryInterface<T extends AbstractEntity<T>> {
	public abstract <S extends T> @NonNull S saveAndFlush(@NonNull S entity);

	public abstract void deleteByIdAndFlush(Long entityId);

	public abstract @NonNull List<T> findAll();

	public abstract T findByIdNotOptional(Long entityId);

	public abstract T findByIdOrNewEntity(Long entityId);

	public abstract @NonNull Page<T> findAll(@NonNull Pageable pageable);

	public abstract @NonNull Page<T> findAll(@NonNull Pageable pageable, TableField sortingTableField, TableSortingDirection tableSortingDirection);

	public abstract @NonNull Page<T> findAll(@NonNull Pageable pageable, TableField sortingTableField, TableSortingDirection tableSortingDirection, Specification<T> specification);

	public abstract @NonNull Page<T> findAll(Specification<T> specification, final @NonNull Pageable pageable);

	public abstract <S extends T> @NonNull List<S> findAll(@NonNull Example<S> example);

	public abstract <S extends T> @NonNull Page<S> findAll(@NonNull Example<S> example, final @NonNull Pageable pageable);

	public abstract <S extends T> @NonNull Page<S> findAll(@NonNull Example<S> example, final @NonNull Pageable pageable, TableField sortingTableField, TableSortingDirection tableSortingDirection);

	public abstract T refreshEntity(T entity);

	public abstract T executeQuerySingleResult(@NonNull String jpqlQuery, Map<String, Object> parameterMap);

	public abstract @NonNull List<T> executeQueryResultList(@NonNull String jpqlQuery, Map<String, Object> parameterMap);

	public abstract int executeUpdate(@NonNull String jpqlQuery, Map<String, Object> parameterMap);

}

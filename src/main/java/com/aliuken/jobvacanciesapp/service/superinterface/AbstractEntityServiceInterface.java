package com.aliuken.jobvacanciesapp.service.superinterface;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.aliuken.jobvacanciesapp.model.AbstractEntity;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;

public interface AbstractEntityServiceInterface<T extends AbstractEntity> {
	Class<T> getEntityClass();

	T saveAndFlush(T entity);

	void deleteByIdAndFlush(Long entityId);

	List<T> findAll();

	T findByIdNotOptional(Long entityId);

	Page<T> findAll(Pageable pageable);

	Page<T> findAll(Pageable pageable, TableOrder tableOrder);

	Page<T> findAll(Pageable pageable, TableOrder tableOrder, Specification<T> specification);

	Page<T> findAll(Example<T> example, Pageable pageable);

	Page<T> findAll(Example<T> example, Pageable pageable, TableOrder tableOrder);

}

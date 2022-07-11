package com.aliuken.jobvacanciesapp.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AbstractEntity;

@Repository
public class AbstractEntityRepositoryImpl<T extends AbstractEntity> implements AbstractEntityRepository<T> {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public T refreshEntity(T abstractEntity) {
		if (abstractEntity == null) {
			return null;
		}

		Long abstractEntityId = abstractEntity.getId();
		if (abstractEntityId == null) {
			return null;
		}

		Class<T> abstractEntityClass = (Class<T>) abstractEntity.getClass();
		abstractEntity = entityManager.find(abstractEntityClass, abstractEntityId);
		return abstractEntity;
	}
}

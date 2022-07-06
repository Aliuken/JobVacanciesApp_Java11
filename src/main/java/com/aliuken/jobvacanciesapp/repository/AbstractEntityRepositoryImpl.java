package com.aliuken.jobvacanciesapp.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AbstractEntity;

@Repository
public class AbstractEntityRepositoryImpl<T extends AbstractEntity> implements AbstractEntityRepository<T> {
	@PersistenceContext
    private EntityManager entityManager;
	
	public T refreshEntity(T abstractEntity) {
		if(abstractEntity != null) {
			abstractEntity = entityManager.merge(abstractEntity);
			entityManager.refresh(abstractEntity);
		}
		return abstractEntity;
	}
}

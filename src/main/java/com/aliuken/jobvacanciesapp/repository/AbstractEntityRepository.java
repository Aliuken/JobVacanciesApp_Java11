package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.model.AbstractEntity;

public interface AbstractEntityRepository<T extends AbstractEntity> {
	public T refreshEntity(T abstractEntity);
}

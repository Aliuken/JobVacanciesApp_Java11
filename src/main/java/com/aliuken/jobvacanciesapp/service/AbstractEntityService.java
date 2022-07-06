package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.AbstractEntity;

public interface AbstractEntityService<T extends AbstractEntity> {
	public T refreshEntity(T abstractEntity);
}

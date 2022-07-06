package com.aliuken.jobvacanciesapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliuken.jobvacanciesapp.model.AbstractEntity;
import com.aliuken.jobvacanciesapp.repository.AbstractEntityRepository;

@Service("abstractEntityService")
@Transactional
public class AbstractEntityServiceImpl<T extends AbstractEntity> implements AbstractEntityService<T> {

	@Autowired
	private AbstractEntityRepository<T> abstractEntityRepository;

	@Override
	public T refreshEntity(T abstractEntity) {
		abstractEntity = abstractEntityRepository.refreshEntity(abstractEntity);
		return abstractEntity;
	}

}

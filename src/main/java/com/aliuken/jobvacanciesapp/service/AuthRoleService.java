package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.service.superclass.AbstractEntityServiceSuperclass;

public abstract class AuthRoleService extends AbstractEntityServiceSuperclass<AuthRole> {

	public abstract AuthRole findByName(final String name);

}

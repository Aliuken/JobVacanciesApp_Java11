package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;
import com.aliuken.jobvacanciesapp.service.superclass.AbstractEntityWithAuthUserServiceSuperclass;

public abstract class AuthUserRoleService extends AbstractEntityWithAuthUserServiceSuperclass<AuthUserRole> {

	public abstract AuthUserRole findByAuthUserAndAuthRole(final AuthUser authUser, final AuthRole authRole);

}
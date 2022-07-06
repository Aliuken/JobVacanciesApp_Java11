package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.AuthRole;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserRole;
import com.aliuken.jobvacanciesapp.service.superinterface.AbstractEntityWithAuthUserExtraServiceInterface;

public interface AuthUserRoleService extends AbstractEntityWithAuthUserExtraServiceInterface<AuthUserRole> {

	AuthUserRole findByAuthUserAndAuthRole(AuthUser authUser, AuthRole authRole);

}
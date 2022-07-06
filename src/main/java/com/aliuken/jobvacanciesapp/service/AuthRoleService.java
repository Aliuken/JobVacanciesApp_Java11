package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.AuthRole;
import com.aliuken.jobvacanciesapp.service.superinterface.AbstractEntityExtraServiceInterface;

public interface AuthRoleService extends AbstractEntityExtraServiceInterface<AuthRole> {

	AuthRole findByName(String name);

}

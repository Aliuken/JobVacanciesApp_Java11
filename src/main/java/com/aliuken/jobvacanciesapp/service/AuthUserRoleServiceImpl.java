package com.aliuken.jobvacanciesapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliuken.jobvacanciesapp.model.AuthRole;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserRole;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.repository.AuthUserRoleRepository;

@Service
@Transactional
public class AuthUserRoleServiceImpl implements AuthUserRoleService {

	@Autowired
	private AuthUserRoleRepository authUserRoleRepository;

	@Override
	public Class<AuthUserRole> getEntityClass() {
		return AuthUserRole.class;
	}

	@Override
	public AuthUserRole saveAndFlush(AuthUserRole authUserRole) {
		return authUserRoleRepository.saveAndFlush(authUserRole);
	}

	@Override
	public void deleteByIdAndFlush(Long authUserRoleId) {
		authUserRoleRepository.deleteByIdAndFlush(authUserRoleId);
	}

	@Override
	public List<AuthUserRole> findAll() {
		return authUserRoleRepository.findAll();
	}

	@Override
	public AuthUserRole findByIdNotOptional(Long authUserRoleId) {
		return authUserRoleRepository.findByIdNotOptional(authUserRoleId);
	}

	@Override
	public AuthUserRole findByAuthUserAndAuthRole(AuthUser authUser, AuthRole authRole) {
		return authUserRoleRepository.findByAuthUserAndAuthRole(authUser, authRole);
	}

	@Override
	public Page<AuthUserRole> findAll(Pageable pageable) {
		return authUserRoleRepository.findAll(pageable);
	}

	@Override
	public Page<AuthUserRole> findAll(Pageable pageable, TableOrder tableOrder) {
		return authUserRoleRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<AuthUserRole> findAll(Pageable pageable, TableOrder tableOrder, Specification<AuthUserRole> specification) {
		return authUserRoleRepository.findAll(pageable, tableOrder, specification);
	}

	@Override
	public Page<AuthUserRole> findAll(Example<AuthUserRole> example, Pageable pageable) {
		return authUserRoleRepository.findAll(example, pageable);
	}

	@Override
	public Page<AuthUserRole> findAll(Example<AuthUserRole> example, Pageable pageable, TableOrder tableOrder) {
		return authUserRoleRepository.findAll(example, pageable, tableOrder);
	}

	@Override
	public AuthUserRole getNewEntityWithGenericData(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser) {
		AuthUserRole authUserRole = new AuthUserRole();
		authUserRole.setId(id);
		authUserRole.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authUserRole.setLastModificationAuthUser(lastModificationAuthUser);

		return authUserRole;
	}

	@Override
	public AuthUserRole getNewEntityWithAuthUserEmail(String authUserEmail) {
		AuthUser authUser = new AuthUser();
		authUser.setEmail(authUserEmail);

		AuthUserRole authUserRole = new AuthUserRole();
		authUserRole.setAuthUser(authUser);

		return authUserRole;
	}

	@Override
	public AuthUserRole getNewEntityWithAuthUserName(String authUserName) {
		AuthUser authUser = new AuthUser();
		authUser.setName(authUserName);

		AuthUserRole authUserRole = new AuthUserRole();
		authUserRole.setAuthUser(authUser);

		return authUserRole;
	}

	@Override
	public AuthUserRole getNewEntityWithAuthUserSurnames(String authUserSurnames) {
		AuthUser authUser = new AuthUser();
		authUser.setSurnames(authUserSurnames);

		AuthUserRole authUserRole = new AuthUserRole();
		authUserRole.setAuthUser(authUser);

		return authUserRole;
	}

}

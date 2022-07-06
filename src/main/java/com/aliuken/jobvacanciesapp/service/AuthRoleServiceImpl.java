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
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.repository.AuthRoleRepository;

@Service
@Transactional
public class AuthRoleServiceImpl implements AuthRoleService {

	@Autowired
	private AuthRoleRepository authRoleRepository;

	@Override
	public Class<AuthRole> getEntityClass() {
		return AuthRole.class;
	}

	@Override
	public void save(AuthRole authRole) {
		authRoleRepository.save(authRole);
	}

	@Override
	public void deleteById(Long authRoleId) {
		authRoleRepository.deleteById(authRoleId);
	}

	@Override
	public List<AuthRole> findAll() {
		return authRoleRepository.findAll();
	}

	@Override
	public AuthRole findById(Long authRoleId) {
		return authRoleRepository.findByIdNotOptional(authRoleId);
	}

	@Override
	public AuthRole findByName(String name) {
		return authRoleRepository.findByName(name);
	}

	@Override
	public Page<AuthRole> findAll(Pageable pageable) {
		return authRoleRepository.findAll(pageable);
	}

	@Override
	public Page<AuthRole> findAll(Pageable pageable, Example<AuthRole> example) {
		return authRoleRepository.findAll(example, pageable);
	}

	@Override
	public Page<AuthRole> findAll(Pageable pageable, TableOrder tableOrder) {
		return authRoleRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<AuthRole> findAll(Pageable pageable, TableOrder tableOrder, Example<AuthRole> example) {
		return authRoleRepository.findAll(pageable, tableOrder, example);
	}

	@Override
	public Page<AuthRole> findAll(Pageable pageable, TableOrder tableOrder, Specification<AuthRole> specification) {
		return authRoleRepository.findAll(pageable, tableOrder, specification);
	}

	@Override
	public AuthRole getNewEntityWithGenericData(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser) {
		AuthRole authRole = new AuthRole();
		authRole.setId(id);
		authRole.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authRole.setLastModificationAuthUser(lastModificationAuthUser);

		return authRole;
	}

}

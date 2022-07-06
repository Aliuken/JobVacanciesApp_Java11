package com.aliuken.jobvacanciesapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.repository.AuthUserRepository;

@Service
@Transactional
public class AuthUserServiceImpl implements AuthUserService {

	@Autowired
	private AuthUserRepository authUserRepository;

	@Override
	public Class<AuthUser> getEntityClass() {
		return AuthUser.class;
	}

	@Override
	public void save(AuthUser authUser) {
		authUserRepository.save(authUser);
	}

	@Override
	public void deleteById(Long authUserId) {
		authUserRepository.deleteById(authUserId);
	}

	@Override
	public List<AuthUser> findAll() {
		return authUserRepository.findAll();
	}

	@Override
	public AuthUser findById(Long authUserId) {
		return authUserRepository.findByIdNotOptional(authUserId);
	}

	@Override
	public AuthUser findByEmail(String email) {
		return authUserRepository.findByEmail(email);
	}

	@Transactional
	@Override
	public int lock(Long authUserId) {
		final int rows = authUserRepository.lock(authUserId);
		return rows;
	}

	@Transactional
	@Override
	public int unlock(Long authUserId) {
		final int rows = authUserRepository.unlock(authUserId);
		return rows;
	}

	@Override
	public Page<AuthUser> findAll(Pageable pageable) {
		return authUserRepository.findAll(pageable);
	}

	@Override
	public Page<AuthUser> findAll(Pageable pageable, Example<AuthUser> example) {
		return authUserRepository.findAll(example, pageable);
	}

	@Override
	public Page<AuthUser> findAll(Pageable pageable, TableOrder tableOrder) {
		return authUserRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<AuthUser> findAll(Pageable pageable, TableOrder tableOrder, Example<AuthUser> example) {
		return authUserRepository.findAll(pageable, tableOrder, example);
	}

	@Override
	public Page<AuthUser> findAll(Pageable pageable, TableOrder tableOrder, Specification<AuthUser> specification) {
		return authUserRepository.findAll(pageable, tableOrder, specification);
	}
	
	@Override
	public AuthUser getNewEntityWithGenericData(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser) {
		AuthUser authUser = new AuthUser();
		authUser.setId(id);
		authUser.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authUser.setLastModificationAuthUser(lastModificationAuthUser);

		return authUser;
	}

}

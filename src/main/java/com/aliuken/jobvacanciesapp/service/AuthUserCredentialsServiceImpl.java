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
import com.aliuken.jobvacanciesapp.model.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.repository.AuthUserCredentialsRepository;

@Service
@Transactional
public class AuthUserCredentialsServiceImpl implements AuthUserCredentialsService {

	@Autowired
	private AuthUserCredentialsRepository authUserCredentialsRepository;

	@Override
	public Class<AuthUserCredentials> getEntityClass() {
		return AuthUserCredentials.class;
	}

	@Override
	public void save(AuthUserCredentials authUserCredentials) {
		authUserCredentialsRepository.save(authUserCredentials);
	}

	@Override
	public void deleteById(Long authUserCredentialsId) {
		authUserCredentialsRepository.deleteById(authUserCredentialsId);
	}

	@Override
	public List<AuthUserCredentials> findAll() {
		return authUserCredentialsRepository.findAll();
	}

	@Override
	public AuthUserCredentials findById(Long authUserCredentialsId) {
		return authUserCredentialsRepository.findByIdNotOptional(authUserCredentialsId);
	}

	@Override
	public AuthUserCredentials findByEmail(String email) {
		return authUserCredentialsRepository.findByEmail(email);
	}

	@Override
	public Page<AuthUserCredentials> findAll(Pageable pageable) {
		return authUserCredentialsRepository.findAll(pageable);
	}

	@Override
	public Page<AuthUserCredentials> findAll(Pageable pageable, Example<AuthUserCredentials> example) {
		return authUserCredentialsRepository.findAll(example, pageable);
	}

	@Override
	public Page<AuthUserCredentials> findAll(Pageable pageable, TableOrder tableOrder) {
		return authUserCredentialsRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<AuthUserCredentials> findAll(Pageable pageable, TableOrder tableOrder, Example<AuthUserCredentials> example) {
		return authUserCredentialsRepository.findAll(pageable, tableOrder, example);
	}

	@Override
	public Page<AuthUserCredentials> findAll(Pageable pageable, TableOrder tableOrder, Specification<AuthUserCredentials> specification) {
		return authUserCredentialsRepository.findAll(pageable, tableOrder, specification);
	}
	
	@Override
	public AuthUserCredentials getNewEntityWithGenericData(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser) {
		AuthUserCredentials authUserCredentials = new AuthUserCredentials();
		authUserCredentials.setId(id);
		authUserCredentials.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authUserCredentials.setLastModificationAuthUser(lastModificationAuthUser);

		return authUserCredentials;
	}

}

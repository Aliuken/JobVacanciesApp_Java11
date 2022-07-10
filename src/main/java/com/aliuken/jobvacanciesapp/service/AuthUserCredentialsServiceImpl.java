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
	public AuthUserCredentials findByEmail(String email) {
		return authUserCredentialsRepository.findByEmail(email);
	}

	@Override
	public AuthUserCredentials findByEmailAndEncryptedPassword(String email, String encryptedPassword) {
		return authUserCredentialsRepository.findByEmailAndEncryptedPassword(email, encryptedPassword);
	}

	@Override
	public Class<AuthUserCredentials> getEntityClass() {
		return AuthUserCredentials.class;
	}

	@Override
	public AuthUserCredentials saveAndFlush(AuthUserCredentials authUserCredentials) {
		return authUserCredentialsRepository.saveAndFlush(authUserCredentials);
	}

	@Override
	public void deleteByIdAndFlush(Long authUserCredentialsId) {
		authUserCredentialsRepository.deleteByIdAndFlush(authUserCredentialsId);
	}

	@Override
	public List<AuthUserCredentials> findAll() {
		return authUserCredentialsRepository.findAll();
	}

	@Override
	public AuthUserCredentials findByIdNotOptional(Long authUserCredentialsId) {
		return authUserCredentialsRepository.findByIdNotOptional(authUserCredentialsId);
	}

	@Override
	public Page<AuthUserCredentials> findAll(Pageable pageable) {
		return authUserCredentialsRepository.findAll(pageable);
	}

	@Override
	public Page<AuthUserCredentials> findAll(Pageable pageable, TableOrder tableOrder) {
		return authUserCredentialsRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<AuthUserCredentials> findAll(Pageable pageable, TableOrder tableOrder, Specification<AuthUserCredentials> specification) {
		return authUserCredentialsRepository.findAll(pageable, tableOrder, specification);
	}

	@Override
	public Page<AuthUserCredentials> findAll(Example<AuthUserCredentials> example, Pageable pageable) {
		return authUserCredentialsRepository.findAll(example, pageable);
	}

	@Override
	public Page<AuthUserCredentials> findAll(Example<AuthUserCredentials> example, Pageable pageable, TableOrder tableOrder) {
		return authUserCredentialsRepository.findAll(example, pageable, tableOrder);
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

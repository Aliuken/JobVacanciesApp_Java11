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
import com.aliuken.jobvacanciesapp.model.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.repository.AuthUserConfirmationRepository;

@Service
@Transactional
public class AuthUserConfirmationServiceImpl implements AuthUserConfirmationService {

	@Autowired
	private AuthUserConfirmationRepository authUserConfirmationRepository;

	@Override
	public Class<AuthUserConfirmation> getEntityClass() {
		return AuthUserConfirmation.class;
	}

	@Override
	public void save(AuthUserConfirmation authUserConfirmation) {
		authUserConfirmationRepository.save(authUserConfirmation);
	}

	@Override
	public void deleteById(Long authUserConfirmationId) {
		authUserConfirmationRepository.deleteById(authUserConfirmationId);
	}

	@Override
	public List<AuthUserConfirmation> findAll() {
		return authUserConfirmationRepository.findAll();
	}

	@Override
	public AuthUserConfirmation findById(Long authUserConfirmationId) {
		return authUserConfirmationRepository.findByIdNotOptional(authUserConfirmationId);
	}

	@Override
	public AuthUserConfirmation findByEmail(String email) {
		return authUserConfirmationRepository.findByEmail(email);
	}

	@Override
	public AuthUserConfirmation findByEmailAndUuid(String email, String uuid) {
		return authUserConfirmationRepository.findByEmailAndUuid(email, uuid);
	}

	@Override
	public Page<AuthUserConfirmation> findAll(Pageable pageable) {
		return authUserConfirmationRepository.findAll(pageable);
	}

	@Override
	public Page<AuthUserConfirmation> findAll(Pageable pageable, Example<AuthUserConfirmation> example) {
		return authUserConfirmationRepository.findAll(example, pageable);
	}

	@Override
	public Page<AuthUserConfirmation> findAll(Pageable pageable, TableOrder tableOrder) {
		return authUserConfirmationRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<AuthUserConfirmation> findAll(Pageable pageable, TableOrder tableOrder, Example<AuthUserConfirmation> example) {
		return authUserConfirmationRepository.findAll(pageable, tableOrder, example);
	}

	@Override
	public Page<AuthUserConfirmation> findAll(Pageable pageable, TableOrder tableOrder, Specification<AuthUserConfirmation> specification) {
		return authUserConfirmationRepository.findAll(pageable, tableOrder, specification);
	}
	
	@Override
	public AuthUserConfirmation getNewEntityWithGenericData(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser) {
		AuthUserConfirmation authUserConfirmation = new AuthUserConfirmation();
		authUserConfirmation.setId(id);
		authUserConfirmation.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authUserConfirmation.setLastModificationAuthUser(lastModificationAuthUser);

		return authUserConfirmation;
	}

}

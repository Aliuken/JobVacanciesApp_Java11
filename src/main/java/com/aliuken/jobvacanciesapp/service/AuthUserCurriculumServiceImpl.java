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
import com.aliuken.jobvacanciesapp.model.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.repository.AuthUserCurriculumRepository;

@Service
@Transactional
public class AuthUserCurriculumServiceImpl implements AuthUserCurriculumService {

	@Autowired
	private AuthUserCurriculumRepository authUserCurriculumRepository;

	@Override
	public Class<AuthUserCurriculum> getEntityClass() {
		return AuthUserCurriculum.class;
	}

	@Override
	public void save(AuthUserCurriculum authUserCurriculum) {
		authUserCurriculumRepository.save(authUserCurriculum);
	}

	@Override
	public void deleteById(Long authUserCurriculumId) {
		authUserCurriculumRepository.deleteById(authUserCurriculumId);
	}

	@Override
	public List<AuthUserCurriculum> findAll() {
		return authUserCurriculumRepository.findAll();
	}

	@Override
	public AuthUserCurriculum findById(Long authUserCurriculumId) {
		return authUserCurriculumRepository.findByIdNotOptional(authUserCurriculumId);
	}

	@Override
	public Page<AuthUserCurriculum> findAll(Pageable pageable) {
		return authUserCurriculumRepository.findAll(pageable);
	}

	@Override
	public Page<AuthUserCurriculum> findAll(Pageable pageable, Example<AuthUserCurriculum> example) {
		return authUserCurriculumRepository.findAll(example, pageable);
	}

	@Override
	public Page<AuthUserCurriculum> findAll(Pageable pageable, TableOrder tableOrder) {
		return authUserCurriculumRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<AuthUserCurriculum> findAll(Pageable pageable, TableOrder tableOrder, Example<AuthUserCurriculum> example) {
		return authUserCurriculumRepository.findAll(pageable, tableOrder, example);
	}

	@Override
	public Page<AuthUserCurriculum> findAll(Pageable pageable, TableOrder tableOrder, Specification<AuthUserCurriculum> specification) {
		return authUserCurriculumRepository.findAll(pageable, tableOrder, specification);
	}
	
	@Override
	public AuthUserCurriculum getNewEntityWithGenericData(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser) {
		AuthUserCurriculum authUserCurriculum = new AuthUserCurriculum();
		authUserCurriculum.setId(id);
		authUserCurriculum.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authUserCurriculum.setLastModificationAuthUser(lastModificationAuthUser);

		return authUserCurriculum;
	}

	@Override
	public AuthUserCurriculum getNewEntityWithAuthUserEmail(String authUserEmail) {
		AuthUser authUser = new AuthUser();
		authUser.setEmail(authUserEmail);

		AuthUserCurriculum authUserCurriculum = new AuthUserCurriculum();
		authUserCurriculum.setAuthUser(authUser);

		return authUserCurriculum;
	}

	@Override
	public AuthUserCurriculum getNewEntityWithAuthUserName(String authUserName) {
		AuthUser authUser = new AuthUser();
		authUser.setName(authUserName);

		AuthUserCurriculum authUserCurriculum = new AuthUserCurriculum();
		authUserCurriculum.setAuthUser(authUser);

		return authUserCurriculum;
	}

	@Override
	public AuthUserCurriculum getNewEntityWithAuthUserSurnames(String authUserSurnames) {
		AuthUser authUser = new AuthUser();
		authUser.setSurnames(authUserSurnames);

		AuthUserCurriculum authUserCurriculum = new AuthUserCurriculum();
		authUserCurriculum.setAuthUser(authUser);

		return authUserCurriculum;
	}

}

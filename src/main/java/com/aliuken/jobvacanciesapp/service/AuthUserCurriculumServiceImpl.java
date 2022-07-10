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
	public AuthUserCurriculum findByAuthUserAndFileName(AuthUser authUser, String fileName) {
		return authUserCurriculumRepository.findByAuthUserAndFileName(authUser, fileName);
	}

	@Override
	public Class<AuthUserCurriculum> getEntityClass() {
		return AuthUserCurriculum.class;
	}

	@Override
	public AuthUserCurriculum saveAndFlush(AuthUserCurriculum authUserCurriculum) {
		return authUserCurriculumRepository.saveAndFlush(authUserCurriculum);
	}

	@Override
	public void deleteByIdAndFlush(Long authUserCurriculumId) {
		authUserCurriculumRepository.deleteByIdAndFlush(authUserCurriculumId);
	}

	@Override
	public List<AuthUserCurriculum> findAll() {
		return authUserCurriculumRepository.findAll();
	}

	@Override
	public AuthUserCurriculum findByIdNotOptional(Long authUserCurriculumId) {
		return authUserCurriculumRepository.findByIdNotOptional(authUserCurriculumId);
	}

	@Override
	public Page<AuthUserCurriculum> findAll(Pageable pageable) {
		return authUserCurriculumRepository.findAll(pageable);
	}

	@Override
	public Page<AuthUserCurriculum> findAll(Pageable pageable, TableOrder tableOrder) {
		return authUserCurriculumRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<AuthUserCurriculum> findAll(Pageable pageable, TableOrder tableOrder, Specification<AuthUserCurriculum> specification) {
		return authUserCurriculumRepository.findAll(pageable, tableOrder, specification);
	}

	@Override
	public Page<AuthUserCurriculum> findAll(Example<AuthUserCurriculum> example, Pageable pageable) {
		return authUserCurriculumRepository.findAll(example, pageable);
	}

	@Override
	public Page<AuthUserCurriculum> findAll(Example<AuthUserCurriculum> example, Pageable pageable, TableOrder tableOrder) {
		return authUserCurriculumRepository.findAll(example, pageable, tableOrder);
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

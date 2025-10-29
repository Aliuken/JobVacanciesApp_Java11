package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.repository.superinterface.JdbcTokenByEmailRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class JdbcTokenByEmailServiceImpl implements JdbcTokenByEmailService {

	@Autowired
	private JdbcTokenByEmailRepositoryInterface jdbcTokenByEmailRepository;

	@Override
	@ServiceMethod
	public void initDao() {
		jdbcTokenByEmailRepository.initDao();
	}

	@Override
	@ServiceMethod
	public void createNewToken(final PersistentRememberMeToken token) {
		jdbcTokenByEmailRepository.createNewToken(token);
	}

	@Override
	@ServiceMethod
	public void updateToken(final String series, final String tokenValue, final Date lastUsed) {
		jdbcTokenByEmailRepository.updateToken(series, tokenValue, lastUsed);
	}

	@Override
	@ServiceMethod
	public PersistentRememberMeToken getTokenForSeries(final String seriesId) {
		final PersistentRememberMeToken persistentRememberMeToken = jdbcTokenByEmailRepository.getTokenForSeries(seriesId);
		return persistentRememberMeToken;
	}

	@Override
	@ServiceMethod
	public void removeUserTokens(final String email) {
		jdbcTokenByEmailRepository.removeUserTokens(email);
	}

	@Override
	@ServiceMethod
	public void setCreateTableOnStartup(final boolean createTableOnStartup) {
		jdbcTokenByEmailRepository.setCreateTableOnStartup(createTableOnStartup);
	}
}

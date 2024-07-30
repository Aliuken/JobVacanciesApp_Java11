package com.aliuken.jobvacanciesapp.service;

import java.util.Date;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public interface JdbcTokenByEmailService extends PersistentTokenRepository {

	public abstract void initDao();

	public abstract void createNewToken(PersistentRememberMeToken token);

	public abstract void updateToken(String series, String tokenValue, Date lastUsed);

	public abstract PersistentRememberMeToken getTokenForSeries(String seriesId);

	public abstract void removeUserTokens(String email);

	public abstract void setCreateTableOnStartup(boolean createTableOnStartup);

}

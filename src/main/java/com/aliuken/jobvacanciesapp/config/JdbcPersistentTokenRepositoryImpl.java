package com.aliuken.jobvacanciesapp.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

/**
 * JDBC based persistent login token repository implementation.
 */
@Component
public class JdbcPersistentTokenRepositoryImpl extends JdbcUserDetailsManager implements PersistentTokenRepository {

	/** Default SQL for creating the database table to store the tokens */
	public static final String CREATE_TABLE_SQL = "create table auth_persistent_logins (email varchar(255) not null, series varchar(64) primary key, token varchar(64) not null, last_used timestamp not null)";

	/** The default SQL used by the <tt>getTokenBySeries</tt> query */
	public static final String DEF_TOKEN_BY_SERIES_SQL = "select email, series, token, last_used from auth_persistent_logins where series = ?";

	/** The default SQL used by <tt>createNewToken</tt> */
	public static final String DEF_INSERT_TOKEN_SQL = "insert into auth_persistent_logins (email, series, token, last_used) values (?,?,?,?)";

	/** The default SQL used by <tt>updateToken</tt> */
	public static final String DEF_UPDATE_TOKEN_SQL = "update auth_persistent_logins set token = ?, last_used = ? where series = ?";

	/** The default SQL used by <tt>removeUserTokens</tt> */
	public static final String DEF_REMOVE_USER_TOKENS_SQL = "delete from auth_persistent_logins where email = ?";

	private String tokensBySeriesSql = DEF_TOKEN_BY_SERIES_SQL;

	private String insertTokenSql = DEF_INSERT_TOKEN_SQL;

	private String updateTokenSql = DEF_UPDATE_TOKEN_SQL;

	private String removeUserTokensSql = DEF_REMOVE_USER_TOKENS_SQL;

	private boolean createTableOnStartup = false;

	public JdbcPersistentTokenRepositoryImpl(@Autowired DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	@Override
	protected void initDao() {
		if (this.createTableOnStartup) {
			getJdbcTemplate().execute(CREATE_TABLE_SQL);
		}
	}

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		getJdbcTemplate().update(this.insertTokenSql, token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		getJdbcTemplate().update(this.updateTokenSql, tokenValue, lastUsed, series);
	}

	/**
	 * Loads the token data for the supplied series identifier.
	 *
	 * If an error occurs, it will be reported and null will be returned (since the result
	 * should just be a failed persistent login).
	 * @param seriesId
	 * @return the token matching the series, or null if no match found or an exception occurred.
	 */
	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		try {
			return getJdbcTemplate().queryForObject(this.tokensBySeriesSql, this::createRememberMeToken, seriesId);
		} catch (EmptyResultDataAccessException ex) {
			this.logger.debug(LogMessage.format("Querying token for series '%s' returned no results.", seriesId), ex);
		} catch (IncorrectResultSizeDataAccessException ex) {
			this.logger.error(LogMessage.format("Querying token for series '%s' returned more than one value. Series should be unique", seriesId));
		} catch (DataAccessException ex) {
			this.logger.error("Failed to load token for series " + seriesId, ex);
		}
		return null;
	}

	private PersistentRememberMeToken createRememberMeToken(ResultSet rs, int rowNum) throws SQLException {
		return new PersistentRememberMeToken(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4));
	}

	@Override
	public void removeUserTokens(String email) {
		getJdbcTemplate().update(this.removeUserTokensSql, email);
	}

	/**
	 * Intended for convenience in debugging. Will create the persistent_tokens database
	 * table when the class is initialized during the initDao method.
	 * @param createTableOnStartup set to true to execute the
	 */
	public void setCreateTableOnStartup(boolean createTableOnStartup) {
		this.createTableOnStartup = createTableOnStartup;
	}

}
package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.repository.superinterface.JdbcTokenByEmailRepositoryInterface;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * JDBC based persistent login token repository implementation.
 */
@Repository
@Slf4j
public class JdbcTokenByEmailRepository extends JdbcUserDetailsManager implements JdbcTokenByEmailRepositoryInterface {

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

	private boolean createTableOnStartup = false;

	public JdbcTokenByEmailRepository(@Autowired final DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	@Override
	@RepositoryMethod
	public void initDao() {
		if(this.createTableOnStartup) {
			getJdbcTemplate().execute(JdbcTokenByEmailRepository.CREATE_TABLE_SQL);
		}
	}

	@Override
	@RepositoryMethod
	public void createNewToken(final PersistentRememberMeToken token) {
		getJdbcTemplate().update(JdbcTokenByEmailRepository.DEF_INSERT_TOKEN_SQL, token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
	}

	@Override
	@RepositoryMethod
	public void updateToken(final String series, final String tokenValue, final Date lastUsed) {
		getJdbcTemplate().update(JdbcTokenByEmailRepository.DEF_UPDATE_TOKEN_SQL, tokenValue, lastUsed, series);
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
	@RepositoryMethod
	public PersistentRememberMeToken getTokenForSeries(final String seriesId) {
		try {
			return getJdbcTemplate().queryForObject(JdbcTokenByEmailRepository.DEF_TOKEN_BY_SERIES_SQL, this::createRememberMeToken, seriesId);
		} catch(final EmptyResultDataAccessException exception) {
			if(log.isDebugEnabled()) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
				log.debug(StringUtils.getStringJoined("Querying token for series '", seriesId, "' returned no results. Exception: ", rootCauseMessage));
			}
		} catch(final IncorrectResultSizeDataAccessException exception) {
			if(log.isErrorEnabled()) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
				log.error(StringUtils.getStringJoined("Querying token for series '", seriesId, "' returned more than one value. Series should be unique. Exception: ", rootCauseMessage));
			}
		} catch(final DataAccessException exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("Failed to load token for series '", seriesId, "'. Exception: ", stackTrace));
			}
		}
		return null;
	}

	private PersistentRememberMeToken createRememberMeToken(final ResultSet rs, final int rowNum) throws SQLException {
		return new PersistentRememberMeToken(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4));
	}

	@Override
	@RepositoryMethod
	public void removeUserTokens(final String email) {
		getJdbcTemplate().update(JdbcTokenByEmailRepository.DEF_REMOVE_USER_TOKENS_SQL, email);
	}

	/**
	 * Intended for convenience in debugging. Will create the persistent_tokens database
	 * table when the class is initialized during the initDao method.
	 * @param createTableOnStartup set to true to execute the
	 */
	@Override
	@RepositoryMethod
	public void setCreateTableOnStartup(final boolean createTableOnStartup) {
		this.createTableOnStartup = createTableOnStartup;
	}
}
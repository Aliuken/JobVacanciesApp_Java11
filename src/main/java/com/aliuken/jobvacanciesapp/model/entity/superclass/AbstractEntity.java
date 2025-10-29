package com.aliuken.jobvacanciesapp.model.entity.superclass;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.superinterface.AbstractEntityFieldsPrintable;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;
import com.aliuken.jobvacanciesapp.util.security.SessionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@Slf4j
public abstract class AbstractEntity implements Serializable, Comparable<AbstractEntity>, AbstractEntityFieldsPrintable {
	private static final long serialVersionUID = -1146558230499546161L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name="first_registration_date_time", nullable=false)
	private LocalDateTime firstRegistrationDateTime;

	//@NotNull
	@ManyToOne
	@JoinColumn(name="first_registration_auth_user_id")//, nullable=false)
	private AuthUser firstRegistrationAuthUser;

	@Column(name="last_modification_date_time")
	private LocalDateTime lastModificationDateTime;

	@ManyToOne
	@JoinColumn(name="last_modification_auth_user_id")
	private AuthUser lastModificationAuthUser;

	public AbstractEntity() {
		super();
	}

	@PrePersist
	private void prePersist() {
		firstRegistrationDateTime = LocalDateTime.now();
		firstRegistrationAuthUser = AbstractEntity.getSessionAuthUser();
		lastModificationDateTime = null;
		lastModificationAuthUser = null;
	}

	@PreUpdate
	private void preUpdate() {
		final AbstractEntity currentEntity = UpgradedJpaRepository.getEntityStatically(id, this.getClass());
		if(currentEntity != null) {
			firstRegistrationDateTime = currentEntity.getFirstRegistrationDateTime();
			firstRegistrationAuthUser = currentEntity.getFirstRegistrationAuthUser();
		} else {
			throw new IllegalArgumentException("Cannot find the entity with the given id to be updated");
		}

		lastModificationDateTime = LocalDateTime.now();
		lastModificationAuthUser = AbstractEntity.getSessionAuthUser();
	}

	public LocalDateTime getLastDateTime() {
		final LocalDateTime lastDateTime;
		if(lastModificationDateTime != null) {
			lastDateTime = lastModificationDateTime;
		} else {
			lastDateTime = firstRegistrationDateTime;
		}
		return lastDateTime;
	}

	public AuthUser getLastAuthUser() {
		final AuthUser lastAuthUser;
		if(lastModificationDateTime != null) {
			lastAuthUser = lastModificationAuthUser;
		} else {
			lastAuthUser = firstRegistrationAuthUser;
		}
		return lastAuthUser;
	}

	public String getIdString() {
		final String idString = Objects.toString(id);
		return idString;
	}

	public String getFirstRegistrationDateTimeString() {
		final String firstRegistrationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(firstRegistrationDateTime);
		return firstRegistrationDateTimeString;
	}

	public Long getFirstRegistrationAuthUserId() {
		final Long firstRegistrationAuthUserId = (firstRegistrationAuthUser != null) ? firstRegistrationAuthUser.getId() : null;
		return firstRegistrationAuthUserId;
	}

	public String getFirstRegistrationAuthUserEmail() {
		final String firstRegistrationAuthUserEmail = (firstRegistrationAuthUser != null) ? firstRegistrationAuthUser.getEmail() : null;
		return firstRegistrationAuthUserEmail;
	}

	public String getFirstRegistrationAuthUserEmail(final String defaultValue) {
		final String firstRegistrationAuthUserEmail = (firstRegistrationAuthUser != null) ? firstRegistrationAuthUser.getEmail() : defaultValue;
		return firstRegistrationAuthUserEmail;
	}

	public String getLastModificationDateTimeString() {
		final String lastModificationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(lastModificationDateTime);
		return lastModificationDateTimeString;
	}

	public Long getLastModificationAuthUserId() {
		final Long lastModificationAuthUserId = (lastModificationAuthUser != null) ? lastModificationAuthUser.getId() : null;
		return lastModificationAuthUserId;
	}

	public String getLastModificationAuthUserEmail() {
		final String lastModificationAuthUserEmail = (lastModificationAuthUser != null) ? lastModificationAuthUser.getEmail() : null;
		return lastModificationAuthUserEmail;
	}

	public String getLastModificationAuthUserEmail(final String defaultValue) {
		final String lastModificationAuthUserEmail = (lastModificationAuthUser != null) ? lastModificationAuthUser.getEmail() : defaultValue;
		return lastModificationAuthUserEmail;
	}

	@Override
	public String getCommonFields() {
		final String firstRegistrationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(firstRegistrationDateTime);
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(lastModificationDateTime);
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("insertDate: "), firstRegistrationDateTimeString, Constants.NEWLINE,
			StyleApplier.getBoldString("insertMail: "), firstRegistrationAuthUserEmail, Constants.NEWLINE,
			StyleApplier.getBoldString("updateDate: "), lastModificationDateTimeString, Constants.NEWLINE,
			StyleApplier.getBoldString("updateMail: "), lastModificationAuthUserEmail);
		return result;
	}

	public static AuthUser getSessionAuthUser() {
		AuthUser sessionAuthUser;
		try {
			sessionAuthUser = SessionUtils.getSessionAuthUserFromRequestContextHolder();
		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when trying to get the session user. Exception: ", stackTrace));
			}
			sessionAuthUser = null;
		}

		if(sessionAuthUser == null) {
			sessionAuthUser = UpgradedJpaRepository.getEntityStatically(Constants.ANONYMOUS_AUTH_USER_ID, AuthUser.class);
		}

		if(sessionAuthUser == null) {
			throw new IllegalArgumentException("Session user and anonymous user cannot be null");
		}

		return sessionAuthUser;
	}

	@Override
	public int compareTo(AbstractEntity abstractEntity) {
		if(this.id == null) {
			return -1;
		}
		if(abstractEntity == null || abstractEntity.id == null) {
			return 1;
		}
		return Long.compare(abstractEntity.id, this.id);
	}

	@Override
	public String toString() {
		final String idString = this.getIdString();
		final String firstRegistrationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(firstRegistrationDateTime);
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(lastModificationDateTime);
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AbstractEntity [id=", idString,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int result;
		if(id != null) {
			final Class<? extends AbstractEntity> entityClass = this.getClass();
			result = Objects.hash(entityClass, id);
		} else {
			result = System.identityHashCode(this);
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(this.getClass() != obj.getClass()) {
			return false;
		}

		final AbstractEntity other = (AbstractEntity) obj;

		final boolean result;
		if (id != null && other.id != null) {
			result = id.equals(other.id);
		} else {
			result = false;
		}
		return result;
	}
}

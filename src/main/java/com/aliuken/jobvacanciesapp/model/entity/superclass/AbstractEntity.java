package com.aliuken.jobvacanciesapp.model.entity.superclass;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.comparator.superclass.AbstractEntityDefaultComparator;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.superinterface.AbstractEntityFieldsPrintable;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;
import com.aliuken.jobvacanciesapp.util.security.SessionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@Slf4j
public abstract class AbstractEntity<T extends AbstractEntity<T>> implements Serializable, Comparable<T>, AbstractEntityFieldsPrintable {
	private static final long serialVersionUID = -1146558230499546161L;

	private final @NonNull Class<T> entityClass;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="first_registration_date_time", nullable=false)
	private @NonNull LocalDateTime firstRegistrationDateTime;

	@ManyToOne
	@JoinColumn(name="first_registration_auth_user_id", nullable=false)
	private @NonNull AuthUser firstRegistrationAuthUser;

	@Column(name="last_modification_date_time")
	private LocalDateTime lastModificationDateTime;

	@ManyToOne
	@JoinColumn(name="last_modification_auth_user_id")
	private AuthUser lastModificationAuthUser;

	public AbstractEntity() {
		super();
		final Class<?> initialEntityClass = this.getClass();
		entityClass = GenericsUtils.cast(initialEntityClass);
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
		final T currentEntity = UpgradedJpaRepository.getEntityStatically(id, entityClass);
		if(currentEntity != null) {
			firstRegistrationDateTime = currentEntity.getFirstRegistrationDateTime();
			firstRegistrationAuthUser = currentEntity.getFirstRegistrationAuthUser();
		} else {
			throw new IllegalArgumentException("Cannot find the entity with the given id to be updated");
		}

		lastModificationDateTime = LocalDateTime.now();
		lastModificationAuthUser = AbstractEntity.getSessionAuthUser();
	}

	public @NonNull LocalDateTime getLastDateTime() {
		final LocalDateTime lastDateTime;
		if(lastModificationDateTime != null) {
			lastDateTime = lastModificationDateTime;
		} else {
			lastDateTime = firstRegistrationDateTime;
		}
		return lastDateTime;
	}

	public @NonNull AuthUser getLastAuthUser() {
		final AuthUser lastAuthUser;
		if(lastModificationDateTime != null) {
			lastAuthUser = lastModificationAuthUser;
		} else {
			lastAuthUser = firstRegistrationAuthUser;
		}
		return lastAuthUser;
	}

	public @NonNull String getEntityClassName() {
		final String entityClassName = entityClass.getName();
		return entityClassName;
	}

	public @NonNull String getIdString() {
		final String idString = String.valueOf(id);
		return idString;
	}

	public String getFirstRegistrationDateTimeString() {
		final String firstRegistrationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(firstRegistrationDateTime);
		return firstRegistrationDateTimeString;
	}

	public Long getFirstRegistrationAuthUserId() {
		final Long firstRegistrationAuthUserId = firstRegistrationAuthUser.getId();
		return firstRegistrationAuthUserId;
	}

	public @NonNull String getFirstRegistrationAuthUserEmail() {
		final String firstRegistrationAuthUserEmail = firstRegistrationAuthUser.getEmail();
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

	public @NonNull String getLastModificationAuthUserEmail(final @NonNull String defaultValue) {
		final String lastModificationAuthUserEmail = (lastModificationAuthUser != null) ? lastModificationAuthUser.getEmail() : defaultValue;
		return lastModificationAuthUserEmail;
	}

	@Override
	public @NonNull String getCommonFields() {
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

	public static @NonNull AuthUser getSessionAuthUser() {
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
	public @NonNull String toString() {
		final String entityClassName = this.getEntityClassName();
		final String idString = this.getIdString();
		final String firstRegistrationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(firstRegistrationDateTime);
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(lastModificationDateTime);
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AbstractEntity [entityClass=", entityClassName, ", id=", idString,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}

	@Override
	public final int hashCode() {
		final int result = Objects.hash(this.getClass(), this.id);
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(this.getClass() != obj.getClass()) {
			return false;
		}

		final T other = GenericsUtils.cast(obj);

		final boolean result = Objects.equals(this.id, other.getId());
		return result;
	}

	@Override
	public final int compareTo(final @NonNull T other) {
		Objects.requireNonNull(other, "other cannot be null");
		final T thisEntity = GenericsUtils.cast(this);
		final AbstractEntityDefaultComparator.EntityComparators<T> entityComparators = AbstractEntityDefaultComparator.getEntityComparators(entityClass);
		final AbstractEntityDefaultComparator<T> currentDefaultComparator = entityComparators.getCurrentDefaultComparator();

		final int compareResult = currentDefaultComparator.compare(thisEntity, other);
		return compareResult;
	}
}

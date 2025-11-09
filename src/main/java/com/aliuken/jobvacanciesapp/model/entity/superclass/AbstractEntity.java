package com.aliuken.jobvacanciesapp.model.entity.superclass;

import com.aliuken.jobvacanciesapp.Constants;
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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;

@MappedSuperclass
@Getter
@Setter
@Slf4j
public abstract class AbstractEntity<T extends AbstractEntity<T>> implements Serializable, Comparable<T>, AbstractEntityFieldsPrintable {
	private static final long serialVersionUID = -1146558230499546161L;
	private static final int THIS_FIRST = -1;
	private static final int OTHER_FIRST = 1;
	public final Function<T, Integer> COMPARE_TO_ASC_FUNCTION = other -> compareTo(other, false);
	public final Function<T, Integer> COMPARE_TO_DESC_FUNCTION = other -> compareTo(other, true);

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
		final Class<?> initialEntityClass = this.getClass();
		final Class<T> entityClass = GenericsUtils.cast(initialEntityClass);
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
	public final int compareTo(T other) {
		//final int compareResult = COMPARE_TO_ASC_FUNCTION.apply(other);
		final int compareResult = this.compareTo(other, false);
		return compareResult;
	}

	/**
	 * Defines a natural ordering among entities.
	 * <p>
	 * Entities are ordered by:
	 * <ul>
	 *   <li>Class name (lexicographically)</li>
	 *   <li>ID value (ascending or descending)</li>
	 * </ul>
	 */
	private final int compareTo(T other, boolean descending) {
		final int direction = descending ? -1 : 1;

		if (other == null) {
			// In ascending order, nulls are sorted last; in descending, first.
			final int nullCompareResult = descending ? OTHER_FIRST : THIS_FIRST;
			return nullCompareResult;
		}

		final Class<?> thisClass = this.getClass();
		final Class<?> otherClass = other.getClass();
		if (thisClass != otherClass) {
			// Different classes are sorted by their names (including packages).
			final int classCompareResult = direction * thisClass.getName().compareTo(otherClass.getName());
			return classCompareResult;
		}

		final int idCompareResult = AbstractEntity.getIdCompareResult(this.id, other.getId(), descending);
		return idCompareResult;
	}

	private static int getIdCompareResult(Long thisId, Long otherId, boolean descending) {
		final int direction = descending ? -1 : 1;

		// In ascending order, entities with null ids are sorted last; in descending, first.
		final int idCompareResult;
		if (thisId == null && otherId == null) {
			idCompareResult = 0;
		} else if (thisId == null) {
			idCompareResult = descending ? THIS_FIRST : OTHER_FIRST;
		} else if (otherId == null) {
			idCompareResult = descending ? OTHER_FIRST : THIS_FIRST;
		} else {
			idCompareResult = direction * Long.compare(thisId, otherId);
		}
		return idCompareResult;
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
}

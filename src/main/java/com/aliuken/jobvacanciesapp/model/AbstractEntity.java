package com.aliuken.jobvacanciesapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;
import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@MappedSuperclass
@Data
@SuppressWarnings("serial")
@Slf4j
public abstract class AbstractEntity implements Serializable, Comparable<AbstractEntity> {
	@Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name="first_registration_date_time", nullable=false)
	private LocalDateTime firstRegistrationDateTime;

	@NotNull
	@ManyToOne
	@JoinColumn(name="first_registration_auth_user_id", nullable=false)
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
		firstRegistrationAuthUser = getSessionAuthUser();
	}

	@PreUpdate
	private void preUpdate() {
		lastModificationDateTime = LocalDateTime.now();
		lastModificationAuthUser = getSessionAuthUser();
	}

	public Long getFirstRegistrationAuthUserId() {
		Long firstRegistrationAuthUserId = (firstRegistrationAuthUser != null) ? firstRegistrationAuthUser.getId() : null;
		return firstRegistrationAuthUserId;
	}

	public String getFirstRegistrationAuthUserEmail() {
		String firstRegistrationAuthUserEmail = (firstRegistrationAuthUser != null) ? firstRegistrationAuthUser.getEmail() : null;
		return firstRegistrationAuthUserEmail;
	}

	public String getFirstRegistrationAuthUserEmail(String defaultValue) {
		String firstRegistrationAuthUserEmail = (firstRegistrationAuthUser != null) ? firstRegistrationAuthUser.getEmail() : defaultValue;
		return firstRegistrationAuthUserEmail;
	}

	public Long getLastModificationAuthUserId() {
		Long lastModificationAuthUserId = (lastModificationAuthUser != null) ? lastModificationAuthUser.getId() : null;
		return lastModificationAuthUserId;
	}

	public String getLastModificationAuthUserEmail() {
		String lastModificationAuthUserEmail = (lastModificationAuthUser != null) ? lastModificationAuthUser.getEmail() : null;
		return lastModificationAuthUserEmail;
	}

	public String getLastModificationAuthUserEmail(String defaultValue) {
		String lastModificationAuthUserEmail = (lastModificationAuthUser != null) ? lastModificationAuthUser.getEmail() : defaultValue;
		return lastModificationAuthUserEmail;
	}

	public static AuthUser getSessionAuthUser() {
		AuthUser sessionAuthUser;
		try {
			final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		    final HttpSession httpSession = servletRequestAttributes.getRequest().getSession(true);

			sessionAuthUser = (AuthUser) httpSession.getAttribute(Constants.SESSION_AUTH_USER);
		} catch(Exception e) {
			if(log.isErrorEnabled()) {
				log.error("An exception happened when trying to get the session user", e);
			}
			sessionAuthUser = null;
		}
		if(sessionAuthUser == null) {
			sessionAuthUser = AbstractEntity.getAnonymousAuthUser();
		}

		return sessionAuthUser;
	}

	private static AuthUser getAnonymousAuthUser() {
		final JpaRepository<AuthUser, Long> authUserRepository = JpaRepositoryWithPaginationAndSorting.getJpaRepository(AuthUser.class);

		Optional<AuthUser> optionalAuthUser = authUserRepository.findById(Constants.ANONYMOUS_AUTH_USER_ID);
		if (!optionalAuthUser.isPresent()) {
			return null;
		}

		AuthUser authUser = optionalAuthUser.get();
		return authUser;
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
		final String idString = String.valueOf(this.getId());
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(this.getFirstRegistrationDateTime());
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = DateTimeUtils.convertToString(this.getLastModificationDateTime());
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AbstractEntity [id=", idString,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstRegistrationDateTime, this.getFirstRegistrationAuthUserEmail(), lastModificationDateTime, this.getLastModificationAuthUserEmail());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		AbstractEntity other = (AbstractEntity) obj;

		return Objects.equals(id, other.id)
			&& Objects.equals(firstRegistrationDateTime, other.firstRegistrationDateTime)
			&& Objects.equals(this.getFirstRegistrationAuthUserEmail(), other.getFirstRegistrationAuthUserEmail())
			&& Objects.equals(lastModificationDateTime, other.lastModificationDateTime)
			&& Objects.equals(this.getLastModificationAuthUserEmail(), other.getLastModificationAuthUserEmail());
	}

}

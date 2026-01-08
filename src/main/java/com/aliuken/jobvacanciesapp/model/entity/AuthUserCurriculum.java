package com.aliuken.jobvacanciesapp.model.entity;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntityWithAuthUser;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="auth_user_curriculum", indexes={
		@Index(name="auth_user_curriculum_unique_key_1", columnList="auth_user_id, file_name", unique=true),
		@Index(name="auth_user_curriculum_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="auth_user_curriculum_key_2", columnList="last_modification_auth_user_id"),
		@Index(name="auth_user_curriculum_key_3", columnList="auth_user_id")})
@Getter
@Setter
public class AuthUserCurriculum extends AbstractEntityWithAuthUser<AuthUserCurriculum> {
	private static final long serialVersionUID = -8095503029320669346L;

	@ManyToOne
	@JoinColumn(name="auth_user_id", nullable=false)
	private @NonNull AuthUser authUser;

	@Size(max=255)
	@Column(name="file_name", length=255, nullable=false)
	private @NonNull String fileName;

	@Size(max=500)
	@Column(name="description", length=500, nullable=false)
	private @NonNull String description;

	public AuthUserCurriculum() {
		super();
	}

	public @NonNull String getFilePath() {
		final AuthUser authUser = this.getAuthUser();

		final String authUserIdString;
		if(authUser != null) {
			Long authUserId = authUser.getId();
			authUserIdString = (authUserId != null) ? authUserId.toString() : "temp";
		} else {
			authUserIdString = "temp";
		}

		final String filePath = StringUtils.getStringJoined(authUserIdString, "/", fileName);
		return filePath;
	}

	public String getSelectionName() {
		final String idString = this.getIdString();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String selectionName = StringUtils.getStringJoined("CV ", idString, Constants.SPACE, firstRegistrationDateTimeString);
		return selectionName;
	}

	@Override
	public boolean isPrintableEntity() {
		return true;
	}

	@Override
	public @NonNull String getKeyFields() {
		final String idString = this.getIdString();
		final String authUserIdString = this.getAuthUserId();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("id: "), idString, Constants.NEWLINE,
			StyleApplier.getBoldString("[authUserId, fileName]: "), "[", authUserIdString, ", ", fileName, "]");
		return result;
	}

	@Override
	public @NonNull String getOtherFields() {
		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("description: "), description);
		return result;
	}

	@Override
	public @NonNull String toString() {
		final String idString = this.getIdString();
		final String authUserEmail = this.getAuthUserEmail();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AuthUserCurriculum [id=", idString, ", authUser=", authUserEmail, ", fileName=", fileName,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}
}

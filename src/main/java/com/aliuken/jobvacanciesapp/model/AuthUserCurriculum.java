package com.aliuken.jobvacanciesapp.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Entity
@Table(name="auth_user_curriculum", indexes={
		@Index(name="auth_user_curriculum_unique_key_1", columnList="auth_user_id, file_name", unique=true),
		@Index(name="auth_user_curriculum_key_1", columnList="first_registration_auth_user_id"),
		@Index(name="auth_user_curriculum_key_2", columnList="last_modification_auth_user_id"),
		@Index(name="auth_user_curriculum_key_3", columnList="auth_user_id")})
@Data
public class AuthUserCurriculum extends AbstractEntityWithAuthUser {

	private static final long serialVersionUID = -8095503029320669346L;

	@NotNull
    @Size(max=255)
    @Column(name="file_name", length=255, nullable=false)
	private String fileName;

	@NotNull
	@Size(max=255)
    @Column(name="description", length=255, nullable=false)
	private String description;

	public AuthUserCurriculum() {
		super();
	}

	public String getFilePath() {
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
		final String idString = String.valueOf(this.getId());
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(this.getFirstRegistrationDateTime());
		final String selectionName = StringUtils.getStringJoined("CV ", idString, " ", firstRegistrationDateTimeString);
		return selectionName;
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(this.getId());
		final String authUserEmail = (this.getAuthUser() != null) ? this.getAuthUser().getEmail() : null;
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(this.getFirstRegistrationDateTime());
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = DateTimeUtils.convertToString(this.getLastModificationDateTime());
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AuthUserCurriculum [id=", idString, ", authUser=", authUserEmail, ", fileName=", fileName,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail, ", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(this.getAuthUser(), fileName);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		AuthUserCurriculum other = (AuthUserCurriculum) obj;
		return Objects.equals(this.getAuthUser(), other.getAuthUser()) && Objects.equals(fileName, other.fileName);
	}

}

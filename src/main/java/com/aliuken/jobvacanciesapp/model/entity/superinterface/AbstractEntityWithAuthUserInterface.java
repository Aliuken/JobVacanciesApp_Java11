package com.aliuken.jobvacanciesapp.model.entity.superinterface;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;

public interface AbstractEntityWithAuthUserInterface extends Serializable, AbstractEntityFieldsPrintable {
	public abstract AuthUser getAuthUser();
	public abstract void setAuthUser(AuthUser authUser);

	public default String getAuthUserId() {
		final AuthUser authUser = this.getAuthUser();
		final String authUserId = (authUser != null) ? authUser.getIdString() : null;
		return authUserId;
	}

	public default String getAuthUserEmail() {
		final AuthUser authUser = this.getAuthUser();
		final String authUserEmail = (authUser != null) ? authUser.getEmail() : null;
		return authUserEmail;
	}

	public default String getAuthUserName() {
		final AuthUser authUser = this.getAuthUser();
		final String authUserName = (authUser != null) ? authUser.getName() : null;
		return authUserName;
	}

	public default String getAuthUserSurnames() {
		final AuthUser authUser = this.getAuthUser();
		final String authUserSurnames = (authUser != null) ? authUser.getSurnames() : null;
		return authUserSurnames;
	}

	public default String getAuthUserFullName() {
		final AuthUser authUser = this.getAuthUser();
		final String fullName = (authUser != null) ? authUser.getFullName() : null;
		return fullName;
	}

	@Override
	public default @NonNull String getAuthUserFields() {
		final String email = this.getAuthUserEmail();
		final String name = this.getAuthUserName();
		final String surnames = this.getAuthUserSurnames();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("email: "), email, Constants.NEWLINE,
			StyleApplier.getBoldString("name: "), name, Constants.NEWLINE,
			StyleApplier.getBoldString("surnames: "), surnames);
		return result;
	}
}

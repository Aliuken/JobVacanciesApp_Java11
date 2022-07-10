package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Data
public class AuthUserForSignupDTO implements Serializable {

	private static final long serialVersionUID = -7781889193918523592L;

	private String email;
	private String password1;
	private String password2;
    private String name;
    private String surnames;
    private String language;

	public AuthUserForSignupDTO() {
		super();
	}

	public AuthUserForSignupDTO(AuthUser authUser) {
		if(authUser != null) {
			this.email = authUser.getEmail();
			this.password1 = null;
			this.password2 = null;
			this.name = authUser.getName();
			this.surnames = authUser.getSurnames();
			this.language = authUser.getLanguage().getCode();
		}
	}

	@Override
	public String toString() {
		final String result = StringUtils.getStringJoined("AuthUserForSignupDTO [email=", email, ", name=", name, ", surnames=", surnames, ", language=", language, "]");

		return result;
	}

}

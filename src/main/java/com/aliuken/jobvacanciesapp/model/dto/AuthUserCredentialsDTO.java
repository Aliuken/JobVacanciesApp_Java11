package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;

import com.aliuken.jobvacanciesapp.model.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Data
public class AuthUserCredentialsDTO implements Serializable {

	private static final long serialVersionUID = -5672637016876579368L;

	private long id;
	private String email;
    private String password;
    private String newPassword1;
    private String newPassword2;

	public AuthUserCredentialsDTO() {
		super();
	}

	public AuthUserCredentialsDTO(AuthUserCredentials authUserCredentials) {
		if(authUserCredentials != null) {
			this.id = authUserCredentials.getId();
			this.email = authUserCredentials.getEmail();
			this.password = null;
			this.newPassword1 = null;
			this.newPassword2 = null;
		}
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(id);

		final String result = StringUtils.getStringJoined("AuthUserCredentialsDTO [id=", idString, ", email=", email, "]");

		return result;
	}

}

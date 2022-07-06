package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Data
public class AuthUserDTO implements Serializable {

	private static final long serialVersionUID = -7781889193918523592L;

	private long id;
	private String email;
	private String password;
    private String name;
    private String surnames;
    private String language;
    private boolean enabled;
    private String maxPriorityAuthRoleName;
    private LocalDateTime firstRegistrationDateTime;
    private String firstRegistrationAuthUserEmail;
    private Set<String> authRoleNames;

	public AuthUserDTO() {
		super();
	}

	public AuthUserDTO(AuthUser authUser) {
		if(authUser != null) {
			this.id = authUser.getId();
			this.email = authUser.getEmail();
			this.password = null;
			this.name = authUser.getName();
			this.surnames = authUser.getSurnames();
			this.language = authUser.getLanguage().getCode();
			this.enabled = true;
			this.maxPriorityAuthRoleName = authUser.getMaxPriorityAuthRoleName();
			this.firstRegistrationDateTime = authUser.getFirstRegistrationDateTime();
			this.firstRegistrationAuthUserEmail = authUser.getFirstRegistrationAuthUserEmail();
			this.authRoleNames = authUser.getAuthRoleNames();
		}
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(id);
		final String enabledString = enabled ? "true" : "false";
		final String firstRegistrationDateTimeString = DateTimeUtils.convertToString(firstRegistrationDateTime);
		final String authRoleNamesString = authRoleNames.toString();

		final String result = StringUtils.getStringJoined("AuthUserDTO [id=", idString, ", email=", email, ", name=", name, ", surnames=", surnames, ", language=", language, ", enabled=", enabledString, ", maxPriorityAuthRoleName=", maxPriorityAuthRoleName, 
				", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUserEmail=", firstRegistrationAuthUserEmail, ", authRoleNames=", authRoleNamesString, "]");

		return result;
	}

}

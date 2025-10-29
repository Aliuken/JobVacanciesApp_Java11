package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Data
public class AuthUserCredentialsDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = -5672637016876579368L;

	private static final AuthUserCredentialsDTO NO_ARGS_INSTANCE = new AuthUserCredentialsDTO(null, null, null, null, null);

	@NotNull(message="{id.notNull}")
	private final Long id;

	@NotEmpty(message="{email.notEmpty}")
	@Size(max=255, message="{email.maxSize}")
	@Email(message="{email.validFormat}")
	private final String email;

	@NotEmpty(message="{password.notEmpty}")
	@Size(min=7, max=20, message="{password.minAndMaxSize}")
	private final String password;

	@NotEmpty(message="{newPassword1.notEmpty}")
	@Size(min=7, max=20, message="{newPassword1.minAndMaxSize}")
	private final String newPassword1;

	@NotEmpty(message="{newPassword2.notEmpty}")
	@Size(min=7, max=20, message="{newPassword2.minAndMaxSize}")
	private final String newPassword2;

	public static AuthUserCredentialsDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public String toString() {
		final String idString = Objects.toString(id);

		final String result = StringUtils.getStringJoined("AuthUserCredentialsDTO [id=", idString, ", email=", email, "]");
		return result;
	}
}

package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class AuthUserForSignupDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = -7781889193918523592L;

	@NotEmpty(message="{email.notEmpty}")
	@Size(max=255, message="{email.maxSize}")
	@Email(message="{email.validFormat}")
	private final @NonNull String email;

	@NotEmpty(message="{password1.notEmpty}")
	@Size(min=7, max=20, message="{password1.minAndMaxSize}")
	private final String password1;

	@NotEmpty(message="{password2.notEmpty}")
	@Size(min=7, max=20, message="{password2.minAndMaxSize}")
	private final String password2;

	@NotEmpty(message="{name.notEmpty}")
	@Size(max=25, message="{name.maxSize25}")
	private final @NonNull String name;

	@NotEmpty(message="{surnames.notEmpty}")
	@Size(max=35, message="{surnames.maxSize}")
	private final @NonNull String surnames;

	@NotEmpty(message="{language.notEmpty}")
	@Size(min=2, max=2, message="{language.minAndMaxSize}")
	private final @NonNull String languageCode;

	public static @NonNull AuthUserForSignupDTO getNewInstance(final @NonNull String languageCode) {
		final AuthUserForSignupDTO authUserForSignupDTO = new AuthUserForSignupDTO(Constants.EMPTY_STRING, null, null, Constants.EMPTY_STRING, Constants.EMPTY_STRING, languageCode);
		return authUserForSignupDTO;
	}

	@Override
	public @NonNull String toString() {
		final String result = StringUtils.getStringJoined("AuthUserForSignupDTO [email=", email, ", name=", name, ", surnames=", surnames, ", languageCode=", languageCode, "]");
		return result;
	}
}

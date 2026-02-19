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
public class AuthUserEmailDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = 4350778405637490133L;

	private static final @NonNull AuthUserEmailDTO NO_ARGS_INSTANCE = new AuthUserEmailDTO(null, Constants.EMPTY_STRING);

	private final Long id;

	@NotEmpty(message="{email.notEmpty}")
	@Size(max=255, message="{email.maxSize}")
	@Email(message="{email.validFormat}")
	private final @NonNull String email;

	public static @NonNull AuthUserEmailDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public @NonNull String toString() {
		final String idString = String.valueOf(id);

		final String result = StringUtils.getStringJoined("AuthUserEmailDTO [id=", idString, ", email=", email, "]");
		return result;
	}
}

package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class AuthUserResetPasswordDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = -3316162707082805827L;

	private final Long id;

	@NotEmpty(message="{email.notEmpty}")
	@Size(max=255, message="{email.maxSize}")
	@Email(message="{email.validFormat}")
	private final String email;

	@NotEmpty(message="{uuid.notEmpty}")
	@Size(min=36, max=36, message="{uuid.minAndMaxSize}")
	private final String uuid;

	@NotEmpty(message="{newPassword1.notEmpty}")
	@Size(min=7, max=20, message="{newPassword1.minAndMaxSize}")
	private final String newPassword1;

	@NotEmpty(message="{newPassword2.notEmpty}")
	@Size(min=7, max=20, message="{newPassword2.minAndMaxSize}")
	private final String newPassword2;

	@Override
	public @NonNull String toString() {
		final String idString = String.valueOf(id);

		final String result = StringUtils.getStringJoined("AuthUserResetPasswordDTO [id=", idString, ", email=", email, "]");
		return result;
	}
}

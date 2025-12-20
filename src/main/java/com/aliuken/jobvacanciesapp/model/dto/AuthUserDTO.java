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
import java.util.Set;

@Data
public class AuthUserDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = -7781889193918523592L;

	private static final AuthUserDTO NO_ARGS_INSTANCE = new AuthUserDTO(null, null, null, null, null, null, null, null, null, null, null, null, null);

	@NotNull(message="{id.notNull}")
	private final Long id;

	@NotEmpty(message="{email.notEmpty}")
	@Size(max=255, message="{email.maxSize}")
	@Email(message="{email.validFormat}")
	private final String email;

	@NotEmpty(message="{name.notEmpty}")
	@Size(max=25, message="{name.maxSize25}")
	private final String name;

	@NotEmpty(message="{surnames.notEmpty}")
	@Size(max=35, message="{surnames.maxSize}")
	private final String surnames;

	@NotEmpty(message="{language.notEmpty}")
	@Size(min=2, max=2, message="{language.minAndMaxSize}")
	private final String languageCode;

	@NotNull(message="{enabled.notNull}")
	private final Boolean enabled;

	@NotEmpty(message="{colorModeCode.notEmpty}")
	private final String colorModeCode;

	@NotNull(message="{initialCurrencySymbol.notEmpty}")
	private final String initialCurrencySymbol;

	@NotNull(message="{initialTableSortingDirectionCode.notNull}")
	private final String initialTableSortingDirectionCode;

	@NotNull(message="{initialTablePageSizeValue.notNull}")
	private final Integer initialTablePageSizeValue;

	@NotEmpty(message="{pdfDocumentPageFormatCode.notEmpty}")
	private final String pdfDocumentPageFormatCode;

	@NotEmpty(message="{maxPriorityAuthRoleName.notEmpty}")
	@Size(max=20, message="{maxPriorityAuthRoleName.maxSize}")
	private final String maxPriorityAuthRoleName;

	@NotEmpty(message="{authRoleNames.notEmpty}")
	private final Set<String> authRoleNames;

	public static AuthUserDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public String toString() {
		final String idString = Objects.toString(id);
		final String enabledString = Objects.toString(enabled);
		final String initialTablePageSizeValueString = Objects.toString(initialTablePageSizeValue);
		final String authRoleNamesString = authRoleNames.toString();

		final String result = StringUtils.getStringJoined("AuthUserDTO [id=", idString, ", email=", email, ", name=", name, ", surnames=", surnames, ", languageCode=", languageCode, ", enabled=", enabledString,
				", initialTableSortingDirectionCode=", initialTableSortingDirectionCode, ", initialTablePageSizeValue=", initialTablePageSizeValueString, ", colorModeCode=", colorModeCode, ", pdfDocumentPageFormatCode=", pdfDocumentPageFormatCode, ", maxPriorityAuthRoleName=", maxPriorityAuthRoleName, ", authRoleNames=", authRoleNamesString, "]");
		return result;
	}
}

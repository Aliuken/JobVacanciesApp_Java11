package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
public class AuthUserDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = -7781889193918523592L;

	private static final @NonNull AuthUserDTO NO_ARGS_INSTANCE = new AuthUserDTO(null, null, null, null, null, null, null, null, null, null, null, null, null);

	@NotNull(message="{id.notNull}")
	private final @NonNull Long id;

	@NotEmpty(message="{email.notEmpty}")
	@Size(max=255, message="{email.maxSize}")
	@Email(message="{email.validFormat}")
	private final @NonNull String email;

	@NotEmpty(message="{name.notEmpty}")
	@Size(max=25, message="{name.maxSize25}")
	private final @NonNull String name;

	@NotEmpty(message="{surnames.notEmpty}")
	@Size(max=35, message="{surnames.maxSize}")
	private final @NonNull String surnames;

	@NotEmpty(message="{language.notEmpty}")
	@Size(min=2, max=2, message="{language.minAndMaxSize}")
	private final @NonNull String languageCode;

	@NotNull(message="{enabled.notNull}")
	private final @NonNull Boolean enabled;

	@NotEmpty(message="{colorModeCode.notEmpty}")
	private final @NonNull String colorModeCode;

	@NotNull(message="{initialCurrencySymbol.notEmpty}")
	private final @NonNull String initialCurrencySymbol;

	@NotNull(message="{initialTableSortingDirectionCode.notNull}")
	private final @NonNull String initialTableSortingDirectionCode;

	@NotNull(message="{initialTablePageSizeValue.notNull}")
	private final @NonNull Integer initialTablePageSizeValue;

	@NotEmpty(message="{pdfDocumentPageFormatCode.notEmpty}")
	private final @NonNull String pdfDocumentPageFormatCode;

	@NotEmpty(message="{maxPriorityAuthRoleName.notEmpty}")
	@Size(max=20, message="{maxPriorityAuthRoleName.maxSize}")
	private final @NonNull String maxPriorityAuthRoleName;

	@NotEmpty(message="{authRoleNames.notEmpty}")
	private final @NonNull Set<String> authRoleNames;

	public static @NonNull AuthUserDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public @NonNull String toString() {
		final String idString = String.valueOf(id);
		final String enabledString = String.valueOf(enabled);
		final String initialTablePageSizeValueString = String.valueOf(initialTablePageSizeValue);
		final String authRoleNamesString = authRoleNames.toString();

		final String result = StringUtils.getStringJoined("AuthUserDTO [id=", idString, ", email=", email, ", name=", name, ", surnames=", surnames, ", languageCode=", languageCode, ", enabled=", enabledString,
				", initialTableSortingDirectionCode=", initialTableSortingDirectionCode, ", initialTablePageSizeValue=", initialTablePageSizeValueString, ", colorModeCode=", colorModeCode, ", pdfDocumentPageFormatCode=", pdfDocumentPageFormatCode, ", maxPriorityAuthRoleName=", maxPriorityAuthRoleName, ", authRoleNames=", authRoleNamesString, "]");
		return result;
	}
}

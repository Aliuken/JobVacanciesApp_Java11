package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.file.CustomMultipartFile;
import lombok.Data;
import org.jspecify.annotations.NonNull;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class AuthUserCurriculumDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = 8027584650213205654L;

	private final Long id;

	@NotNull(message="{authUser.notNull}")
	private final @NonNull AuthUserDTO authUser;

	@NotNull(message="{curriculumFile.notNull}")
	private final @NonNull MultipartFile curriculumFile;

	private final String fileName;

	@NotEmpty(message="{description.notEmpty}")
	@Size(max=500, message="{description.maxSize}")
	private final @NonNull String description;

	public AuthUserCurriculumDTO(final Long id, final @NonNull AuthUserDTO authUser, final @NonNull MultipartFile curriculumFile, final String fileName, final @NonNull String description) {
		this.id = id;
		this.authUser = authUser;
		this.curriculumFile = curriculumFile;
		this.fileName = fileName;
		this.description = description;
	}

	public static @NonNull AuthUserCurriculumDTO getNewInstance(final Long id, final @NonNull AuthUserDTO authUserDTO, final String fileName, final @NonNull String description) {
		final MultipartFile curriculumFile = new CustomMultipartFile("curriculumFile", null, null, null);
		final AuthUserCurriculumDTO authUserCurriculumDTO = new AuthUserCurriculumDTO(id, authUserDTO, curriculumFile, fileName, description);
		return authUserCurriculumDTO;
	}

	@Override
	public @NonNull String toString() {
		final String idString = String.valueOf(id);
		final String authUserEmail = (authUser != null) ? authUser.getEmail() : null;

		final String result = StringUtils.getStringJoined("AuthUserCurriculumDTO [id=", idString, ", authUserEmail=", authUserEmail, ", fileName=", fileName, ", description=", description, "]");
		return result;
	}
}

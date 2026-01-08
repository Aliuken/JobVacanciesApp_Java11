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
	private final AuthUserDTO authUser;

	@NotNull(message="{curriculumFile.notNull}")
	private final @NonNull MultipartFile curriculumFile;

	private final String fileName;

	@NotEmpty(message="{description.notEmpty}")
	@Size(max=500, message="{description.maxSize}")
	private final @NonNull String description;

	public AuthUserCurriculumDTO(final Long id, final AuthUserDTO authUser, final MultipartFile curriculumFile, final String fileName, final @NonNull String description) {
		this.id = id;

		if(authUser != null) {
			this.authUser = authUser;
		} else {
			this.authUser = AuthUserDTO.getNewInstance();
		}

		if(curriculumFile != null) {
			this.curriculumFile = curriculumFile;
		} else {
			this.curriculumFile = new CustomMultipartFile("curriculumFile", null, null, null);
		}

		this.fileName = fileName;
		this.description = description;
	}

	public static @NonNull AuthUserCurriculumDTO getNewInstance(final String description) {
		final AuthUserCurriculumDTO authUserCurriculumDTO = new AuthUserCurriculumDTO(null, null, null, null, description);
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

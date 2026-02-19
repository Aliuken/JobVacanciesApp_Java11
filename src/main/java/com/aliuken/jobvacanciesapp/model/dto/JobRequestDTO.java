package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class JobRequestDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = 6400290314757351128L;

	private final Long id;

	@NotNull(message="{authUser.notNull}")
	private final @NonNull AuthUserDTO authUser;

	@NotNull(message="{jobVacancy.notNull}")
	private final @NonNull JobVacancyDTO jobVacancy;

	//In jobRequestForm.html we cannot use *{jobVacancy.id} because JobVacancyDTO is a Java record, so we use *{jobVacancyId} instead
	private final Long jobVacancyId;

	@NotEmpty(message="{comments.notEmpty}")
	@Size(max=1000, message="{comments.maxSize}")
	private final @NonNull String comments;

	@NotEmpty(message="{curriculumFileName.notEmpty}")
	@Size(max=255, message="{curriculumFileName.maxSize}")
	private final @NonNull String curriculumFileName;

	public JobRequestDTO(final Long id, final @NonNull AuthUserDTO authUser, final @NonNull JobVacancyDTO jobVacancy, final @NonNull String comments, final @NonNull String curriculumFileName) {
		super();
		this.id = id;
		this.authUser = authUser;
		this.jobVacancy = jobVacancy;
		this.jobVacancyId = jobVacancy.getId();
		this.comments = comments;
		this.curriculumFileName = curriculumFileName;
	}

	public static @NonNull JobRequestDTO getNewInstance(final @NonNull AuthUserDTO authUserDTO, final @NonNull JobVacancyDTO jobVacancyDTO) {
		final JobRequestDTO jobRequestDTO = new JobRequestDTO(
			null,
			authUserDTO,
			jobVacancyDTO,
			Constants.EMPTY_STRING,
			Constants.EMPTY_STRING);
		return jobRequestDTO;
	}

	public static @NonNull JobRequestDTO getNewInstance(@NonNull JobRequestDTO jobRequestDTO, final @NonNull JobVacancyDTO jobVacancyDTO) {
		jobRequestDTO = new JobRequestDTO(
			jobRequestDTO.getId(),
			jobRequestDTO.getAuthUser(),
			jobVacancyDTO,
			jobRequestDTO.getComments(),
			jobRequestDTO.getCurriculumFileName()
		);
		return jobRequestDTO;
	}

	@Override
	public @NonNull String toString() {
		final String idString = String.valueOf(id);
		final String authUserEmail = authUser.getEmail();
		final String jobVacancyIdString = String.valueOf(jobVacancy.getId());

		final String result = StringUtils.getStringJoined("JobRequestDTO [id=", idString, ", authUserEmail=", authUserEmail, ", jobVacancyId=", jobVacancyIdString, ", comments=", comments, ", curriculumFileName=", curriculumFileName, "]");
		return result;
	}
}

package com.aliuken.jobvacanciesapp.model.dto;

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

	private static final @NonNull JobRequestDTO NO_ARGS_INSTANCE = new JobRequestDTO(null, null, null, null, null, null);

	private final Long id;
	private final AuthUserDTO authUser;

	@NotNull(message="{jobVacancy.notNull}")
	private final JobVacancyDTO jobVacancy;

	//In jobRequestForm.html we cannot use *{jobVacancy.id} because JobVacancyDTO is a Java record, so we use *{jobVacancyId} instead
	private final Long jobVacancyId;

	@NotEmpty(message="{comments.notEmpty}")
	@Size(max=1000, message="{comments.maxSize}")
	private final String comments;

	@NotEmpty(message="{curriculumFileName.notEmpty}")
	@Size(max=255, message="{curriculumFileName.maxSize}")
	private final String curriculumFileName;

	public JobRequestDTO(final Long id, final AuthUserDTO authUser, final JobVacancyDTO jobVacancy, final Long jobVacancyId, final String comments, final String curriculumFileName) {
		super();
		this.id = id;

		if(authUser != null) {
			this.authUser = authUser;
		} else {
			this.authUser = AuthUserDTO.getNewInstance();
		}

		if(jobVacancy == null) {
			this.jobVacancy = JobVacancyDTO.getNewInstance(jobVacancyId);
			this.jobVacancyId = jobVacancyId;
		} else if(jobVacancyId == null) {
			this.jobVacancy = jobVacancy;
			this.jobVacancyId = jobVacancy.getId();
		} else {
			this.jobVacancy = jobVacancy;
			this.jobVacancyId = jobVacancyId;
		}

		this.comments = comments;
		this.curriculumFileName = curriculumFileName;
	}

	public static @NonNull JobRequestDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	public static @NonNull JobRequestDTO getNewInstance(@NonNull JobRequestDTO jobRequestDTO, final @NonNull JobVacancyDTO jobVacancyDTO) {
		jobRequestDTO = new JobRequestDTO(
			jobRequestDTO.getId(),
			jobRequestDTO.getAuthUser(),
			jobVacancyDTO,
			jobVacancyDTO.getId(),
			jobRequestDTO.getComments(),
			jobRequestDTO.getCurriculumFileName()
		);
		return jobRequestDTO;
	}

	@Override
	public @NonNull String toString() {
		final String idString = String.valueOf(id);
		final String authUserEmail = (authUser != null) ? authUser.getEmail() : null;
		final String jobVacancyIdString = (jobVacancy != null) ? String.valueOf(jobVacancy.getId()) : null;

		final String result = StringUtils.getStringJoined("JobRequestDTO [id=", idString, ", authUserEmail=", authUserEmail, ", jobVacancyId=", jobVacancyIdString, ", comments=", comments, ", curriculumFileName=", curriculumFileName, "]");
		return result;
	}
}

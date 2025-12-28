package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class JobRequestDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = 6400290314757351128L;

	private static final JobRequestDTO NO_ARGS_INSTANCE = new JobRequestDTO(null, null, null, null, null, null);

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

	public static JobRequestDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	public static JobRequestDTO getNewInstance(JobRequestDTO jobRequestDTO, final JobVacancyDTO jobVacancyDTO) {
		if(jobRequestDTO != null) {
			jobRequestDTO = new JobRequestDTO(
				jobRequestDTO.getId(),
				jobRequestDTO.getAuthUser(),
				jobVacancyDTO,
				(jobVacancyDTO != null) ? jobVacancyDTO.getId() : null,
				jobRequestDTO.getComments(),
				jobRequestDTO.getCurriculumFileName()
			);
		} else {
			jobRequestDTO = new JobRequestDTO(
				null,
				null,
				jobVacancyDTO,
				(jobVacancyDTO != null) ? jobVacancyDTO.getId() : null,
				null,
				null
			);
		}
		return jobRequestDTO;
	}

	@Override
	public String toString() {
		final String idString = String.valueOf(id);
		final String authUserEmail = (authUser != null) ? String.valueOf(authUser.getEmail()) : null;
		final String jobVacancyIdString = (jobVacancy != null) ? String.valueOf(jobVacancy.getId()) : null;

		final String result = StringUtils.getStringJoined("JobRequestDTO [id=", idString, ", authUserEmail=", authUserEmail, ", jobVacancyId=", jobVacancyIdString, ", comments=", comments, ", curriculumFileName=", curriculumFileName, "]");
		return result;
	}
}

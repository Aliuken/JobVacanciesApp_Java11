package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
public class JobCategoryDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = 291514185211279253L;

	private static final @NonNull JobCategoryDTO NO_ARGS_INSTANCE = new JobCategoryDTO(null, null, null, null);

	private final Long id;

	@NotEmpty(message="{name.notEmpty}")
	@Size(max=35, message="{name.maxSize35}")
	private final String name;

	@NotEmpty(message="{description.notEmpty}")
	@Size(max=500, message="{description.maxSize}")
	private final String description;

	@NotNull(message="{jobVacancyIds.notNull}")
	private final Set<Long> jobVacancyIds;

	public static @NonNull JobCategoryDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	public static @NonNull JobCategoryDTO getNewInstance(final Long jobCategoryId) {
		final JobCategoryDTO jobCategoryDTO = new JobCategoryDTO(jobCategoryId, null, null, null);
		return jobCategoryDTO;
	}

	@Override
	public @NonNull String toString() {
		final String idString = String.valueOf(id);
		final String jobVacancyIdsString = jobVacancyIds.toString();

		final String result = StringUtils.getStringJoined("JobCategoryDTO [id=", idString, ", name=", name, ", description=", description, ", jobVacancyIds=", jobVacancyIdsString, "]");
		return result;
	}
}

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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class JobCompanyDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = 5328929981541165382L;

	private static final @NonNull JobCompanyDTO NO_ARGS_INSTANCE = new JobCompanyDTO(null, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Boolean.FALSE, null, null, null);

	private final Long id;

	@NotEmpty(message="{name.notEmpty}")
	@Size(max=35, message="{name.maxSize35}")
	private final @NonNull String name;

	@NotEmpty(message="{description.notEmpty}")
	@Size(max=500, message="{description.maxSize}")
	private final @NonNull String description;

	@NotNull
	private final Boolean isSelectedLogo;

	private final Long selectedLogoId;

	@NotEmpty(message="{selectedLogoFilePath.notEmpty}")
	private final @NonNull String selectedLogoFilePath;

	private final Set<JobCompanyLogoDTO> jobCompanyLogos;

	public JobCompanyDTO(final Long id, final @NonNull String name, final @NonNull String description, final Boolean isSelectedLogo, final Long selectedLogoId, final String selectedLogoFilePath, final Set<JobCompanyLogoDTO> jobCompanyLogos) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.isSelectedLogo = isSelectedLogo;
		this.selectedLogoId = selectedLogoId;

		if(selectedLogoId == null || Constants.NO_SELECTED_LOGO_ID.equals(selectedLogoId) || selectedLogoFilePath == null) {
			this.selectedLogoFilePath = Constants.NO_SELECTED_LOGO_FILE_PATH;
		} else {
			this.selectedLogoFilePath = selectedLogoFilePath;
		}

		this.jobCompanyLogos = jobCompanyLogos;
	}

	public static @NonNull JobCompanyDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	public static @NonNull JobCompanyDTO getNewInstance(final Long jobCompanyId) {
		final JobCompanyDTO jobCompanyDTO = new JobCompanyDTO(jobCompanyId, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Boolean.FALSE, null, null, null);
		return jobCompanyDTO;
	}

	public static @NonNull JobCompanyDTO getNewInstance(JobCompanyDTO jobCompanyDTO, final Boolean isSelectedLogo, final Long selectedLogoId, final String selectedLogoFilePath) {
		if(jobCompanyDTO != null) {
			jobCompanyDTO = new JobCompanyDTO(
				jobCompanyDTO.getId(),
				jobCompanyDTO.getName(),
				jobCompanyDTO.getDescription(),
				isSelectedLogo,
				selectedLogoId,
				(selectedLogoFilePath != null) ? selectedLogoFilePath : jobCompanyDTO.getSelectedLogoFilePath(),
				jobCompanyDTO.getJobCompanyLogos()
			);
		} else {
			jobCompanyDTO = new JobCompanyDTO(
				null,
				Constants.EMPTY_STRING,
				Constants.EMPTY_STRING,
				isSelectedLogo,
				selectedLogoId,
				selectedLogoFilePath,
				null
			);
		}
		return jobCompanyDTO;
	}

	public List<String> getJobCompanyLogoFilePaths() {
		final List<String> jobCompanyLogoFilePaths = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(jobCompanyLogos)
			.map(jcl -> jcl.getFilePath())
			.collect(Collectors.toList());

		return jobCompanyLogoFilePaths;
	}

	@Override
	public @NonNull String toString() {
		final String idString = String.valueOf(id);
		final String isSelectedLogoString = String.valueOf(isSelectedLogo);
		final String selectedLogoIdString = String.valueOf(selectedLogoId);
		final String jobCompanyLogoFilePaths = this.getJobCompanyLogoFilePaths().toString();

		final String result = StringUtils.getStringJoined("JobCompanyDTO [id=", idString, ", name=", name, ", description=", description, ", isSelectedLogo=", isSelectedLogoString, ", selectedLogoId=", selectedLogoIdString, ", selectedLogoFilePath=", selectedLogoFilePath, ", jobCompanyLogos=", jobCompanyLogoFilePaths, "]");
		return result;
	}
}

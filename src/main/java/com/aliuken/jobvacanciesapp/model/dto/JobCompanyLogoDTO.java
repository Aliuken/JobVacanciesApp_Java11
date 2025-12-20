package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Data
public class JobCompanyLogoDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = 4433301684411887321L;

	private static final JobCompanyLogoDTO NO_ARGS_INSTANCE = new JobCompanyLogoDTO(null, null, null, null);

	@NotNull(message="{id.notNull}")
	private final Long id;

	@NotEmpty(message="{fileName.notEmpty}")
	@Size(max=255, message="{fileName.maxSize}")
	private final String fileName;

	@NotEmpty(message="{filePath.notEmpty}")
	private final String filePath;

	@NotEmpty(message="{selectionName.notEmpty}")
	private final String selectionName;

	public static JobCompanyLogoDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public String toString() {
		final String idString = Objects.toString(id);

		final String result = StringUtils.getStringJoined("JobCompanyLogoDTO [id=", idString, ", fileName=", fileName, ", filePath=", filePath, ", selectionName=", selectionName, "]");
		return result;
	}
}

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
public class JobCompanyLogoDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = 4433301684411887321L;

	@NotNull(message="{id.notNull}")
	private final Long id;

	@NotEmpty(message="{fileName.notEmpty}")
	@Size(max=255, message="{fileName.maxSize}")
	private final String fileName;

	@NotEmpty(message="{filePath.notEmpty}")
	private final String filePath;

	@NotEmpty(message="{selectionName.notEmpty}")
	private final String selectionName;

	@Override
	public @NonNull String toString() {
		final String idString = String.valueOf(id);

		final String result = StringUtils.getStringJoined("JobCompanyLogoDTO [id=", idString, ", fileName=", fileName, ", filePath=", filePath, ", selectionName=", selectionName, "]");
		return result;
	}
}

package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class EmailAttachmentDTO implements Serializable {
	private static final long serialVersionUID = 8630535588098308818L;

	@NotEmpty(message="{attachmentFileName.notEmpty}")
	private final String attachmentFileName;

	@NotEmpty(message="{attachmentFilePath.notEmpty}")
	private final String attachmentFilePath;

	@Override
	public @NonNull String toString() {
		final String result = StringUtils.getStringJoined("EmailAttachmentDTO [attachmentFileName=", attachmentFileName, ", attachmentFilePath=", attachmentFilePath, "]");
		return result;
	}
}

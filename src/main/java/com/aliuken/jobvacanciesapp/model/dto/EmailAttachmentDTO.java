package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class EmailAttachmentDTO implements Serializable {
	private static final long serialVersionUID = 8630535588098308818L;

	private static final EmailAttachmentDTO NO_ARGS_INSTANCE = new EmailAttachmentDTO(null, null);

	@NotEmpty(message="{attachmentFileName.notEmpty}")
	private final String attachmentFileName;

	@NotEmpty(message="{attachmentFilePath.notEmpty}")
	private final String attachmentFilePath;

	public static EmailAttachmentDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public String toString() {
		final String result = StringUtils.getStringJoined("EmailAttachmentDTO [attachmentFileName=", attachmentFileName, ", attachmentFilePath=", attachmentFilePath, "]");
		return result;
	}
}

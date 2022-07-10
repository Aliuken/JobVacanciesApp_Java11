package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;

import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.Data;

@Data
public class EmailAttachmentDTO implements Serializable {
	private static final long serialVersionUID = 8630535588098308818L;

	private final String attachmentFileName;
	private final String attachmentFilePath;

	@Override
	public String toString() {
		final String result = StringUtils.getStringJoined("EmailAttachmentDTO [attachmentFileName=", attachmentFileName,
			", attachmentFilePath=", attachmentFilePath, "]");

		return result;
	}
}

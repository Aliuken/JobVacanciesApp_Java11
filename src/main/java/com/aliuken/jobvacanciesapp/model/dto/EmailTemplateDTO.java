package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class EmailTemplateDTO implements Serializable {
	private static final long serialVersionUID = 3727182939546135030L;

	private static final EmailTemplateDTO NO_ARGS_INSTANCE = new EmailTemplateDTO(null, null);

	@NotEmpty(message="{originEmailAddress.notEmpty}")
	private final String originEmailAddress;

	@NotEmpty(message="{textTemplate.notEmpty}")
	private final String textTemplate;

	public static EmailTemplateDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public String toString() {
		final String result = StringUtils.getStringJoined("EmailTemplateDTO [originEmailAddress=", originEmailAddress, ", textTemplate=", textTemplate, "]");
		return result;
	}
}

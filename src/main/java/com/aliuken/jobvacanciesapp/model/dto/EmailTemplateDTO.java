package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class EmailTemplateDTO implements Serializable {
	private static final long serialVersionUID = 3727182939546135030L;

	@NotEmpty(message="{originEmailAddress.notEmpty}")
	private final String originEmailAddress;

	@NotEmpty(message="{textTemplate.notEmpty}")
	private final String textTemplate;

	@Override
	public @NonNull String toString() {
		final String result = StringUtils.getStringJoined("EmailTemplateDTO [originEmailAddress=", originEmailAddress, ", textTemplate=", textTemplate, "]");
		return result;
	}
}

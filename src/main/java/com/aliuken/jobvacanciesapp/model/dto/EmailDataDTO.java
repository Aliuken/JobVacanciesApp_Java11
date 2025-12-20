package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class EmailDataDTO implements Serializable {
	private static final long serialVersionUID = 5897938866031322763L;

	private static final EmailDataDTO NO_ARGS_INSTANCE = new EmailDataDTO(null, null, null, null, null, null, null);

	@NotEmpty(message="{destinationEmailAddress.notEmpty}")
	private final String destinationEmailAddress;

	@NotEmpty(message="{subject.notEmpty}")
	private final String subject;

	@NotEmpty(message="{textTitle.notEmpty}")
	private final String textTitle;

	@NotEmpty(message="{textBody.notEmpty}")
	private final String textBody;

	private final Boolean isHtml;

	@NotNull(message="{language.notNull}")
	private final Language language;

	private final List<EmailAttachmentDTO> attachments;

	public static EmailDataDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public String toString() {
		final String languageName = language.name();
		final String attachmentsString = attachments.toString();
		final String isHtmlString = String.valueOf(isHtml);

		final String result = StringUtils.getStringJoined("EmailDataDTO [destinationEmailAddress=", destinationEmailAddress, ", subject=", subject,
			", textTitle=", textTitle, ", textBody=", textBody,  ", isHtml=", isHtmlString, ", language=", languageName,  ", attachments=", attachmentsString, "]");
		return result;
	}
}

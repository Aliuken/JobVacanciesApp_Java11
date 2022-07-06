package com.aliuken.jobvacanciesapp.service;

import java.util.List;

import javax.mail.MessagingException;

import com.aliuken.jobvacanciesapp.model.AuthUserLanguage;
import com.aliuken.jobvacanciesapp.model.dto.EmailAttachmentDTO;

public interface EmailService {

	void sendSimpleMessage(String to, String subject, String textTitle, String textBody, final AuthUserLanguage authUserLanguage);

	void sendComplexMessage(String to, String subject, String textTitle, String textBody, final AuthUserLanguage authUserLanguage, boolean isHtml, List<EmailAttachmentDTO> attachments) throws MessagingException;

}

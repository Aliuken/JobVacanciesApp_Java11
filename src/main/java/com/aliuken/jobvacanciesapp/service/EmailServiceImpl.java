package com.aliuken.jobvacanciesapp.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliuken.jobvacanciesapp.model.AuthUserLanguage;
import com.aliuken.jobvacanciesapp.model.dto.EmailAttachmentDTO;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    public SimpleMailMessage templateSimpleMessageEnglish;

    @Autowired
    public SimpleMailMessage templateSimpleMessageSpanish;

    @Override
    public void sendSimpleMessage(String to, String subject, String textTitle, String textBody, final AuthUserLanguage authUserLanguage) {
       	SimpleMailMessage templateSimpleMessage;
    	if(AuthUserLanguage.SPANISH.equals(authUserLanguage)) {
    		templateSimpleMessage = templateSimpleMessageSpanish;
    	} else {
    		templateSimpleMessage = templateSimpleMessageEnglish;
    	}

    	final String from = templateSimpleMessage.getFrom();
    	final String text = String.format(templateSimpleMessage.getText(), textTitle, textBody);

    	final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        emailSender.send(message);
    }

    @Override
    public void sendComplexMessage(String to, String subject, String textTitle, String textBody, final AuthUserLanguage authUserLanguage, boolean isHtml, List<EmailAttachmentDTO> attachments) throws MessagingException {
    	SimpleMailMessage templateSimpleMessage;
    	if(AuthUserLanguage.SPANISH.equals(authUserLanguage)) {
    		templateSimpleMessage = templateSimpleMessageSpanish;
    	} else {
    		templateSimpleMessage = templateSimpleMessageEnglish;
    	}

    	final MimeMessage mimeMessage = emailSender.createMimeMessage();
    	final String from = templateSimpleMessage.getFrom();
    	final String text = String.format(templateSimpleMessage.getText(), textTitle, textBody);

    	final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, isHtml);

        if(attachments != null) {
        	for(final EmailAttachmentDTO attachment : attachments) {
        		final String attachmentFileName = attachment.getAttachmentFileName();
        		final String attachmentFilePath = attachment.getAttachmentFilePath();
        		final Path attachmentPath = Paths.get(attachmentFilePath);
        		final FileSystemResource attachmentFile = new FileSystemResource(attachmentPath);
		        helper.addAttachment(attachmentFileName, attachmentFile);
        	}
        }

        emailSender.send(mimeMessage);
    }

}
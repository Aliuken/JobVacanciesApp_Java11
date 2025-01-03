package com.aliuken.jobvacanciesapp.service;

import javax.mail.MessagingException;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;

public interface EmailService {

	public abstract void sendSignUpConfirmationEmail(String destinationEmailAddress, String uuid, Language language) throws MessagingException;

	public abstract void sendResetPasswordEmail(String destinationEmailAddress, String uuid, Language language) throws MessagingException;

}

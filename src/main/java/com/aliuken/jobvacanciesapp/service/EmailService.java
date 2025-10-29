package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;

import javax.mail.MessagingException;

public interface EmailService {

	public abstract void sendSignUpConfirmationEmail(String destinationEmailAddress, String uuid, Language language) throws MessagingException;

	public abstract void sendResetPasswordEmail(String destinationEmailAddress, String uuid, Language language) throws MessagingException;

}

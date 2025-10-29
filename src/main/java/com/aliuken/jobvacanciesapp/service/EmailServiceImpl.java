package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.dto.EmailAttachmentDTO;
import com.aliuken.jobvacanciesapp.model.dto.EmailDataDTO;
import com.aliuken.jobvacanciesapp.model.dto.EmailTemplateDTO;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.util.List;

@Service
@Transactional
@Slf4j
public class EmailServiceImpl implements EmailService {

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	public EmailTemplateDTO emailTemplateEnglish;

	@Autowired
	public EmailTemplateDTO emailTemplateSpanish;

	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	@Override
	@ServiceMethod
	public void sendSignUpConfirmationEmail(final String destinationEmailAddress, final String uuid, final Language language) throws MessagingException {
		final String signUpConfirmedLink = this.getSignupConfirmedLink(destinationEmailAddress, uuid, language);
		final EmailDataDTO emailDataDTO = this.getSignUpConfirmationEmailData(destinationEmailAddress, signUpConfirmedLink, language);

		if(log.isInfoEnabled()) {
			log.info(StringUtils.getStringJoined("Trying to send an email to '", destinationEmailAddress, "' with the signup-confirmed link '", signUpConfirmedLink, "'"));
		}
		this.sendMail(emailDataDTO);
	}

	@Override
	@ServiceMethod
	public void sendResetPasswordEmail(final String destinationEmailAddress, final String uuid, final Language language) throws MessagingException {
		final String resetPasswordLink = this.getResetPasswordLink(destinationEmailAddress, uuid, language);
		final EmailDataDTO emailDataDTO = this.getResetPasswordEmailData(destinationEmailAddress, resetPasswordLink, language);

		if(log.isInfoEnabled()) {
			log.info(StringUtils.getStringJoined("Trying to send an email to '", destinationEmailAddress, "' with the reset-password link '", resetPasswordLink, "'"));
		}
		this.sendMail(emailDataDTO);
	}

	private String getSignupConfirmedLink(final String destinationEmailAddress, final String uuid, final Language language) {
		final String appUrl = this.getAppUrl();
		final String languageCode = language.getCode();

		final String accountActivationLink = StringUtils.getStringJoined(appUrl, "/signup-confirmed?email=", destinationEmailAddress, "&uuid=", uuid, "&languageParam=", languageCode);
		return accountActivationLink;
	}

	private String getResetPasswordLink(final String destinationEmailAddress, final String uuid, final Language language) {
		final String appUrl = this.getAppUrl();
		final String languageCode = language.getCode();

		final String resetPasswordLink = StringUtils.getStringJoined(appUrl, "/reset-password?email=", destinationEmailAddress, "&uuid=", uuid, "&languageParam=", languageCode);
		return resetPasswordLink;
	}

	private String getAppUrl() {
		final StringBuffer url = httpServletRequest.getRequestURL();
		final String uri = httpServletRequest.getRequestURI();
		final String host = url.substring(0, url.indexOf(uri));

		final String appUrl = host + httpServletRequest.getContextPath();
		return appUrl;
	}

	private EmailDataDTO getSignUpConfirmationEmailData(final String destinationEmailAddress, final String accountActivationLink, final Language language) {
		final Boolean isHtml = Boolean.TRUE;
		final List<EmailAttachmentDTO> attachments = null;

		final EmailDataDTO emailDataDTO;
		if(Language.SPANISH == language) {
			final String subject = "¡Confirma tu cuenta de JobVacanciesApp!";
			final String textTitle = "<h3>Activación de cuenta de JobVacanciesApp requerida</h3>";
			final String signupConfirmationLinkExpirationHoursString = configPropertiesBean.getSignupConfirmationLinkExpirationHoursString();
			final String textBody = StringUtils.getStringJoined(
				"<p>Haz clic en el siguiente enlace para activar tu cuenta de JobVacanciesApp:</p><p><a href=\"", accountActivationLink, "\">ENLACE DE ACTIVACIÓN DE CUENTA</a></p><p>Tienes ", signupConfirmationLinkExpirationHoursString, " horas para usar este enlace. Después de ese tiempo, tendrás que registrarte de nuevo y activar la cuenta por email para entrar al sitio web.</p><p>Un saludo,</p> <p>el equipo de JobVacanciesApp</p>");

			emailDataDTO = new EmailDataDTO(destinationEmailAddress, subject, textTitle, textBody, isHtml, language, attachments);
		} else {
			final String subject = "Confirm your JobVacanciesApp account!";
			final String textTitle = "<h3>JobVacanciesApp account activation required</h3>";
			final String signupConfirmationLinkExpirationHoursString = configPropertiesBean.getSignupConfirmationLinkExpirationHoursString();
			final String textBody = StringUtils.getStringJoined(
				"<p>Click in the following link to activate your JobVacanciesApp account:</p><p><a href=\"", accountActivationLink, "\">ACCOUNT ACTIVATION LINK</a></p><p>You have  ", signupConfirmationLinkExpirationHoursString, "  hours to use the link. After that time, you'll have to register again and activate the account by email to enter in the website.</p><p>Regards,</p> <p>the JobVacanciesApp team</p>");

			emailDataDTO = new EmailDataDTO(destinationEmailAddress, subject, textTitle, textBody, isHtml, language, attachments);
		}

		return emailDataDTO;
	}

	private EmailDataDTO getResetPasswordEmailData(final String destinationEmailAddress, final String resetPasswordLink, final Language language) {
		final Boolean isHtml = Boolean.TRUE;
		final List<EmailAttachmentDTO> attachments = null;

		final EmailDataDTO emailDataDTO;
		if(Language.SPANISH == language) {
			final String subject = "¡Resetea la contraseña de tu cuenta de JobVacanciesApp!";
			final String textTitle = "<h3>Reseteo de la contraseña de tu cuenta de JobVacanciesApp</h3>";
			final String resetPasswordLinkExpirationHoursString = configPropertiesBean.getResetPasswordLinkExpirationHoursString();
			final String textBody = StringUtils.getStringJoined(
				"<p>Haz clic en el siguiente enlace para resetear la contraseña de tu cuenta de JobVacanciesApp:</p><p><a href=\"", resetPasswordLink, "\">ENLACE DE RESETEO DE CONTRASEÑA</a></p><p>Tienes ", resetPasswordLinkExpirationHoursString, " horas para usar este enlace antes de que expire.</p><p>Un saludo,</p> <p>el equipo de JobVacanciesApp</p>");

			emailDataDTO = new EmailDataDTO(destinationEmailAddress, subject, textTitle, textBody, isHtml, language, attachments);
		} else {
			final String subject = "Reset the password of your JobVacanciesApp account!";
			final String textTitle = "<h3>Reset the password of your JobVacanciesApp account</h3>";
			final String resetPasswordLinkExpirationHoursString = configPropertiesBean.getResetPasswordLinkExpirationHoursString();
			final String textBody = StringUtils.getStringJoined(
				"<p>Click in the following link to reset the password of your JobVacanciesApp account:</p><p><a href=\"", resetPasswordLink, "\">RESET PASSWORD LINK</a></p><p>You have ", resetPasswordLinkExpirationHoursString, " hours to use the link before it expires.</p><p>Regards,</p> <p>the JobVacanciesApp team</p>");

			emailDataDTO = new EmailDataDTO(destinationEmailAddress, subject, textTitle, textBody, isHtml, language, attachments);
		}

		return emailDataDTO;
	}

	private void sendMail(final EmailDataDTO emailDataDTO) throws MessagingException {
		final EmailTemplateDTO emailTemplateDTO;
		if(Language.SPANISH == emailDataDTO.getLanguage()) {
			emailTemplateDTO = emailTemplateSpanish;
		} else {
			emailTemplateDTO = emailTemplateEnglish;
		}

		final String originEmailAddress = emailTemplateDTO.getOriginEmailAddress();
		final String destinationEmailAddress = emailDataDTO.getDestinationEmailAddress();
		final String subject = emailDataDTO.getSubject();
		final String text = EmailServiceImpl.getText(emailTemplateDTO, emailDataDTO);
		final Boolean isHtml = emailDataDTO.getIsHtml();
		final List<EmailAttachmentDTO> attachments = emailDataDTO.getAttachments();

		if(isHtml == null) {
			if(LogicalUtils.isNullOrEmpty(attachments)) {
				this.sendSimpleMail(originEmailAddress, destinationEmailAddress, subject, text);
			} else {
				this.sendComplexMail(originEmailAddress, destinationEmailAddress, subject, text, false, attachments);
			}
		} else {
			this.sendComplexMail(originEmailAddress, destinationEmailAddress, subject, text, isHtml, attachments);
		}
	}

	private static String getText(final EmailTemplateDTO emailTemplateDTO, final EmailDataDTO emailDataDTO) {
		final String textTemplate = emailTemplateDTO.getTextTemplate();
		final String textTitle = emailDataDTO.getTextTitle();
		final String textBody = emailDataDTO.getTextBody();

		final String text = String.format(textTemplate, textTitle, textBody);
		return text;
	}

	private void sendSimpleMail(final String originEmailAddress, final String destinationEmailAddress, final String subject, final String text) {
		final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(originEmailAddress);
		simpleMailMessage.setTo(destinationEmailAddress);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(text);

		emailSender.send(simpleMailMessage);
	}

	private void sendComplexMail(final String originEmailAddress, final String destinationEmailAddress, final String subject, final String text, final boolean isHtml, final List<EmailAttachmentDTO> attachments) throws MessagingException {
		final MimeMessage mimeMessage = emailSender.createMimeMessage();

		final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setFrom(originEmailAddress);
		mimeMessageHelper.setTo(destinationEmailAddress);
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setText(text, isHtml);

		if(attachments != null) {
			for(final EmailAttachmentDTO attachment : attachments) {
				final String attachmentFileName = attachment.getAttachmentFileName();
				final String attachmentFilePath = attachment.getAttachmentFilePath();
				final Path attachmentPath = Path.of(attachmentFilePath);
				final FileSystemResource attachmentFile = new FileSystemResource(attachmentPath);
				mimeMessageHelper.addAttachment(attachmentFileName, attachmentFile);
			}
		}

		emailSender.send(mimeMessage);
	}
}
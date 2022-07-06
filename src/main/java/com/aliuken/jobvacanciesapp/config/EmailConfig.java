package com.aliuken.jobvacanciesapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class EmailConfig {

	@Bean("templateSimpleMessageEnglish")
	public SimpleMailMessage templateSimpleMessageEnglish() {
		final SimpleMailMessage templateSimpleMessage = new SimpleMailMessage();
	    templateSimpleMessage.setFrom("noreply@aliuken.com");
	    templateSimpleMessage.setText(
	      "<h3>%s</h3>Dear customer,\n\n%s\n\nPlease don't reply to this email.\n\nRegards,\n\nthe JobVacanciesApp team.");
	    return templateSimpleMessage;
	}
	
	@Bean("templateSimpleMessageSpanish")
	public SimpleMailMessage templateSimpleMessageSpanish() {
		final SimpleMailMessage templateSimpleMessage = new SimpleMailMessage();
	    templateSimpleMessage.setFrom("noreply@aliuken.com");
	    templateSimpleMessage.setText(
	      "<h3>%s</h3>Estimado cliente,\n\n%s\n\nPor favor, no responda a este email.\n\nUn saludo,\n\nel equipo de JobVacanciesApp.");
	    return templateSimpleMessage;
	}

}
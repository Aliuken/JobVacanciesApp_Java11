package com.aliuken.jobvacanciesapp.config;

import com.aliuken.jobvacanciesapp.model.dto.EmailTemplateDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

	@Bean("emailTemplateEnglish")
	EmailTemplateDTO emailTemplateEnglish() {
		final String originEmailAddress = "noreply@aliuken.com";
		final String textTemplate = "%s\n\nDear customer,\n\n%s\n\nPlease don't reply to this email.\n\nRegards,\n\nthe JobVacanciesApp team.";

		final EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO(originEmailAddress, textTemplate);
		return emailTemplateDTO;
	}

	@Bean("emailTemplateSpanish")
	EmailTemplateDTO emailTemplateSpanish() {
		final String originEmailAddress = "noreply@aliuken.com";
		final String textTemplate = "%s\n\nEstimado cliente,\n\n%s\n\nPor favor, no responda a este email.\n\nUn saludo,\n\nel equipo de JobVacanciesApp.";

		final EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO(originEmailAddress, textTemplate);
		return emailTemplateDTO;
	}
}
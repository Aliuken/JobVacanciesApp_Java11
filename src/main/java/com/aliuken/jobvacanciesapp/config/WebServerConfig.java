package com.aliuken.jobvacanciesapp.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServerConfig {

	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
	    final TomcatServletWebServerFactory webServerFactory = new TomcatServletWebServerFactory();
	    webServerFactory.setPort(8080);
	    return webServerFactory;
	}

}
package com.aliuken.jobvacanciesapp.config;

import org.apache.coyote.http2.Http2Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServerConfig {

	@Bean
	ConfigurableServletWebServerFactory webServerFactory() {
//		final ServerProperties serverProperties = new ServerProperties();
//		serverProperties.setPort(8080);
//		final Http2 http2 = serverProperties.getHttp2();

		final Http2Protocol http2Protocol = new Http2Protocol();

		final TomcatServletWebServerFactory webServerFactory = new TomcatServletWebServerFactory();
		webServerFactory.setPort(8080);
//		webServerFactory.setHttp2(http2);
		webServerFactory.addConnectorCustomizers(connector -> connector.addUpgradeProtocol(http2Protocol));
		return webServerFactory;
	}
}
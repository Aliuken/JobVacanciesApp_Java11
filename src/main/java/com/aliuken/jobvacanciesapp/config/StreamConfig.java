package com.aliuken.jobvacanciesapp.config;

import com.aliuken.jobvacanciesapp.util.javase.stream.StreamUtilsImpl;
import com.aliuken.jobvacanciesapp.util.javase.stream.superclass.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StreamConfig {

	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	@Bean
	StreamUtils streamUtils() throws Exception {
		final boolean useParallelStreams = configPropertiesBean.isUseParallelStreams();
		final StreamUtils streamUtils = StreamUtilsImpl.getInstance(useParallelStreams);

		return streamUtils;
	}
}
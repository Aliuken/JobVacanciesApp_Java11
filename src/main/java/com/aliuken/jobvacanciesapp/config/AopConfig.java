package com.aliuken.jobvacanciesapp.config;

import com.aliuken.jobvacanciesapp.aop.aspect.ControllerAspect;
import com.aliuken.jobvacanciesapp.aop.aspect.RepositoryAspect;
import com.aliuken.jobvacanciesapp.aop.aspect.ServiceAspect;
import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
	@Bean
	ControllerAspect controllerAspect() {
		final ControllerAspect controllerAspect = Aspects.aspectOf(ControllerAspect.class);
		return controllerAspect;
	}

	@Bean
	ServiceAspect serviceAspect() {
		final ServiceAspect serviceAspect = Aspects.aspectOf(ServiceAspect.class);
		return serviceAspect;
	}

	@Bean
	RepositoryAspect repositoryAspect() {
		final RepositoryAspect repositoryAspect = Aspects.aspectOf(RepositoryAspect.class);
		return repositoryAspect;
	}
}

package com.aliuken.jobvacanciesapp.config;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

import com.aliuken.jobvacanciesapp.util.DateTimeUtils;
import com.aliuken.jobvacanciesapp.util.DateUtils;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import nz.net.ultraq.thymeleaf.layoutdialect.decorators.strategies.GroupingStrategy;

@Configuration
public class WebTemplateConfig {

	@Autowired
	ServletContext servletContext;

    @Autowired
    SpringResourceTemplateResolver springResourceTemplateResolver;

    @Value("${spring.thymeleaf.cache}")
    Boolean springThymeleafCache;

    @Bean
    public UrlTemplateResolver urlTemplateResolver() {
    	final UrlTemplateResolver urlTemplateResolver = new UrlTemplateResolver();
        urlTemplateResolver.setCacheable(springThymeleafCache);

        return urlTemplateResolver;
    }

    @Bean
    public ServletContextTemplateResolver servletContextTemplateResolver() {
    	final ServletContextTemplateResolver servletContextTemplateResolver = new ServletContextTemplateResolver(servletContext);
        servletContextTemplateResolver.setCacheable(false);
        servletContextTemplateResolver.setTemplateMode("HTML");
        servletContextTemplateResolver.setCharacterEncoding("UTF-8");

        return servletContextTemplateResolver;
    }

    @Bean
    public SpringTemplateEngine springTemplateEngine() {
    	final SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.addTemplateResolver(springResourceTemplateResolver);
        springTemplateEngine.addTemplateResolver(urlTemplateResolver());
        springTemplateEngine.addTemplateResolver(servletContextTemplateResolver());
        springTemplateEngine.addDialect(new SpringSecurityDialect());
        springTemplateEngine.addDialect(new LayoutDialect(new GroupingStrategy()));
        springTemplateEngine.addDialect(new Java8TimeDialect());

        return springTemplateEngine;
    }

    @Bean
    public ViewResolver viewResolver() {
    	final ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(springTemplateEngine());
        thymeleafViewResolver.setCharacterEncoding("UTF-8");
        thymeleafViewResolver.setCache(false);
        thymeleafViewResolver.setOrder(1);

        // To use:     ${dateUtils.convertToStringForWebPageField(localDateVar)}
        // Instead of: ${(localDateVar != null) ? #temporals.format(localDateVar, 'dd-MM-yyyy') : '-'}
        thymeleafViewResolver.addStaticVariable("dateUtils", DateUtils.getInstance());

        // To use:     ${dateTimeUtils.convertToStringForWebPageField(localDateTimeVar)}
        // Instead of: ${(localDateTimeVar != null) ? #temporals.format(localDateTimeVar, 'dd-MM-yyyy HH:mm:ss') : '-'}
        thymeleafViewResolver.addStaticVariable("dateTimeUtils", DateTimeUtils.getInstance());

        return thymeleafViewResolver;
    }

}
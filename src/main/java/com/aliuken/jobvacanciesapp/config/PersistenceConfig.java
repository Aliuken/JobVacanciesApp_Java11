package com.aliuken.jobvacanciesapp.config;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.aliuken.jobvacanciesapp.repository")
public class PersistenceConfig {
	private static final DataSource DATA_SOURCE = PersistenceConfig.getDataSource();
	private static final AbstractEntityManagerFactoryBean ENTITY_MANAGER_FACTORY_BEAN = PersistenceConfig.getEntityManagerFactoryBean(PersistenceConfig.DATA_SOURCE);
	private static final PlatformTransactionManager TRANSACTION_MANAGER = PersistenceConfig.getTransactionManager(PersistenceConfig.ENTITY_MANAGER_FACTORY_BEAN);

	private static DataSource getDataSource() {
		String mysqlHost = System.getenv("MYSQL_HOST");
		if(mysqlHost == null) {
			mysqlHost = "localhost";
		}

		String mysqlPort = System.getenv("MYSQL_PORT");
		if(mysqlPort == null) {
			mysqlPort = "3306";
		}

		String mysqlUser = System.getenv("MYSQL_USER");
		if(mysqlUser == null) {
			mysqlUser = "root";
		}

		String mysqlPassword = System.getenv("MYSQL_PASSWORD");
		if(mysqlPassword == null) {
			mysqlPassword = "admin";
		}

		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl(StringUtils.getStringJoined("jdbc:mysql://", mysqlHost, ":", mysqlPort, "/job-vacancies-app-db?useSSL=false&serverTimezone=Europe/Madrid&allowPublicKeyRetrieval=true"));
		dataSource.setUsername(mysqlUser);
		dataSource.setPassword(mysqlPassword);

		return dataSource;
	}

	private static AbstractEntityManagerFactoryBean getEntityManagerFactoryBean(final DataSource dataSource) {
		final JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

		final Properties additionalProperties = new Properties();
		additionalProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		additionalProperties.setProperty("hibernate.format_sql", "true");
		additionalProperties.setProperty("hibernate.enable_lazy_load_no_trans", "true");

		final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setPackagesToScan(new String[]{"com.aliuken.jobvacanciesapp.model"});
		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		entityManagerFactoryBean.setJpaProperties(additionalProperties);

		return entityManagerFactoryBean;
	}

	private static PlatformTransactionManager getTransactionManager(final AbstractEntityManagerFactoryBean entityManagerFactoryBean) {
		final EntityManagerFactory entityManagerFactory = entityManagerFactoryBean.getObject();
//		final EntityManagerFactory entityManagerFactory = entityManagerFactoryBean.getNativeEntityManagerFactory();

		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);

		return transactionManager;
	}

	@Bean
	DataSource dataSource() {
		return PersistenceConfig.DATA_SOURCE;
	}

	@Bean
	AbstractEntityManagerFactoryBean entityManagerFactory() {
		return PersistenceConfig.ENTITY_MANAGER_FACTORY_BEAN;
	}

	@Bean
	PlatformTransactionManager transactionManager() {
		return PersistenceConfig.TRANSACTION_MANAGER;
	}

	@Bean
	PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		final PersistenceExceptionTranslationPostProcessor exceptionTranslation = new PersistenceExceptionTranslationPostProcessor();

		return exceptionTranslation;
	}
}

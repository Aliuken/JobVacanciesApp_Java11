package com.aliuken.jobvacanciesapp.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.aliuken.jobvacanciesapp.model.dto.ViewsAllowedDTO;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	private static final String USERS_BY_USERNAME_QUERY = WebSecurityConfig.getUsersByUsernameQuery();
	private static final String AUTHORITIES_BY_USERNAME_QUERY = WebSecurityConfig.getAuthoritiesByUsernameQuery();
	private static final String[] STATIC_RESOURCES_ARRAY = WebSecurityConfig.getStaticResourcesArray();
	private ViewsAllowedDTO VIEWS_ALLOWED_DTO;

	@Autowired
	private JobVacanciesAppConfigPropertiesBean jobVacanciesAppConfigPropertiesBean;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcPersistentTokenRepositoryImpl jdbcPersistentTokenRepositoryImpl;

	@Autowired
	private CustomAuthenticationHandler customAuthenticationHandler;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@PostConstruct
	private void postConstruct() {
		VIEWS_ALLOWED_DTO = this.getViewsAllowedDTO();
	}

	/**
	 * Implementación de Spring Security que encripta passwords con el algoritmo Bcrypt
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return passwordEncoder;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
	    authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource)
	    	.passwordEncoder(passwordEncoder)
			.usersByUsernameQuery(USERS_BY_USERNAME_QUERY)
			.authoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_QUERY);
	}

	private static final String getUsersByUsernameQuery() {
		return "select au.email, auc.encrypted_password, au.enabled from auth_user au, auth_user_credentials auc where au.email = ? and au.email = auc.email";
	}

	private static final String getAuthoritiesByUsernameQuery() {
		return "select au.email, ar.name from auth_user_role aur inner join auth_user au on au.id = aur.auth_user_id inner join auth_role ar on ar.id = aur.auth_role_id where au.email = ?";
	}

	/**
	 * Personalizamos el Acceso a las URLs de la aplicación
	 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
		    .csrf().disable()
		    .authorizeRequests()

			// Static resources don't require authentication
			.antMatchers(STATIC_RESOURCES_ARRAY).permitAll()

			// Public views don't require authentication
			.antMatchers(VIEWS_ALLOWED_DTO.getAnonymousViewsArray()).permitAll()

			// Assign permissions to URLs by ROLES
			.antMatchers(VIEWS_ALLOWED_DTO.getUserViewsArray()).hasAnyAuthority("USER", "SUPERVISOR", "ADMINISTRATOR")
			.antMatchers(VIEWS_ALLOWED_DTO.getSupervisorViewsArray()).hasAnyAuthority("SUPERVISOR", "ADMINISTRATOR")
			.antMatchers(VIEWS_ALLOWED_DTO.getAdministratorViewsArray()).hasAnyAuthority("ADMINISTRATOR")

			// The rest of the URLs in the application require authentication
			.anyRequest().authenticated()

			// Configuration for login, logout and remember me
			.and().formLogin().successHandler(customAuthenticationHandler).loginPage("/login").usernameParameter("email").passwordParameter("password").permitAll()
			.and().logout().logoutSuccessHandler(customAuthenticationHandler).logoutUrl("/logout").logoutSuccessUrl("/index").permitAll()
			.and().rememberMe().tokenRepository(jdbcPersistentTokenRepositoryImpl);

		return httpSecurity.build();
	}

	private static final String[] getStaticResourcesArray() {
		return new String[]{
			"/images/**", "/job-company-logos/**", "/auth-user-curriculum-files/**", "/jobvacanciesapp-javascript-utils/**", "/jquery/**", "/jquery-ui-1.13.1/**", "/jquery-timepicker-addon-1.6.3-dist/**", "/tinymce-6.1.0/**", "/bootstrap-5.2.0-dist/**", "/MDB5-STANDARD-UI-KIT-Free-4.2.0/**"};
	}

	private final ViewsAllowedDTO getViewsAllowedDTO() {
		final boolean anonymousAccessAllowed = jobVacanciesAppConfigPropertiesBean.isAnonymousAccessAllowed();

		final String[] anonymousViewsArray;
		final String[] userViewsArray;
		final String[] supervisorViewsArray;
		final String[] administratorViewsArray;
		if(anonymousAccessAllowed) {
			anonymousViewsArray = new String[]{"/login", "/signup", "/signup-confirmed", "/about", "/", "/search", "/auth-users/view/**", "/job-categories/view/**", "/job-companies/view/**", "/job-vacancies/view/**"};
			userViewsArray = new String[]{"/my-user/auth-users/**", "/my-user/auth-user-curriculums/**", "/job-requests/create/**", "/job-requests/save/**"};
			supervisorViewsArray = new String[]{"/job-requests/**", "/job-vacancies/**", "/job-categories/**", "/job-companies/**"};
			administratorViewsArray = new String[]{"/auth-users/**"};
		} else {
			anonymousViewsArray = new String[]{"/login", "/signup", "/signup-confirmed", "/about"};
			userViewsArray = new String[]{"/my-user/auth-users/**", "/my-user/auth-user-curriculums/**", "/job-requests/create/**", "/job-requests/save/**", "/", "/search", "/auth-users/view/**", "/job-categories/view/**", "/job-companies/view/**", "/job-vacancies/view/**"};
			supervisorViewsArray = new String[]{"/job-requests/**", "/job-vacancies/**", "/job-categories/**", "/job-companies/**"};
			administratorViewsArray = new String[]{"/auth-users/**"};
		}

		final ViewsAllowedDTO viewsAllowedDTO = new ViewsAllowedDTO();
		viewsAllowedDTO.setAnonymousViewsArray(anonymousViewsArray);
		viewsAllowedDTO.setUserViewsArray(userViewsArray);
		viewsAllowedDTO.setSupervisorViewsArray(supervisorViewsArray);
		viewsAllowedDTO.setAdministratorViewsArray(administratorViewsArray);

		return viewsAllowedDTO;
	}

}
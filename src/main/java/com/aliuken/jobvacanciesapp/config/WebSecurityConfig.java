package com.aliuken.jobvacanciesapp.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import com.aliuken.jobvacanciesapp.enumtype.AllowedViewsEnum;
import com.aliuken.jobvacanciesapp.security.CustomAuthenticationHandler;
import com.aliuken.jobvacanciesapp.service.JdbcTokenByEmailService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends GlobalAuthenticationConfigurerAdapter {
	private static final String USERS_BY_USERNAME_QUERY = WebSecurityConfig.getUsersByUsernameQuery();
	private static final String AUTHORITIES_BY_USERNAME_QUERY = WebSecurityConfig.getAuthoritiesByUsernameQuery();
	private static final String[] STATIC_RESOURCES_ARRAY = WebSecurityConfig.getStaticResourcesArray();
	private static final Map<Boolean, SecurityFilterChain> SECURITY_FILTER_CHAIN_MAP = new HashMap<>();

	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTokenByEmailService jdbcTokenByEmailService;

	@Autowired
	private CustomAuthenticationHandler customAuthenticationHandler;

	private static final int BCRYPT_LOG_ROUNDS = 12;
	private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(WebSecurityConfig.BCRYPT_LOG_ROUNDS);

	/**
	 * Spring Security password encoder implementation using BCrypt algorithm
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return WebSecurityConfig.PASSWORD_ENCODER;
	}

	@Bean
	public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
		final AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
		return authenticationManager;
	}

	/**
	 * Configuration of the authentication via JDBC
	 */
	@Override
	public void init(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		final JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> jdbcUserDetailsManagerConfigurer = authenticationManagerBuilder.jdbcAuthentication();

		jdbcUserDetailsManagerConfigurer
			.dataSource(dataSource)
			.passwordEncoder(WebSecurityConfig.PASSWORD_ENCODER)
			.usersByUsernameQuery(WebSecurityConfig.USERS_BY_USERNAME_QUERY)
			.authoritiesByUsernameQuery(WebSecurityConfig.AUTHORITIES_BY_USERNAME_QUERY);
	}

	private static final String getUsersByUsernameQuery() {
		return "select au.email, auc.encrypted_password, au.enabled from auth_user au, auth_user_credentials auc where au.email = ? and au.email = auc.email";
	}

	private static final String getAuthoritiesByUsernameQuery() {
		return "select au.email, ar.name from auth_user_role aur inner join auth_user au on au.id = aur.auth_user_id inner join auth_role ar on ar.id = aur.auth_role_id where au.email = ?";
	}

//	@Bean
//	public FilterChainProxy springSecurityFilterChain(final HttpSecurity httpSecurity) throws ServletException, Exception {
//		final SecurityFilterChain securityFilterChain = filterChain(httpSecurity);
//		final FilterChainProxy filterChainProxy = new FilterChainProxy(securityFilterChain);
//
//		return filterChainProxy;
//	}

	@Bean
	public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
		final Boolean anonymousAccessAllowed = configPropertiesBean.getCurrentAnonymousAccessAllowed();

		final SecurityFilterChain securityFilterChain;
		if(WebSecurityConfig.SECURITY_FILTER_CHAIN_MAP.containsKey(anonymousAccessAllowed)) {
			securityFilterChain = WebSecurityConfig.SECURITY_FILTER_CHAIN_MAP.get(anonymousAccessAllowed);
		} else {
			securityFilterChain = filterChain(httpSecurity, anonymousAccessAllowed);
			WebSecurityConfig.SECURITY_FILTER_CHAIN_MAP.put(anonymousAccessAllowed, securityFilterChain);
		}

		return securityFilterChain;
	}

	/**
	 * Customization of access to the application URLs and the login, logout and remember-me functionalities
	 */
	private SecurityFilterChain filterChain(final HttpSecurity httpSecurity, final boolean anonymousAccessAllowed) throws Exception {
		final AllowedViewsEnum allowedViewsEnum = AllowedViewsEnum.getInstance(anonymousAccessAllowed);

		httpSecurity
			.csrf()
				.disable()

			.authorizeHttpRequests()
				// Static resources don't require authentication
				.antMatchers(WebSecurityConfig.STATIC_RESOURCES_ARRAY).permitAll()

				// Public views don't require authentication
				.antMatchers(allowedViewsEnum.getAnonymousViewsArray()).permitAll()

				// Assign permissions to URLs by roles
				.antMatchers(allowedViewsEnum.getUserViewsArray()).hasAnyAuthority("USER", "SUPERVISOR", "ADMINISTRATOR")
				.antMatchers(allowedViewsEnum.getSupervisorViewsArray()).hasAnyAuthority("SUPERVISOR", "ADMINISTRATOR")
				.antMatchers(allowedViewsEnum.getAdministratorViewsArray()).hasAnyAuthority("ADMINISTRATOR")
//				.antMatchers(allowedViewsEnum.getUserViewsArray()).access(AuthorityAuthorizationManager.hasAnyAuthority("USER", "SUPERVISOR", "ADMINISTRATOR"))
//				.antMatchers(allowedViewsEnum.getSupervisorViewsArray()).access(AuthorityAuthorizationManager.hasAnyAuthority("SUPERVISOR", "ADMINISTRATOR"))
//				.antMatchers(allowedViewsEnum.getAdministratorViewsArray()).access(AuthorityAuthorizationManager.hasAnyAuthority("ADMINISTRATOR"))

				// The rest of the URLs in the application require authentication
				.anyRequest().authenticated()

			// Configuration for login, logout and remember me
			.and().formLogin()
				.successHandler(customAuthenticationHandler)
				.loginPage("/login")
				.usernameParameter("email")
				.passwordParameter("password")
//				.defaultSuccessUrl("/",true)
				.permitAll()
			.and().logout()
				.addLogoutHandler(customAuthenticationHandler)
				.logoutUrl("/logout")
				.permitAll()
			.and().rememberMe()
				.tokenRepository(jdbcTokenByEmailService);

		final DefaultSecurityFilterChain defaultSecurityFilterChain = httpSecurity.build();

		return defaultSecurityFilterChain;
	}

//	public void setSecurityFilterChain(final String nextDefaultLanguageCode, final String nextAnonymousAccessPermissionName, final String nextDefaultInitialTablePageSizeValue, final String nextDefaultColorModeCode, final String nextUserInterfaceFrameworkCode) throws Exception {
//		MainApp.restartApp(nextDefaultLanguageCode, nextAnonymousAccessPermissionName, nextDefaultInitialTablePageSizeValue, nextDefaultColorModeCode, nextUserInterfaceFrameworkCode);
//		BeanUtils.refreshBean("springSecurityFilterChain");
//	}

	private static final String[] getStaticResourcesArray() {
		return new String[]{
			"/auth-user-curriculum-files/**",
			"/job-company-logos/**",
			"/bootstrap-5.3.2-dist/**",
			"/fontawesome-free-6.5.1-web/**",
			"/images/**",
			"/jobvacanciesapp-utils/**",
			"/jquery/**",
			"/jquery-timepicker-addon-1.6.3-dist/**",
			"/jquery-ui-1.13.2/**",
			"/MDB5-STANDARD-UI-KIT-Free-7.1.0/**",
			"/tinymce-6.8.2/**"
		};
	}

}
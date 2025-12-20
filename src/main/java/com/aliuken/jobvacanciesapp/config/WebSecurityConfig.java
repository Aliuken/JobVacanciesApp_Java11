package com.aliuken.jobvacanciesapp.config;

import com.aliuken.jobvacanciesapp.enumtype.AllowedViewsEnum;
import com.aliuken.jobvacanciesapp.enumtype.AnonymousAccessPermission;
import com.aliuken.jobvacanciesapp.security.CustomAuthenticationHandler;
import com.aliuken.jobvacanciesapp.service.JdbcTokenByEmailService;
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

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends GlobalAuthenticationConfigurerAdapter {
	private static final String USERS_BY_USERNAME_QUERY = WebSecurityConfig.getUsersByUsernameQuery();
	private static final String AUTHORITIES_BY_USERNAME_QUERY = WebSecurityConfig.getAuthoritiesByUsernameQuery();
	private static final String[] STATIC_RESOURCES_ARRAY = WebSecurityConfig.getStaticResourcesArray();
	private static final Map<AnonymousAccessPermission, SecurityFilterChain> SECURITY_FILTER_CHAIN_MAP = new HashMap<>();

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
	PasswordEncoder passwordEncoder() {
		return WebSecurityConfig.PASSWORD_ENCODER;
	}

	@Bean
	AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
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
	SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
		final AnonymousAccessPermission currentDefaultAnonymousAccessPermission = ConfigPropertiesBean.CURRENT_DEFAULT_ANONYMOUS_ACCESS_PERMISSION;

		final SecurityFilterChain securityFilterChain;
		if(WebSecurityConfig.SECURITY_FILTER_CHAIN_MAP.containsKey(currentDefaultAnonymousAccessPermission)) {
			securityFilterChain = WebSecurityConfig.SECURITY_FILTER_CHAIN_MAP.get(currentDefaultAnonymousAccessPermission);
		} else {
			securityFilterChain = filterChain(httpSecurity, currentDefaultAnonymousAccessPermission);
			WebSecurityConfig.SECURITY_FILTER_CHAIN_MAP.put(currentDefaultAnonymousAccessPermission, securityFilterChain);
		}

		return securityFilterChain;
	}

	/**
	 * Customization of access to the application URLs and the login, logout and remember-me functionalities
	 */
	private SecurityFilterChain filterChain(final HttpSecurity httpSecurity, final AnonymousAccessPermission anonymousAccessPermission) throws Exception {
		final AllowedViewsEnum allowedViewsEnum = AllowedViewsEnum.getInstance(anonymousAccessPermission);

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

//    public void setSecurityFilterChain(final String nextDefaultLanguageCode, final String nextDefaultAnonymousAccessPermissionValue, final String nextDefaultInitialTableSortingDirectionCode, final String nextDefaultInitialTablePageSizeValue, final String nextDefaultColorModeCode, final String nextDefaultUserInterfaceFrameworkCode, final String nextDefaultPdfDocumentPageFormatCode) throws Exception {
//        MainClass.restartApp(nextDefaultLanguageCode, nextDefaultAnonymousAccessPermissionValue, nextDefaultInitialTableSortingDirectionCode, nextDefaultInitialTablePageSizeValue, nextDefaultColorModeCode, nextDefaultUserInterfaceFrameworkCode, nextDefaultPdfDocumentPageFormatCode);
//        BeanUtils.refreshBean("springSecurityFilterChain");
//    }

	private static String[] getStaticResourcesArray() {
		return new String[]{
			"/auth-user-curriculum-files/**",
			"/auth-user-entity-query-files/**",
			"/job-company-logos/**",
			"/bootstrap-5.3.8-dist/**",
			"/fontawesome-free-7.1.0-web/**",
			"/images/**",
			"/jobvacanciesapp-utils/**",
			"/jquery/**",
			"/jquery-timepicker-addon-1.6.3-dist/**",
			"/jquery-ui-1.14.1/**",
			"/MDB5-STANDARD-UI-KIT-Free-9.3.0/**",
			"/tinymce-8.3.1/**"
		};
	}
}
package com.aliuken.jobvacanciesapp.util.security;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SpringSecurityUtils {

	private static final SpringSecurityUtils SINGLETON_INSTANCE = new SpringSecurityUtils();

	private SpringSecurityUtils(){}

	public static SpringSecurityUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

	private Authentication getAuthentication() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		final Authentication authentication = securityContext.getAuthentication();
		return authentication;
	}

	public boolean isAuthenticated() {
		final Authentication authentication = this.getAuthentication();

		final boolean isAuthenticated = (authentication != null && authentication.isAuthenticated());
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		final Authentication authentication = this.getAuthentication();

		if(authentication != null) {
			authentication.setAuthenticated(isAuthenticated);
		}
	}

	public boolean isAnonymous() {
		final Authentication authentication = this.getAuthentication();

		final boolean result = (authentication instanceof AnonymousAuthenticationToken);
		return result;
	}

	public boolean hasAnyAuthority(final String... authorityNamesVarargs) {
		if(LogicalUtils.isNotNullNorEmpty(authorityNamesVarargs)) {
			final Set<String> grantedAuthorityNames = this.getAuthorityNames();

			if(LogicalUtils.isNotNullNorEmpty(grantedAuthorityNames)) {
				for(final String authorityName : authorityNamesVarargs) {
					if(grantedAuthorityNames.contains(authorityName)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isAuthenticatedAndHasAnyAuthority(final String... authorityNamesVarargs) {
		final boolean isAuthenticatedAndHasAnyAuthority = (this.isAuthenticated() && this.hasAnyAuthority(authorityNamesVarargs));
		return isAuthenticatedAndHasAnyAuthority;
	}

	public String getUsername() {
		final Authentication authentication = this.getAuthentication();

		final String username;
		if(authentication != null) {
			username = authentication.getName();
		} else {
			username = null;
		}

		return username;
	}

	public String getMaxPriorityAuthorityName() {
		final Set<String> grantedAuthorityNames = this.getAuthorityNames();

		if(grantedAuthorityNames.contains(AuthRole.ADMINISTRATOR)) {
			return AuthRole.ADMINISTRATOR;
		} else if(grantedAuthorityNames.contains(AuthRole.SUPERVISOR)) {
			return AuthRole.SUPERVISOR;
		} else if(grantedAuthorityNames.contains(AuthRole.USER)) {
			return AuthRole.USER;
		} else {
			return null;
		}
	}

	public Set<String> getAuthorityNames() {
		final Authentication authentication = this.getAuthentication();

		final Collection<? extends GrantedAuthority> grantedAuthorities;
		if(authentication != null) {
			grantedAuthorities = authentication.getAuthorities();
		} else {
			grantedAuthorities = null;
		}

		final Set<String> grantedAuthorityNames = Constants.PARALLEL_STREAM_UTILS.ofNullableCollection(grantedAuthorities)
				.map(grantedAuthority -> grantedAuthority.getAuthority())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return grantedAuthorityNames;
	}

	public RegexRequestMatcher[] getRegexRequestMatcherArray(final String[] antPatterns) {
		if(antPatterns == null) {
			return new RegexRequestMatcher[0];
		}

		final RegexRequestMatcher[] regexRequestMatcherArray = new RegexRequestMatcher[antPatterns.length];
		for(int i = 0; i < antPatterns.length; i++) {
			final String antPattern = antPatterns[i];
			final String regexPattern = this.convertAntPatternToRegexPattern(antPattern);
			regexRequestMatcherArray[i] = new RegexRequestMatcher(regexPattern, null);
		}
		return regexRequestMatcherArray;
	}

	private String convertAntPatternToRegexPattern(final String antPattern) {
		if (antPattern == null || antPattern.isEmpty()) {
			return Constants.EMPTY_STRING;
		}

		final char[] chars = antPattern.toCharArray();
		final int length = chars.length;

		final StringBuilder regexPatternStringBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			final char c = chars[i];
			if (c == '*') {
				// Handling **
				final boolean isDoubleStar = (i + 1 < length && chars[i + 1] == '*');
				if (isDoubleStar) {
					regexPatternStringBuilder.append(".*");
					i++; // skip the second *
				} else {
					regexPatternStringBuilder.append("[^/]*");
				}
			} else if (c == '?') {
				// Handling ?
				regexPatternStringBuilder.append('.');
			} else if ("+()^$.{}[]|\\".indexOf(c) != -1) {
				// Escaping special characters from regex
				regexPatternStringBuilder.append("\\").append(c);
			} else {
				// Handling normal character
				regexPatternStringBuilder.append(c);
			}
		}

		final String regexPattern = StringUtils.getStringJoined("^", regexPatternStringBuilder.toString(), "$");
		return regexPattern;
	}
}
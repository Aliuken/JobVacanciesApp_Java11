package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class EndpointRegexPatternDTO implements Serializable {
	private static final long serialVersionUID = 4846693014925422004L;

	private static final EndpointRegexPatternDTO NO_ARGS_INSTANCE = new EndpointRegexPatternDTO(null, null, null, null, null);

	@NotNull
	private final HttpMethod httpMethod;

	@NotNull
	private final String httpMethodName;

	@NotNull
	private final Pattern pathRegexPattern;

	@NotNull
	private final String pathRegex;

	@NotNull
	private final String description;

	public static EndpointRegexPatternDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	public static EndpointRegexPatternDTO getNewInstance(final HttpMethod httpMethod, final String pathRegex, final String description) {
		final String httpMethodName = httpMethod.name();
		final Pattern pathRegexPattern = Pattern.compile(pathRegex);
		final EndpointRegexPatternDTO endpointRegexPatternDTO = new EndpointRegexPatternDTO(httpMethod, httpMethodName, pathRegexPattern, pathRegex, description);
		return endpointRegexPatternDTO;
	}

	public boolean matches(final String httpMethodNameToMatch, final String pathToMatch) {
		final boolean result;
		if(httpMethodName.equals(httpMethodNameToMatch)) {
			final Matcher matcher = pathRegexPattern.matcher(pathToMatch);
			result = matcher.matches();
		} else {
			result = false;
		}
		return result;
	}

	public String getEndpointRegexPatternAsString() {
		final String result = StringUtils.getStringJoined(httpMethodName, Constants.SPACE, pathRegex);
		return result;
	}

	@Override
	public String toString() {
		final String result = StringUtils.getStringJoined("EndpointRegexPatternDTO [httpMethod=", httpMethodName, ", pathRegex=", pathRegex, ", description=", description, "]");
		return result;
	}
}
package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpMethod;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

import lombok.Data;

@Data
public class EndpointPatternDTO implements Serializable {
	private static final long serialVersionUID = 4846693014925422004L;

	private static final EndpointPatternDTO NO_ARGS_INSTANCE = new EndpointPatternDTO(null, null);

	@NotNull
	public final String httpMethod;

	@NotNull
	public final Pattern pathPattern;

	public static EndpointPatternDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	public static EndpointPatternDTO getNewInstance(final HttpMethod httpMethod, final Pattern pathPattern) {
		final EndpointPatternDTO endpointPatternDTO = new EndpointPatternDTO(httpMethod.name(), pathPattern);
		return endpointPatternDTO;
	}

	public String getEndpointPatternAsString() {
		final String result = StringUtils.getStringJoined(httpMethod, " ", pathPattern.toString());
		return result;
	}

	@Override
	public String toString() {
		final String result = StringUtils.getStringJoined("EndpointPatternDTO [httpMethod=", httpMethod, ", pathPattern=", pathPattern.toString(), "]");
		return result;
	}
}
package com.aliuken.jobvacanciesapp.model;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;

import com.aliuken.jobvacanciesapp.util.StringUtils;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

import lombok.Data;

@Data
public class AbstractEntityPageWithException<T extends AbstractEntity> implements Serializable {

	private static final long serialVersionUID = 7013173615371005888L;

	@NotNull
	private Page<T> page;

	@NotNull
	private Exception exception;

	public AbstractEntityPageWithException() {
		super();
	}

	@Override
	public String toString() {
		final String pageString = page.toString();

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		final String result = StringUtils.getStringJoined("AbstractEntityPageWithException [page=", pageString, ", exception=", rootCauseMessage, "]");

		return result;
	}

	@Override
	public int hashCode() {
		return Objects.hash(page, exception);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractEntityPageWithException<T> other = (AbstractEntityPageWithException<T>) obj;
		return Objects.equals(page, other.page) && Objects.equals(exception, other.exception);
	}

}

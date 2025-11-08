package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import lombok.Data;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Data
public class AbstractEntityPageWithExceptionDTO<T extends AbstractEntity<T>> implements Serializable {
	private static final long serialVersionUID = 7013173615371005888L;

	@NotNull
	private final Page<T> page;

	private final Throwable throwable;

	public boolean hasException() {
		final boolean result = (throwable != null);
		return result;
	}

	@Override
	public String toString() {
		final String pageString = page.toString();
		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(throwable);

		final String result = StringUtils.getStringJoined("AbstractEntityPageWithExceptionDTO [page=", pageString, ", throwable=", rootCauseMessage, "]");
		return result;
	}

	@Override
	public int hashCode() {
		return Objects.hash(page, throwable);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}

		final AbstractEntityPageWithExceptionDTO<T> other = GenericsUtils.cast(obj);

		final boolean result = Objects.equals(page, other.page) && Objects.equals(throwable, other.throwable);
		return result;
	}
}

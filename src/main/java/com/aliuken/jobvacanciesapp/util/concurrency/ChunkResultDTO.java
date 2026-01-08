package com.aliuken.jobvacanciesapp.util.concurrency;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class ChunkResultDTO<C,R> implements Serializable {
	private static final long serialVersionUID = 4350878405637490133L;

	@NotEmpty(message="{chunk.notEmpty}")
	private final C chunk;

	private final R result;

	@Override
	public @NonNull String toString() {
		final String chunkString = String.valueOf(chunk);
		final String resultString = String.valueOf(result);

		final String result = StringUtils.getStringJoined("ChunkResultDTO [chunk=", chunkString, ", result=", resultString, "]");
		return result;
	}
}

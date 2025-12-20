package com.aliuken.jobvacanciesapp.util.concurrency;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.concurrent.Future;

@Data
public class ChunkFutureDTO<C,R> implements Serializable {
	private static final long serialVersionUID = 4350678405637490133L;

	private static final ChunkFutureDTO NO_ARGS_INSTANCE = new ChunkFutureDTO(null, null);

	@NotEmpty(message="{chunk.notEmpty}")
	private final C chunk;

	@NotNull(message="{future.notNull}")
	private final Future<R> future;

	public static ChunkFutureDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	@Override
	public String toString() {
		final String chunkString = String.valueOf(chunk);
		final String futureString = String.valueOf(future);

		final String result = StringUtils.getStringJoined("ChunkFutureDTO [chunk=", chunkString, ", future=", futureString, "]");
		return result;
	}
}

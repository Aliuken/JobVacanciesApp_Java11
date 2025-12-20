package com.aliuken.jobvacanciesapp.util.concurrency;

import com.aliuken.jobvacanciesapp.util.javase.FunctionalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class ConcurrencyUtils {

	// --------------------------------
	// METHODS WITH A NUMBER OF THREADS
	// --------------------------------

	public static <E> List<ChunkResultDTO<List<E>,Void>> splitAndRunChunksInParallel(final Collection<E> initialElements, final int chunkSize,
																					 final int numberOfThreads, final Consumer<List<E>> chunkConsumer) {
		if (initialElements == null || initialElements.isEmpty()) {
			return List.of();
		}

		if (numberOfThreads <= 0) {
			throw new IllegalArgumentException("The numberOfThreads must be greater than 0");
		}

		final Function<List<E>, Void> chunkFunction = FunctionalUtils.convertConsumerToFunction(chunkConsumer);
		final List<List<E>> chunkList = ConcurrencyUtils.createChunkList(initialElements, chunkSize);
		final List<ChunkResultDTO<List<E>,Void>> chunkResultDTOList = ConcurrencyUtils.runChunksInParallel(chunkList, numberOfThreads, chunkFunction);
		return chunkResultDTOList;
	}

	public static <E,R> List<ChunkResultDTO<List<E>,R>> splitAndRunChunksInParallel(final Collection<E> initialElements, final int chunkSize,
																					final int numberOfThreads, final Function<List<E>,R> chunkFunction) {
		if (initialElements == null || initialElements.isEmpty()) {
			return List.of();
		}

		if (numberOfThreads <= 0) {
			throw new IllegalArgumentException("The numberOfThreads must be greater than 0");
		}

		if (chunkFunction == null) {
			throw new IllegalArgumentException("The chunkFunction must not be null");
		}

		final List<List<E>> chunkList = ConcurrencyUtils.createChunkList(initialElements, chunkSize);
		final List<ChunkResultDTO<List<E>,R>> chunkResultDTOList = ConcurrencyUtils.runChunksInParallel(chunkList, numberOfThreads, chunkFunction);
		return chunkResultDTOList;
	}

	// -----------------------------------------------------------------------------------------------------------------

	// ----------------
	// REUSABLE METHODS
	// ----------------

	//Create a chunkList from the initialElements with chunkSize as the maximum chuck size
	private static <E> List<List<E>> createChunkList(final Collection<E> initialElements, final int chunkSize) {
		if (chunkSize <= 0) {
			throw new IllegalArgumentException("The chunkSize must be greater than 0");
		}

		final List<List<E>> chunkList = new ArrayList<>();
		List<E> chunk = new ArrayList<>(chunkSize);

		for (final E element : initialElements) {
			chunk.add(element);
			if (chunk.size() == chunkSize) {
				chunkList.add(chunk);
				chunk = new ArrayList<>(chunkSize);
			}
		}

		if (!chunk.isEmpty()) {
			chunkList.add(chunk);
		}

		return chunkList;
	}

	//Execute the chunkTask for every chunk in the chunkList (with the given numberOfThreads)
	private static <E,R> List<ChunkResultDTO<List<E>,R>> runChunksInParallel(final List<List<E>> chunkList, final int numberOfThreads,
																			 final Function<List<E>,R> chunkTask) {
		final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
		try {
			final List<ChunkFutureDTO<List<E>,R>> chunkFutureDTOList = new ArrayList<>(chunkList.size());
			for (final List<E> chunk : chunkList) {
				final Callable<R> callable = () -> chunkTask.apply(chunk);
				final Future<R> future = executorService.submit(callable);
				final ChunkFutureDTO chunkFutureDTO = new ChunkFutureDTO(chunk, future);
				chunkFutureDTOList.add(chunkFutureDTO);
			}
			final List<ChunkResultDTO<List<E>,R>> chunkResultDTOList = ConcurrencyUtils.runFutures(chunkFutureDTOList);
			return chunkResultDTOList;
		} finally {
			executorService.shutdown();
		}
	}

	private static <E,R> List<ChunkResultDTO<List<E>,R>> runFutures(final List<ChunkFutureDTO<List<E>,R>> chunkFutureDTOList) {
		final List<ChunkResultDTO<List<E>,R>> chunkResultDTOList;
		if (log.isDebugEnabled()) {
			chunkResultDTOList = ConcurrencyUtils.runFuturesWithLogging(chunkFutureDTOList);
		} else {
			chunkResultDTOList = ConcurrencyUtils.runFuturesWithoutLogging(chunkFutureDTOList);
		}
		return chunkResultDTOList;
	}

	private static <E,R> List<ChunkResultDTO<List<E>,R>> runFuturesWithLogging(final List<ChunkFutureDTO<List<E>,R>> chunkFutureDTOList) {
		final List<ChunkResultDTO<List<E>,R>> chunkResultDTOList = new ArrayList<>(chunkFutureDTOList.size());
		for(final ChunkFutureDTO<List<E>,R> chunkFutureDTO : chunkFutureDTOList) {
			final Future<R> future = chunkFutureDTO.getFuture();
			final R futureResult;
			try {
				futureResult = future.get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			final List<E> chunk = chunkFutureDTO.getChunk();
			final ChunkResultDTO<List<E>,R> chunkResultDTO = new ChunkResultDTO<>(chunk, futureResult);
			chunkResultDTOList.add(chunkResultDTO);

			final int futureIndex = chunkResultDTOList.size();
			final String futureIndexString = String.valueOf(futureIndex);
			final String chunkResultDTOString = String.valueOf(chunkResultDTO);
			log.debug(StringUtils.getStringJoined("Future ", futureIndexString, " executed with chunkResultDTO ", chunkResultDTOString));
		}
		return chunkResultDTOList;
	}

	private static <E,R> List<ChunkResultDTO<List<E>,R>> runFuturesWithoutLogging(final List<ChunkFutureDTO<List<E>,R>> chunkFutureDTOList) {
		final List<ChunkResultDTO<List<E>,R>> chunkResultDTOList = new ArrayList<>(chunkFutureDTOList.size());
		for(final ChunkFutureDTO<List<E>,R> chunkFutureDTO : chunkFutureDTOList) {
			final Future<R> future = chunkFutureDTO.getFuture();
			final R futureResult;
			try {
				futureResult = future.get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			final List<E> chunk = chunkFutureDTO.getChunk();
			final ChunkResultDTO<List<E>,R> chunkResultDTO = new ChunkResultDTO<>(chunk, futureResult);
			chunkResultDTOList.add(chunkResultDTO);
		}
		return chunkResultDTOList;
	}
}

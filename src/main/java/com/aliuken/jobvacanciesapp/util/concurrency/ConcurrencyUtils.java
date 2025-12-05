package com.aliuken.jobvacanciesapp.util.concurrency;

import com.aliuken.jobvacanciesapp.util.javase.FunctionalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class ConcurrencyUtils {

	public static <E> void splitAndRunChunksInParallel(final Collection<E> initialElements, final int chunkSize,
													   final int numberOfThreads, final Consumer<List<E>> chunkConsumer) {
		if (initialElements == null || initialElements.isEmpty()) {
			return;
		}

		if (numberOfThreads <= 0) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The numberOfThreads must be greater than 0"));
		}

		final Function<List<E>, Void> chunkFunction = FunctionalUtils.convertConsumerToFunction(chunkConsumer);
		final List<List<E>> chunkList = ConcurrencyUtils.createChunkList(initialElements, chunkSize);
		ConcurrencyUtils.runChunksInParallel(chunkList, numberOfThreads, chunkFunction);
	}

	public static <E,R> void splitAndRunChunksInParallel(final Collection<E> initialElements, final int chunkSize,
														 final int numberOfThreads, final Function<List<E>,R> chunkFunction) {
		if (initialElements == null || initialElements.isEmpty()) {
			return;
		}

		if (numberOfThreads <= 0) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The numberOfThreads must be greater than 0"));
		}

		if (chunkFunction == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The chunkFunction must not be null"));
		}

		final List<List<E>> chunkList = ConcurrencyUtils.createChunkList(initialElements, chunkSize);
		ConcurrencyUtils.runChunksInParallel(chunkList, numberOfThreads, chunkFunction);
	}

	//Create a chunkList from the initialElements with chunkSize as the maximum chuck size
	private static <E> List<List<E>> createChunkList(final Collection<E> initialElements, final int chunkSize) {
		if (chunkSize <= 0) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The chunkSize must be greater than 0"));
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
	private static <E,R> void runChunksInParallel(final List<List<E>> chunkList, final int numberOfThreads,
												  final Function<List<E>,R> chunkTask) {
		final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
		try {
			final LinkedHashMap<Future<R>, List<E>> futureToChunkMap = new LinkedHashMap<>(chunkList.size());
			for (final List<E> chunk : chunkList) {
				final Callable<R> callable = () -> chunkTask.apply(chunk);
				final Future<R> future = executorService.submit(callable);
				futureToChunkMap.put(future, chunk);
			}
			if (log.isDebugEnabled()) {
				ConcurrencyUtils.runFuturesWithLogging(futureToChunkMap);
			} else {
				ConcurrencyUtils.runFuturesWithoutLogging(futureToChunkMap);
			}
		} finally {
			executorService.shutdown();
		}
	}

	private static <E,R> void runFuturesWithLogging(final LinkedHashMap<Future<R>, List<E>> futureToChunkMap) {
		int futureIndex = 1;
		for (final Map.Entry<Future<R>, List<E>> entry : futureToChunkMap.entrySet()) {
			final Future<R> future = entry.getKey();
			final R futureResult;
			try {
				futureResult = future.get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			final String futureIndexString = String.valueOf(futureIndex);
			final String futureResultString = String.valueOf(futureResult);
			final List<E> chunk = entry.getValue();
			final String chunkString = String.valueOf(chunk);
			log.debug(StringUtils.getStringJoined("Future ", futureIndexString, " executed with result ", futureResultString, " for the chunk ", chunkString));
			futureIndex++;
		}
	}

	private static <E,R> void runFuturesWithoutLogging(final LinkedHashMap<Future<R>, List<E>> futureToChunkMap) {
		for (final Map.Entry<Future<R>, List<E>> entry : futureToChunkMap.entrySet()) {
			final Future<R> future = entry.getKey();
			try {
				future.get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}

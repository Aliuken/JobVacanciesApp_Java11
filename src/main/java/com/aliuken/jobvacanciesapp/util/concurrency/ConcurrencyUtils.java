package com.aliuken.jobvacanciesapp.util.concurrency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class ConcurrencyUtils {

	public static <E> void forEachChunkParallel(final Collection<E> initialElements, final int chunkSize,
                                                final int numberOfThreads, final Consumer<List<E>> chunkTask) {

		final List<List<E>> chunkList = ConcurrencyUtils.createChunkList(initialElements, chunkSize);
        ConcurrencyUtils.forEachChunkParallel(chunkList, numberOfThreads, chunkTask);
	}

    //Create a chunkList from the initialElements with chunkSize as the maximum chuck size
	public static <E> List<List<E>> createChunkList(final Collection<E> initialElements, final int chunkSize) {
		if (initialElements == null || initialElements.isEmpty()) {
			return Collections.emptyList();
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
    public static <T> void forEachChunkParallel(final List<List<T>> chunkList, final int numberOfThreads,
                                                final Consumer<List<T>> chunkTask) {
        if (chunkList.isEmpty()) {
            return;
        }

        final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        try {
            final ArrayList<Future<Void>> futures = new ArrayList<>(chunkList.size());
            for (final List<T> chunk : chunkList) {
                final Callable<Void> callable = () -> {
                    chunkTask.accept(chunk);
                    return null;
                };
                final Future<Void> future = executorService.submit(callable);
                futures.add(future);
            }
            for (final Future<Void> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            executorService.shutdown();
        }
    }
}

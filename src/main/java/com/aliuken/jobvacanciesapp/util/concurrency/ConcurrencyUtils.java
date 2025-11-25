package com.aliuken.jobvacanciesapp.util.concurrency;

import com.aliuken.jobvacanciesapp.util.javase.FunctionalUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConcurrencyUtils {

    public static <E> void splitAndRunChunksInParallel(final Collection<E> initialElements, final int chunkSize,
                                                       final int numberOfThreads, final Consumer<List<E>> chunkTask) {

        final Function<List<E>, Void> chunkFunction = FunctionalUtils.convertConsumerToFunction(chunkTask);
        ConcurrencyUtils.splitAndRunChunksInParallel(initialElements, chunkSize, numberOfThreads, chunkFunction);
    }

    public static <E,R> void splitAndRunChunksInParallel(final Collection<E> initialElements, final int chunkSize,
                                                         final int numberOfThreads, final Function<List<E>,R> chunkFunction) {

        final List<List<E>> chunkList = ConcurrencyUtils.createChunkList(initialElements, chunkSize);
        ConcurrencyUtils.runChunksInParallel(chunkList, numberOfThreads, chunkFunction);
    }

    //Create a chunkList from the initialElements with chunkSize as the maximum chuck size
    private static <E> List<List<E>> createChunkList(final Collection<E> initialElements, final int chunkSize) {
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
    private static <E,R> void runChunksInParallel(final List<List<E>> chunkList, final int numberOfThreads,
                                                  final Function<List<E>,R> chunkTask) {
        if (chunkList.isEmpty()) {
            return;
        }

        final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        try {
            final ArrayList<Future<R>> futures = new ArrayList<>(chunkList.size());
            for (final List<E> chunk : chunkList) {
                final Callable<R> callable = () -> chunkTask.apply(chunk);
                final Future<R> future = executorService.submit(callable);
                futures.add(future);
            }
            for (final Future<R> future : futures) {
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

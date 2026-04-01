package com.aliuken.jobvacanciesapp.util.javase.stream.dto;

import com.aliuken.jobvacanciesapp.util.javase.stream.StreamUtilsImpl;
import com.aliuken.jobvacanciesapp.util.javase.stream.superclass.StreamStrategy;
import com.aliuken.jobvacanciesapp.util.javase.stream.superinterface.StreamUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

@Data
public class StreamUtilsPair<T> {
    private final StreamUtils<T> sequential;
    private final StreamUtils<T> parallel;

    public StreamUtilsPair(StreamUtils<T> sequential, StreamUtils<T> parallel) {
        this.sequential = sequential;
        this.parallel = parallel;
    }

    public StreamUtils<T> get(boolean isParallel) {
        return isParallel ? parallel : sequential;
    }
}
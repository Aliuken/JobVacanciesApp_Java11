package com.aliuken.jobvacanciesapp.util.javase.stream;

import com.aliuken.jobvacanciesapp.util.javase.stream.superclass.StreamStrategy;
import org.jspecify.annotations.NonNull;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class StreamStaticUtils {
    public static <K, V> @NonNull Stream<Map.Entry<K, V>> ofNullableMap(Map<K, V> map, boolean isParallel) {
        final Stream<Map.Entry<K, V>> elementStream;
        if(map != null) {
            final StreamStrategy streamStrategy = StreamStrategy.getInstance(isParallel);
            final Set<Map.Entry<K, V>> mapEntrySet = map.entrySet();
            elementStream = streamStrategy.build(mapEntrySet);
        } else {
            elementStream = Stream.empty();
        }
        return elementStream;
    }

    public static <E extends Enum<E>> @NonNull Stream<E> ofEnum(Class<E> enumClass, boolean isParallel) {
        final Stream<E> elementStream;
        if(enumClass != null) {
            final StreamStrategy streamStrategy = StreamStrategy.getInstance(isParallel);
            final EnumSet<E> enumSet = EnumSet.allOf(enumClass);
            elementStream = streamStrategy.build(enumSet);
        } else {
            elementStream = Stream.empty();
        }
        return elementStream;
    }
}

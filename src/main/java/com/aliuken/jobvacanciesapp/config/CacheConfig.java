package com.aliuken.jobvacanciesapp.config;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig {
	public static final @NonNull String ENTITY_MANAGER_CACHE_NAME = "entityManagerCache";
	public static final @NonNull String ENTITY_MANAGER_CACHE_ALTERNATIVE = "jpaContext";

	@Bean
	@NonNull CacheManager cacheManager() {
		final CacheManager cacheManager = new ConcurrentMapCacheManager("entityManagerCache");
		return cacheManager;
	}

	@Bean
	@NonNull CacheManagerCustomizer<ConcurrentMapCacheManager> simpleCacheCustomizer() {
		final CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer = new CacheManagerCustomizer<>() {
			@Override
			public void customize(final ConcurrentMapCacheManager concurrentMapCacheManager) {
				final List<String> cacheNames = List.of("entityManagerCache");
				concurrentMapCacheManager.setCacheNames(cacheNames);
			}
		};
		return cacheManagerCustomizer;
	}
}

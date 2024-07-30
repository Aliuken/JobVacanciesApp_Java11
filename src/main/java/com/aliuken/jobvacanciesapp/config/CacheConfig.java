package com.aliuken.jobvacanciesapp.config;

import java.util.List;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public CacheManager cacheManager() {
		final CacheManager cacheManager = new ConcurrentMapCacheManager("entityManagerCache");
		return cacheManager;
	}

	@Bean
	public CacheManagerCustomizer<ConcurrentMapCacheManager> simpleCacheCustomizer() {
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

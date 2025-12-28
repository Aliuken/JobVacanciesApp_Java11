package com.aliuken.jobvacanciesapp;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.support.GenericApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@SpringBootApplication
public class MainClass {
	private static volatile String[] args;
	private static volatile SpringApplication springApplication;
	private static volatile GenericApplicationContext applicationContext;
	private static volatile @NonNull ExecutorService restartExecutorService = Executors.newFixedThreadPool(3, new ThreadFactory() {
		@Override
		public @NonNull Thread newThread(final @NonNull Runnable runnable) {
			final Thread restartThread = Executors.defaultThreadFactory().newThread(runnable);
			restartThread.setDaemon(false);
			return restartThread;
		}
	});

	public static void main(final String[] args) {
		MainClass.args = args;
		MainClass.springApplication = new SpringApplicationBuilder(MainClass.class).application();
		MainClass.applicationContext = (GenericApplicationContext) MainClass.springApplication.run(args);
	}

	public static void restartApp(final String nextDefaultLanguageCode, final String nextDefaultAnonymousAccessPermissionValue,
								  final String nextDefaultInitialTableSortingDirectionCode, final String nextDefaultInitialTablePageSizeValue,
								  final String nextDefaultColorModeCode, final String nextDefaultUserInterfaceFrameworkCode, final String nextDefaultPdfDocumentPageFormatCode) {
		if(nextDefaultLanguageCode != null || nextDefaultAnonymousAccessPermissionValue != null ||
				nextDefaultInitialTableSortingDirectionCode != null || nextDefaultInitialTablePageSizeValue != null ||
				nextDefaultColorModeCode != null || nextDefaultUserInterfaceFrameworkCode != null || nextDefaultPdfDocumentPageFormatCode != null) {
			final Map<String, Object> additionalPropertiesMap = new HashMap<>();
			if(nextDefaultLanguageCode != null) {
				additionalPropertiesMap.put("jobvacanciesapp.defaultLanguageCodeOverwritten", nextDefaultLanguageCode);
			}
			if(nextDefaultAnonymousAccessPermissionValue != null) {
				additionalPropertiesMap.put("jobvacanciesapp.defaultAnonymousAccessPermissionValueOverwritten", nextDefaultAnonymousAccessPermissionValue);
			}
			if(nextDefaultInitialTableSortingDirectionCode != null) {
				additionalPropertiesMap.put("jobvacanciesapp.defaultInitialTableSortingDirectionCodeOverwritten", nextDefaultInitialTableSortingDirectionCode);
			}
			if(nextDefaultInitialTablePageSizeValue != null) {
				additionalPropertiesMap.put("jobvacanciesapp.defaultInitialTablePageSizeValueOverwritten", nextDefaultInitialTablePageSizeValue);
			}
			if(nextDefaultColorModeCode != null) {
				additionalPropertiesMap.put("jobvacanciesapp.defaultColorModeCodeOverwritten", nextDefaultColorModeCode);
			}
			if(nextDefaultUserInterfaceFrameworkCode != null) {
				additionalPropertiesMap.put("jobvacanciesapp.defaultUserInterfaceFrameworkCodeOverwritten", nextDefaultUserInterfaceFrameworkCode);
			}
			if(nextDefaultPdfDocumentPageFormatCode != null) {
				additionalPropertiesMap.put("jobvacanciesapp.defaultPdfDocumentPageFormatCodeOverwritten", nextDefaultPdfDocumentPageFormatCode);
			}

			MainClass.restartExecutorService.submit(() -> {
				MainClass.applicationContext.close();
				MainClass.springApplication = new SpringApplicationBuilder(MainClass.class).properties(additionalPropertiesMap).application();
				MainClass.applicationContext = (GenericApplicationContext) MainClass.springApplication.run(MainClass.args);
			});
		}
	}
}
package com.aliuken.jobvacanciesapp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootApplication
public class MainApp {
	private static volatile String[] args;
	private static volatile SpringApplication springApplication;
	private static volatile GenericApplicationContext applicationContext;
	private static volatile ExecutorService restartExecutorService = Executors.newFixedThreadPool(3, new ThreadFactory() {
		@Override
		public Thread newThread(Runnable runnable) {
			Thread restartThread = Executors.defaultThreadFactory().newThread(runnable);
			restartThread.setDaemon(false);
			return restartThread;
		}
	});

	public static void main(String[] args) {
		MainApp.args = args;
		MainApp.springApplication = new SpringApplicationBuilder(MainApp.class).application();
		MainApp.applicationContext = (GenericApplicationContext) MainApp.springApplication.run(args);
	}

	public static void restartApp(final String nextDefaultLanguageCode, final String nextAnonymousAccessPermissionName, final String nextDefaultInitialTablePageSizeValue, final String nextDefaultColorModeCode, final String nextUserInterfaceFrameworkCode) {
		if(nextDefaultLanguageCode != null || nextAnonymousAccessPermissionName != null || nextDefaultInitialTablePageSizeValue != null || nextDefaultColorModeCode != null || nextUserInterfaceFrameworkCode != null) {
			final Map<String, Object> additionalPropertiesMap = new HashMap<>();
			if(nextDefaultLanguageCode != null) {
				additionalPropertiesMap.put("jobvacanciesapp.defaultLanguageCodeOverwritten", nextDefaultLanguageCode);
			}
			if(nextAnonymousAccessPermissionName != null) {
				additionalPropertiesMap.put("jobvacanciesapp.anonymousAccessPermissionNameOverwritten", nextAnonymousAccessPermissionName);
			}
			if(nextDefaultInitialTablePageSizeValue != null) {
				additionalPropertiesMap.put("jobvacanciesapp.defaultInitialTablePageSizeValueOverwritten", nextDefaultInitialTablePageSizeValue);
			}
			if(nextDefaultColorModeCode != null) {
				additionalPropertiesMap.put("jobvacanciesapp.defaultColorModeCodeOverwritten", nextDefaultColorModeCode);
			}
			if(nextUserInterfaceFrameworkCode != null) {
				additionalPropertiesMap.put("jobvacanciesapp.userInterfaceFrameworkCodeOverwritten", nextUserInterfaceFrameworkCode);
			}

			MainApp.restartExecutorService.submit(() -> {
				MainApp.applicationContext.close();
				MainApp.springApplication = new SpringApplicationBuilder(MainApp.class).properties(additionalPropertiesMap).application();
				MainApp.applicationContext = (GenericApplicationContext) MainApp.springApplication.run(MainApp.args);
			});
		}
	}

}
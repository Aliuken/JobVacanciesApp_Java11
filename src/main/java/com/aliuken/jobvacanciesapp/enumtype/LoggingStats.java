package com.aliuken.jobvacanciesapp.enumtype;

import lombok.Getter;

import java.io.Serializable;

public enum LoggingStats implements Serializable {
	HTTP_METHOD    ("stats.http_method"),
	MAPPING_PATH   ("stats.mapping_path"),
	INFORMED_PATH  ("stats.informed_path"),
	OPERATION      ("stats.operation"),
	THREAD_ID      ("stats.thread_id"),
	DB_TIME        ("stats.db_time"),
	NOT_CACHED_TIME("stats.get_entity_manager_not_cached_time"),
	OTHER_TIME     ("stats.other_time"),
	TOTAL_TIME     ("stats.total_time");

	@Getter
	private final String key;

	private LoggingStats(final String key) {
		this.key = key;
	}
}

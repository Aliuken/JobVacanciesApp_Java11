package com.aliuken.jobvacanciesapp.controller.superclass;

import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

public abstract class AbstractEntityControllerWithoutPredefinedFilter<T extends AbstractEntity<T>> extends AbstractEntityControllerWithTable<T> {
	protected abstract String getPaginationUrl();

	protected String getExportToPdfUrl() {
		final String paginationUrl = this.getPaginationUrl();
		final String exportToPdfUrl = StringUtils.getStringJoined(paginationUrl, "/exportToPdf");
		return exportToPdfUrl;
	}
}

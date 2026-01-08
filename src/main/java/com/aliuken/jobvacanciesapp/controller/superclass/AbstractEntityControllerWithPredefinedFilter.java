package com.aliuken.jobvacanciesapp.controller.superclass;

import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

public abstract class AbstractEntityControllerWithPredefinedFilter<T extends AbstractEntity<T>> extends AbstractEntityControllerWithTable<T> {
	protected String getPaginationUrl(final long predefinedFilterId) {
		final String predefinedFilterIdString = String.valueOf(predefinedFilterId);
		final String paginationUrl = this.getPaginationUrl(predefinedFilterIdString);
		return paginationUrl;
	}

	protected String getExportToPdfUrl(final long predefinedFilterId) {
		final String predefinedFilterIdString = String.valueOf(predefinedFilterId);
		final String exportToPdfUrl = this.getExportToPdfUrl(predefinedFilterIdString);
		return exportToPdfUrl;
	}

	protected abstract String getPaginationUrl(final String predefinedFilterIdString);

	protected String getExportToPdfUrl(final String predefinedFilterIdString) {
		final String paginationUrl = this.getPaginationUrl(predefinedFilterIdString);
		final String exportToPdfUrl = StringUtils.getStringJoined(paginationUrl, "/exportToPdf");
		return exportToPdfUrl;
	}
}

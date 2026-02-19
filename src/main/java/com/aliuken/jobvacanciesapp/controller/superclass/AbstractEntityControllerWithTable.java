package com.aliuken.jobvacanciesapp.controller.superclass;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.controller.SessionAuthUserEntityQueryController;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;

public abstract class AbstractEntityControllerWithTable<T extends AbstractEntity<T>> extends AbstractEntityController<T> {
	protected static final String EXPORT_TO_PDF_DISABLED_VALUE = Constants.EMPTY_STRING;

	protected boolean hasExportToPdfEnabled(final TableSearchDTO tableSearchDTO) {
		final boolean result;
		if(this instanceof SessionAuthUserEntityQueryController) {
			result = false;
		} else {
			result = tableSearchDTO != null && tableSearchDTO.hasAllParameters();
		}
		return result;
	}
}

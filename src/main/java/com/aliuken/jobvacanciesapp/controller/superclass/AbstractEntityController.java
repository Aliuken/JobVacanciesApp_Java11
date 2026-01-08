package com.aliuken.jobvacanciesapp.controller.superclass;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.dto.PredefinedFilterDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.persistence.file.FileUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractEntityController<T extends AbstractEntity<T>> {
	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	private static String authUserEntityQueryFilesPath;

	@PostConstruct
	private void postConstruct() {
		authUserEntityQueryFilesPath = configPropertiesBean.getAuthUserEntityQueryFilesPath();
	}

	protected byte[] storeAndDownloadPdf(final PredefinedFilterDTO predefinedFilterDTO, final @NonNull TableSearchDTO tableSearchDTO,
			final Model model, final @NonNull PageEntityEnum pageEntity,
			final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {

		final Object pageEntityModelAttribute;
		if(model != null) {
			final String pageEntityValue = pageEntity.getValue();
			if(pageEntityValue != null) {
				pageEntityModelAttribute = model.getAttribute(pageEntityValue);
			} else {
				pageEntityModelAttribute = null;
			}
		} else {
			pageEntityModelAttribute = null;
		}

		final byte[] pdfByteArray;
		if(pageEntityModelAttribute != null) {
			final Page<T> entityPage = GenericsUtils.cast(pageEntityModelAttribute);

			pdfByteArray = FileUtils.storeAndDownloadPdf(predefinedFilterDTO, tableSearchDTO, pageEntity, entityPage,
					authUserEntityQueryFilesPath, httpServletRequest, httpServletResponse);
		} else {
			pdfByteArray = new byte[0];
		}

		return pdfByteArray;
	}
}

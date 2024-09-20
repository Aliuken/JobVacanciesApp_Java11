package com.aliuken.jobvacanciesapp.controller.superclass;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.persistence.file.FileUtils;

public abstract class AbstractEntityController<T extends AbstractEntity> {
	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	private static String authUserEntityQueryFilesPath;

	@PostConstruct
	private void postConstruct() {
		authUserEntityQueryFilesPath = configPropertiesBean.getAuthUserEntityQueryFilesPath();
	}

	protected byte[] storeAndDownloadPdf(final TableSearchDTO tableSearchDTO, final Model model, final PageEntityEnum pageEntity,
			final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {

		final Object pageEntityModelAttribute;
		if(model != null && pageEntity != null) {
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

			pdfByteArray = FileUtils.storeAndDownloadPdf(tableSearchDTO, pageEntity, entityPage,
					authUserEntityQueryFilesPath, httpServletRequest, httpServletResponse);
		} else {
			pdfByteArray = new byte[0];
		}

		return pdfByteArray;
	}
}

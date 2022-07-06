package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

public enum FileType implements Serializable {
	CURRICULUM("pdf", "doc", "docx"),
	JOB_VACANCY_LOGO("jpg", "jpeg", "png");

	@NotNull
	private final List<String> allowedExtensions;

	private FileType(@NotNull final String... allowedExtensionsVararg) {
		if(allowedExtensionsVararg == null || allowedExtensionsVararg.length == 0) {
			throw new IllegalArgumentException("FileType allowedExtensions must not be null nor empty");
		}

		this.allowedExtensions = new ArrayList<>();
		for(String allowedExtension : allowedExtensionsVararg) {
			if(allowedExtension != null)  {
				this.allowedExtensions.add(allowedExtension.toLowerCase());
			}
		}
	}

	public List<String> getAllowedExtensions() {
		return allowedExtensions;
	}
}

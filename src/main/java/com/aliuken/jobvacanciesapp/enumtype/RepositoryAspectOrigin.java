package com.aliuken.jobvacanciesapp.enumtype;

import java.io.Serializable;

public enum RepositoryAspectOrigin implements Serializable {
	SPECIFIC_JPA_REPO,
	UPGRADED_JPA_REPO,
	LAZY_JPA_RELATION;
}
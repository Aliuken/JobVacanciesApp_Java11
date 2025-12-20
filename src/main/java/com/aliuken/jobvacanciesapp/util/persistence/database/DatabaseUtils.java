package com.aliuken.jobvacanciesapp.util.persistence.database;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public class DatabaseUtils {

	private DatabaseUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	public static ExampleMatcher getExampleMatcherWithExactOneField(final String tableFieldPath) {
		final GenericPropertyMatcher exactGenericPropertyMatcher = ExampleMatcher.GenericPropertyMatchers.exact();

		final ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher(tableFieldPath, exactGenericPropertyMatcher);
		return exampleMatcher;
	}

	public static ExampleMatcher getExampleMatcherWithContainsOneField(final String tableFieldPath) {
		final GenericPropertyMatcher containsGenericPropertyMatcher = ExampleMatcher.GenericPropertyMatchers.contains();

		final ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher(tableFieldPath, containsGenericPropertyMatcher);
		return exampleMatcher;
	}

	public static ExampleMatcher getExampleMatcherWithExactTwoFields(final String tableFieldPath1, final String tableFieldPath2) {
		final GenericPropertyMatcher exactGenericPropertyMatcher = ExampleMatcher.GenericPropertyMatchers.exact();

		final ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher(tableFieldPath1, exactGenericPropertyMatcher)
				.withMatcher(tableFieldPath2, exactGenericPropertyMatcher);
		return exampleMatcher;
	}

	public static ExampleMatcher getExampleMatcherWithContainsTwoFields(final String tableFieldPath1, final String tableFieldPath2) {
		final GenericPropertyMatcher exactGenericPropertyMatcher = ExampleMatcher.GenericPropertyMatchers.exact();
		final GenericPropertyMatcher containsGenericPropertyMatcher = ExampleMatcher.GenericPropertyMatchers.contains();

		final ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher(tableFieldPath1, exactGenericPropertyMatcher)
				.withMatcher(tableFieldPath2, containsGenericPropertyMatcher);
		return exampleMatcher;
	}

	public static Predicate getEqualsDateTimePredicate(final String dateTimeString, final String dateTimeFieldName, final Root<?> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
		final Predicate predicate;
		if(dateTimeString == null || Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD.equals(dateTimeString)) {
			final Expression<LocalDateTime> localDateTimeExpression = root.get(dateTimeFieldName).as(LocalDateTime.class);
			predicate = criteriaBuilder.isNull(localDateTimeExpression);
		} else {
			final Expression<LocalDateTime> localDateTimeExpression = root.get(dateTimeFieldName).as(LocalDateTime.class);
			final Expression<String> dateTimeFormatExpression = criteriaBuilder.literal("%d-%m-%Y %H:%i:%s");
			final Expression<String> finalStringExpression = criteriaBuilder.function("DATE_FORMAT", String.class, localDateTimeExpression, dateTimeFormatExpression);
			predicate = criteriaBuilder.like(finalStringExpression, dateTimeString + "%");
		}
		return predicate;
	}

	public static Predicate getEqualsEntityIdAndDateTimePredicate(final Long entityId, final String entityFieldName, final String dateTimeString, final String dateTimeFieldName, final Root<?> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
		final Predicate predicate;
		if(dateTimeString == null || Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD.equals(dateTimeString)) {
			final Expression<Long> jobCompanyIdExpression = root.get(entityFieldName).get("id").as(Long.class);
			final Predicate predicate1 = criteriaBuilder.equal(jobCompanyIdExpression, entityId);

			final Expression<LocalDateTime> localDateTimeExpression = root.get(dateTimeFieldName).as(LocalDateTime.class);
			final Predicate predicate2 = criteriaBuilder.isNull(localDateTimeExpression);

			predicate = criteriaBuilder.and(predicate1, predicate2);
		} else {
			final Expression<Long> jobCompanyIdExpression = root.get(entityFieldName).get("id").as(Long.class);
			final Predicate predicate1 = criteriaBuilder.equal(jobCompanyIdExpression, entityId);

			final Expression<LocalDateTime> localDateTimeExpression = root.get(dateTimeFieldName).as(LocalDateTime.class);
			final Expression<String> dateTimeFormatExpression = criteriaBuilder.literal("%d-%m-%Y %H:%i:%s");
			final Expression<String> finalStringExpression = criteriaBuilder.function("DATE_FORMAT", String.class, localDateTimeExpression, dateTimeFormatExpression);
			final Predicate predicate2 = criteriaBuilder.like(finalStringExpression, dateTimeString + "%");

			predicate = criteriaBuilder.and(predicate1, predicate2);
		}
		return predicate;
	}
}

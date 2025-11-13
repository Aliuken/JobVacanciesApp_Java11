package com.aliuken.jobvacanciesapp.model.comparator.superclass;

import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class AbstractEntityDefaultComparator<T extends AbstractEntity<T>> extends AbstractEntityComparator<T> {
	private static final Map<Class<?>, EntityComparators<?>> ENTITY_COMPARATORS_MAP = new ConcurrentHashMap<>();

	@Getter
	public static class EntityComparators<U extends AbstractEntity<U>> {
		private final AbstractEntityDefaultComparator<U> ascComparator;
		private final AbstractEntityDefaultComparator<U> descComparator;
		private final BiFunction<U, U, Integer> compareToAscFunction;
		private final BiFunction<U, U, Integer> compareToDescFunction;

		private EntityComparators() {
			this.ascComparator = new AbstractEntityDefaultComparator<>(false);
			this.descComparator = new AbstractEntityDefaultComparator<>(true);
			this.compareToAscFunction = (entity1, entity2) -> ascComparator.compare(entity1, entity2);
			this.compareToDescFunction = (entity1, entity2) -> descComparator.compare(entity1, entity2);
		}
	}

	private final int direction;

	private AbstractEntityDefaultComparator(final boolean isDescendingOrder) {
		direction = isDescendingOrder ? -1 : 1;
	}

	/**
	 * Defines an ordering among entities.
	 * <p>
	 * Entities are ordered by:
	 * <ul>
	 *   <li>Class name (lexicographically ascending)</li>
	 *   <li>ID value (ascending)</li>
	 * </ul>
	 */
	@Override
	public final int compare(final T entity1, final T entity2) {
		final Integer nullCompareResult = this.getNullCompareResult(entity1, entity2);
		if(nullCompareResult != null) {
			return direction * nullCompareResult;
		}

		final Integer classCompareResult = this.getClassCompareResult(entity1, entity2);
		if(classCompareResult != null) {
			return direction * classCompareResult;
		}

		final int idCompareResult = this.getIdCompareResult(entity1, entity2);
		return direction * idCompareResult;
	}

	public static <U extends AbstractEntity<U>> EntityComparators<U> getEntityComparators(Class<U> entityClass) {
		EntityComparators<U> entityComparators = GenericsUtils.cast(ENTITY_COMPARATORS_MAP.get(entityClass));
		if (entityComparators == null) {
			entityComparators = new EntityComparators<>();
			ENTITY_COMPARATORS_MAP.put(entityClass, entityComparators);
		}
		return entityComparators;
	}
}
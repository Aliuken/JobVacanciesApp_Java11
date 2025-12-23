package com.aliuken.jobvacanciesapp.model.comparator.superclass;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractEntityDefaultComparator<T extends AbstractEntity<T>> extends AbstractEntityComparator<T> {
	private static final Map<Class<?>, EntityComparators<?>> ENTITY_COMPARATORS_MAP = new ConcurrentHashMap<>();

	@Getter
	public static class EntityComparators<U extends AbstractEntity<U>> {
		private final AbstractEntityDefaultComparator<U> ascComparator;
		private final AbstractEntityDefaultComparator<U> descComparator;

		private EntityComparators() {
			this.ascComparator = new AbstractEntityDefaultComparator<>(false);
			this.descComparator = new AbstractEntityDefaultComparator<>(true);
		}

		public AbstractEntityDefaultComparator<U> getCurrentDefaultComparator() {
			final TableSortingDirection currentDefaultTableSortingDirection = Constants.ENUM_UTILS.getCurrentDefaultEnumElement(TableSortingDirection.class);

			final AbstractEntityDefaultComparator<U> currentDefaultComparator;
			if(TableSortingDirection.DESC.equals(currentDefaultTableSortingDirection)) {
				currentDefaultComparator = descComparator;
			} else {
				currentDefaultComparator = ascComparator;
			}
			return currentDefaultComparator;
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
	 *   <li>Class name</li>
	 *   <li>ID value</li>
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

	public static <U extends AbstractEntity<U>> EntityComparators<U> getEntityComparators(final Class<U> entityClass) {
		EntityComparators<U> entityComparators = GenericsUtils.cast(ENTITY_COMPARATORS_MAP.get(entityClass));
		if(entityComparators == null) {
			entityComparators = new EntityComparators<>();
			ENTITY_COMPARATORS_MAP.put(entityClass, entityComparators);
		}
		return entityComparators;
	}
}
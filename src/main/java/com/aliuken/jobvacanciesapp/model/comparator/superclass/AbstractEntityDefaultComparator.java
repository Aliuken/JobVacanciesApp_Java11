package com.aliuken.jobvacanciesapp.model.comparator.superclass;

import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;

import java.util.Comparator;

public class AbstractEntityDefaultComparator<T extends AbstractEntity<T>> implements Comparator<AbstractEntity<T>> {
	public static final Integer ENTITIES_EQUAL = 0;
	public static final Integer ENTITY1_FIRST = -1;
	public static final Integer ENTITY2_FIRST = 1;

	/**
	 * Defines an ordering among entities.
	 * <p>
	 * Entities are ordered by:
	 * <ul>
	 *   <li>Class name (lexicographically)</li>
	 *   <li>ID value (ascending)</li>
	 * </ul>
	 */
	@Override
	public final int compare(final AbstractEntity<T> abstractEntity1, final AbstractEntity<T> abstractEntity2) {
		final T entity1 = GenericsUtils.cast(abstractEntity1);
		final T entity2 = GenericsUtils.cast(abstractEntity2);

		final Integer nullCompareResult = this.getNullCompareResult(entity1, entity2);
		if(nullCompareResult != null) {
			return nullCompareResult;
		}

		final Integer classCompareResult = this.getClassCompareResult(entity1, entity2);
		if(classCompareResult != null) {
			return classCompareResult;
		}

		final int idCompareResult = this.getIdCompareResult(entity1, entity2);
		return idCompareResult;
	}

	// Ascending order where null entities are sorted last.
	private final Integer getNullCompareResult(final T entity1, final T entity2) {
		final Integer nullCompareResult;
		if (entity1 == null && entity2 == null) {
			nullCompareResult = ENTITIES_EQUAL;
		} else if (entity1 == null) {
			nullCompareResult = ENTITY2_FIRST;
		} else if (entity2 == null) {
			nullCompareResult = ENTITY1_FIRST;
		} else {
			nullCompareResult = null;
		}
		return nullCompareResult;
	}

	// Ascending order where different classes are sorted by their names (including packages).
	private final Integer getClassCompareResult(final T entity1, final T entity2) {
		final Class<?> abstractEntityClass1 = entity1.getClass();
		final Class<?> abstractEntityClass2 = entity2.getClass();

		final Integer classCompareResult;
		if (abstractEntityClass1 != abstractEntityClass2) {
			classCompareResult = abstractEntityClass1.getName().compareTo(abstractEntityClass2.getName());
		} else {
			classCompareResult = null;
		}
		return classCompareResult;
	}

	// Ascending order where entities with null ids are sorted last.
	private final int getIdCompareResult(final T entity1, final T entity2) {
		final Long abstractEntityId1 = entity1.getId();
		final Long abstractEntityId2 = entity2.getId();

		final int idCompareResult;
		if (abstractEntityId1 == null && abstractEntityId2 == null) {
			idCompareResult = ENTITIES_EQUAL;
		} else if (abstractEntityId1 == null) {
			idCompareResult = ENTITY2_FIRST;
		} else if (abstractEntityId2 == null) {
			idCompareResult = ENTITY1_FIRST;
		} else {
			idCompareResult = Long.compare(abstractEntityId1, abstractEntityId2);
		}
		return idCompareResult;
	}
}
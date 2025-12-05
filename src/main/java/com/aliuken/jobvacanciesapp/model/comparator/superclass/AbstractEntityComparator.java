package com.aliuken.jobvacanciesapp.model.comparator.superclass;

import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;

import java.util.Comparator;

public abstract class AbstractEntityComparator<T extends AbstractEntity<T>> implements Comparator<T> {
	public static final int ENTITIES_EQUAL = 0;
	public static final int ENTITY1_FIRST = -1;
	public static final int ENTITY2_FIRST = 1;

	// Ascending order where null entities are sorted last.
	protected final Integer getNullCompareResult(final T entity1, final T entity2) {
		final Integer nullCompareResult;
		if (entity1 == null && entity2 == null) {
			nullCompareResult = ENTITIES_EQUAL;
		} else if (entity1 == null) {
			nullCompareResult = ENTITY2_FIRST;
		} else if (entity2 == null) {
			nullCompareResult = ENTITY1_FIRST;
		} else {
			nullCompareResult = null; //CONTINUE
		}
		return nullCompareResult;
	}

	// Ascending order where different classes are sorted by their names (including packages).
	protected final Integer getClassCompareResult(final T entity1, final T entity2) {
		final Class<?> abstractEntityClass1 = entity1.getClass();
		final Class<?> abstractEntityClass2 = entity2.getClass();

		final Integer classCompareResult;
		if (!abstractEntityClass1.equals(abstractEntityClass2)) {
			classCompareResult = abstractEntityClass1.getName().compareTo(abstractEntityClass2.getName());
		} else {
			classCompareResult = null; //CONTINUE
		}
		return classCompareResult;
	}

	// Ascending order where entities are sorted by their ids (where null ids are sorted last).
	protected final int getIdCompareResult(final T entity1, final T entity2) {
		final Long abstractEntityId1 = entity1.getId();
		final Long abstractEntityId2 = entity2.getId();

		final int idCompareResult = AbstractEntityComparator.getCompareResult(abstractEntityId1, abstractEntityId2);
		return idCompareResult;
	}

	// Utility method to compare two comparable objects
	protected static <U extends Comparable<U>> int getCompareResult(final U object1, final U object2) {
		final int idCompareResult;
		if (object1 == null && object2 == null) {
			idCompareResult = ENTITIES_EQUAL;
		} else if (object1 == null) {
			idCompareResult = ENTITY2_FIRST;
		} else if (object2 == null) {
			idCompareResult = ENTITY1_FIRST;
		} else {
			idCompareResult = object1.compareTo(object2);
		}
		return idCompareResult;
	}
}
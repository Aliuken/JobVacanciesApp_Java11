package com.aliuken.jobvacanciesapp.model.comparator.superclass;

import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;

public class AbstractEntityDefaultComparator<T extends AbstractEntity<T>> extends AbstractEntityComparator<T> {
	private final int direction;

	public AbstractEntityDefaultComparator(final boolean isDescendingOrder) {
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
	public final int compare(final AbstractEntity<T> abstractEntity1, final AbstractEntity<T> abstractEntity2) {
		final T entity1 = GenericsUtils.cast(abstractEntity1);
		final T entity2 = GenericsUtils.cast(abstractEntity2);

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
}
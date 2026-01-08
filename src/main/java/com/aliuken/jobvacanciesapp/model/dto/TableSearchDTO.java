package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class TableSearchDTO implements Serializable {
	private static final long serialVersionUID = -4084683709653433040L;

	@NotEmpty(message="{language.notEmpty}")
	@Size(min=2, max=2, message="{language.minAndMaxSize}")
	private final String languageParam;

	@NotNull(message="{filterName.notEmpty}")
	private final String filterName;

	@NotNull(message="{filterValue.notEmpty}")
	private final String filterValue;

	@NotEmpty(message="{sortingField.notEmpty}")
	private final String sortingField;

	@NotEmpty(message="{sortingDirection.notEmpty}")
	private final String sortingDirection;

	@NotNull(message="{pageSize.notEmpty}")
	private final Integer pageSize;

	@NotNull(message="{pageNumber.notEmpty}")
	private final Integer pageNumber;

	public TableSearchDTO(final String languageParam, final String filterName, final String filterValue, final String sortingField, final String sortingDirection, final Integer pageSize, final Integer pageNumber) {
		if(languageParam != null) {
			this.languageParam = languageParam;
		} else {
			this.languageParam = Language.ENGLISH.getCode();
		}

		this.filterName = filterName;
		this.filterValue = filterValue;
		this.sortingField = sortingField;
		this.sortingDirection = sortingDirection;
		this.pageSize = pageSize;

		if(pageNumber != null) {
			this.pageNumber = pageNumber;
		} else {
			this.pageNumber = 0;
		}
	}

	public TableField getFilterTableField() {
		final TableField filterTableField = TableField.findByCode(filterName);
		return filterTableField;
	}

	public TableField getSortingTableField() {
		final TableField sortingTableField = TableField.findByCode(sortingField);
		return sortingTableField;
	}

	public TableSortingDirection getTableSortingDirection() {
		final TableSortingDirection tableSortingDirection = TableSortingDirection.findByCode(sortingDirection);
		return tableSortingDirection;
	}

	public TablePageSize getTablePageSize() {
		final TablePageSize tablePageSize = TablePageSize.findByValue(pageSize);
		return tablePageSize;
	}

	//If not all pagination URL parameters -> empty table (in Java)
	public boolean hasAllParameters() {
		final boolean hasAllParameters = (
			LogicalUtils.isNotNullNorEmptyString(languageParam) && !Language.BY_DEFAULT.getCode().equals(languageParam)
			&& filterName != null && filterValue != null
			&& LogicalUtils.isNotNullNorEmptyString(sortingField) && LogicalUtils.isNotNullNorEmptyString(sortingDirection)
			&& pageSize != null && pageNumber != null);
		return hasAllParameters;
	}

	@Override
	public @NonNull String toString() {
		final TableField filterTableField = this.getFilterTableField();
		final String filterTableFieldName = String.valueOf(filterTableField);
		final TableField sortingTableField = this.getSortingTableField();
		final String sortingTableFieldName = String.valueOf(sortingTableField);
		final String pageSizeString = String.valueOf(pageSize);
		final String pageNumberString = String.valueOf(pageNumber);

		final String result = StringUtils.getStringJoined("TableSearchDTO [languageParam=", languageParam,
			", filterName=", filterName, ", filterTableFieldName=", filterTableFieldName, ", filterValue=", filterValue,
			", sortingField=", sortingField, ", sortingTableFieldName=", sortingTableFieldName, ", sortingDirection=", sortingDirection,
			", pageSize=", pageSizeString, ", pageNumber=", pageNumberString, "]");
		return result;
	}
}

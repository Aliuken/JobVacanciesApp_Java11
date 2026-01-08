package com.aliuken.jobvacanciesapp.model.dto;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.javase.NumericUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Function;

@Data
public class JobVacancyDTO implements AbstractEntityDTO, Serializable {
	private static final long serialVersionUID = -3670738813828834458L;

	private static final @NonNull JobVacancyDTO NO_ARGS_INSTANCE = new JobVacancyDTO(null, null, null, null, null, null, null, null, null, null, null, null, null);

	private final Long id;

	@NotEmpty(message="{name.notEmpty}")
	@Size(max=120, message="{name.maxSize120}")
	private final String name;

	@NotEmpty(message="{description.notEmpty}")
	@Size(max=500, message="{description.maxSize}")
	private final String description;

	@NotNull(message="{jobCategory.notNull}")
	private final JobCategoryDTO jobCategory;

	//In jobVacancyForm.html we cannot use *{jobCategory.id} because JobCategoryDTO is a Java record, so we use *{jobCategoryId} instead
	private final Long jobCategoryId;

	@NotNull(message="{jobCompany.notNull}")
	private final JobCompanyDTO jobCompany;

	//In jobVacancyForm.html we cannot use *{jobCompany.id} because JobCompanyDTO is a Java record, so we use *{jobCompanyId} instead
	private final Long jobCompanyId;

	@NotEmpty(message="{statusCode.notEmpty}")
	@Size(max=1, message="{statusCode.maxSize}")
	private final String statusCode;

	@NotNull(message="{publicationDateTime.notNull}")
	private final LocalDateTime publicationDateTime;

	private final String salaryString;

	private final BigDecimalFromStringConversionResult salaryConversionResult;

	@NotEmpty(message="{currencySymbol.notEmpty}")
	@Size(max=1, message="{currencySymbol.maxSize}")
	private final String currencySymbol;

	@NotNull(message="{highlighted.notNull}")
	private final Boolean highlighted;

	@NotEmpty(message="{details.notEmpty}")
	@Size(max=10000, message="{details.maxSize}")
	private final String details;

	public JobVacancyDTO(final Long id, final String name, final String description, final JobCategoryDTO jobCategory, final Long jobCategoryId, final JobCompanyDTO jobCompany, Long jobCompanyId, final String statusCode, final LocalDateTime publicationDateTime, final String salaryString, final String currencySymbol, final Boolean highlighted, final String details) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;

		if(jobCategory == null) {
			this.jobCategory = JobCategoryDTO.getNewInstance(jobCategoryId);
			this.jobCategoryId = jobCategoryId;
		} else if(jobCategoryId == null) {
			this.jobCategory = jobCategory;
			this.jobCategoryId = jobCategory.getId();
		} else {
			this.jobCategory = jobCategory;
			this.jobCategoryId = jobCategoryId;
		}

		if(jobCompany == null) {
			this.jobCompany = JobCompanyDTO.getNewInstance(jobCompanyId);
			this.jobCompanyId = jobCompanyId;
		} else if(jobCompanyId == null) {
			this.jobCompany = jobCompany;
			this.jobCompanyId = jobCompany.getId();
		} else {
			this.jobCompany = jobCompany;
			this.jobCompanyId = jobCompanyId;
		}

		this.statusCode = statusCode;
		this.publicationDateTime = publicationDateTime;
		this.salaryString = salaryString;
		this.salaryConversionResult = NumericUtils.getBigDecimalFromStringConversionResult("jobVacancy.salary", salaryString, 10, 2);
		this.currencySymbol = currencySymbol;
		this.highlighted = highlighted;
		this.details = details;
	}

	public static @NonNull JobVacancyDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}

	public static @NonNull JobVacancyDTO getNewInstance(final Long jobVacancyId) {
		final JobVacancyDTO jobVacancyDTO = new JobVacancyDTO(jobVacancyId, null, null, null, null, null, null, null, null, null, null, null, null);
		return jobVacancyDTO;
	}

	public static @NonNull JobVacancyDTO getNewInstance(JobVacancyDTO jobVacancyDTO, final JobCompanyDTO jobCompanyDTO, final String initialCurrencySymbol) {
		if(jobVacancyDTO != null) {
			jobVacancyDTO = new JobVacancyDTO(
				jobVacancyDTO.getId(),
				jobVacancyDTO.getName(),
				jobVacancyDTO.getDescription(),
				jobVacancyDTO.getJobCategory(),
				(jobVacancyDTO.getJobCategory() != null) ? jobVacancyDTO.getJobCategory().getId() : null,
				jobCompanyDTO,
				(jobCompanyDTO != null) ? jobCompanyDTO.getId() : null,
				jobVacancyDTO.getStatusCode(),
				jobVacancyDTO.getPublicationDateTime(),
				jobVacancyDTO.getSalaryString(),
				initialCurrencySymbol,
				jobVacancyDTO.getHighlighted(),
				jobVacancyDTO.getDetails()
			);
		} else {
			jobVacancyDTO = new JobVacancyDTO(
				null,
				null,
				null,
				null,
				null,
				jobCompanyDTO,
				(jobCompanyDTO != null) ? jobCompanyDTO.getId() : null,
				null,
				null,
				null,
				null,
				null,
				null
			);
		}
		return jobVacancyDTO;
	}

	public Function<Language, String> getConversionErrorFunction() {
		final Function<Language, String> conversionErrorFunction;
		if(salaryConversionResult != null) {
			conversionErrorFunction = salaryConversionResult.getConversionErrorFunction();
		} else {
			conversionErrorFunction = null;
		}
		return conversionErrorFunction;
	}

	public BigDecimal getSalary() {
		final BigDecimal salary;
		if(salaryConversionResult != null) {
			salary = salaryConversionResult.getConversionResult();
		} else {
			salary = null;
		}
		return salary;
	}

	@Override
	public @NonNull String toString() {
		final String idString = String.valueOf(id);
		final String jobCategoryIdString = (jobCategory != null) ? String.valueOf(jobCategory.getId()) : null;
		final String jobCompanyIdString = (jobCompany != null) ? String.valueOf(jobCompany.getId()) : null;
		final String publicationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(publicationDateTime);
		final String salaryConversionResultString  = String.valueOf(salaryConversionResult);
		final String highlightedString = highlighted.toString();

		final String result = StringUtils.getStringJoined("JobVacancyDTO [id=", idString, ", name=", name, ", description=", description,
			", jobCategory=", jobCategoryIdString, ", jobCompany=", jobCompanyIdString, ", statusCode=", statusCode, ", publicationDateTime=", publicationDateTimeString, ", salary=", salaryString, ", salaryConversionResult=", salaryConversionResultString, ", currencySymbol=", currencySymbol, ", highlighted=", highlightedString, ", details=", details, "]");
		return result;
	}
}

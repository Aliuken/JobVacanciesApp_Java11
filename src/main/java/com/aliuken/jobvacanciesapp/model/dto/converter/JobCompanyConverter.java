package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.dto.JobCompanyDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobCompanyLogoDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;

import java.util.Set;

public class JobCompanyConverter extends EntityToDtoConverter<JobCompany, JobCompanyDTO> {

	private static final JobCompanyConverter SINGLETON_INSTANCE = new JobCompanyConverter();

	private JobCompanyConverter() {
		super(JobCompanyConverter::conversionFunction, JobCompany.class, JobCompanyDTO.class, JobCompanyDTO[]::new);
	}

	public static JobCompanyConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	private static JobCompanyDTO conversionFunction(final JobCompany jobCompany) {
		Long id = null;
		String name = null;
		String description = null;
		final Boolean isSelectedLogo;
		final Long selectedLogoId;
		final String selectedLogoFilePath;
		Set<JobCompanyLogoDTO> jobCompanyLogoDTOs = null;

		if(jobCompany != null) {
			id = jobCompany.getId();
			name = jobCompany.getName();
			description = jobCompany.getDescription();

			final JobCompanyLogo selectedJobCompanyLogo = jobCompany.getSelectedJobCompanyLogo();
			if(selectedJobCompanyLogo != null) {
				isSelectedLogo = Boolean.TRUE;
				selectedLogoId = selectedJobCompanyLogo.getId();
				selectedLogoFilePath = selectedJobCompanyLogo.getFilePath();
			} else {
				isSelectedLogo = Boolean.TRUE;
				selectedLogoId = Constants.NO_SELECTED_LOGO_ID;
				selectedLogoFilePath = null;
			}

			final Set<JobCompanyLogo> jobCompanyLogos = jobCompany.getJobCompanyLogos();
			jobCompanyLogoDTOs = JobCompanyLogoConverter.getInstance().convertEntitySet(jobCompanyLogos);
//			if(jobCompanyLogos != null) {
//				jobCompanyLogoDTOs = new LinkedHashSet<>();
//				for(final JobCompanyLogo jobCompanyLogo : jobCompanyLogos) {
//					JobCompanyLogoDTO jobCompanyDTOLogo = JOB_COMPANY_LOGO_CONVERTER.convertEntityElement(jobCompanyLogo);
//					jobCompanyLogoDTOs.add(jobCompanyDTOLogo);
//				}
//			} else {
//				jobCompanyLogoDTOs = null;
//			}
		} else {
			isSelectedLogo = Boolean.FALSE;
			selectedLogoId = Constants.NO_SELECTED_LOGO_ID;
			selectedLogoFilePath = null;
		}

		final JobCompanyDTO jobCompanyDTO = new JobCompanyDTO(id, name, description, isSelectedLogo, selectedLogoId, selectedLogoFilePath, jobCompanyLogoDTOs);
		return jobCompanyDTO;
	}
}
package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.dto.JobCompanyDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobCompanyLogoDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class JobCompanyConverter extends EntityToDtoConverter<JobCompany, JobCompanyDTO> {

	private static final @NonNull JobCompanyConverter SINGLETON_INSTANCE = new JobCompanyConverter();

	private JobCompanyConverter() {
		super(JobCompany.class, JobCompanyDTO.class, JobCompanyDTO[]::new);
	}

	public static @NonNull JobCompanyConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	protected @NonNull JobCompanyDTO convert(final @NonNull JobCompany jobCompany) {
		final Set<JobCompanyLogoDTO> jobCompanyLogoDTOs;
		final Long id = jobCompany.getId();
		final String name = jobCompany.getName();
		final String description = jobCompany.getDescription();
		final JobCompanyLogo selectedJobCompanyLogo = jobCompany.getSelectedJobCompanyLogo();

		final Boolean isSelectedLogo;
		final Long selectedLogoId;
		final String selectedLogoFilePath;
		if(selectedJobCompanyLogo != null) {
			isSelectedLogo = Boolean.TRUE;
			selectedLogoId = selectedJobCompanyLogo.getId();
			selectedLogoFilePath = selectedJobCompanyLogo.getFilePath();
		} else {
			isSelectedLogo = Boolean.TRUE;
			selectedLogoId = null;
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

		final JobCompanyDTO jobCompanyDTO = new JobCompanyDTO(id, name, description, isSelectedLogo, selectedLogoId, selectedLogoFilePath, jobCompanyLogoDTOs);
		return jobCompanyDTO;
	}
}
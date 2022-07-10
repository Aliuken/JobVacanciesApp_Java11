package com.aliuken.jobvacanciesapp.controller.superinterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.model.dto.JobCompanyDTO;
import com.aliuken.jobvacanciesapp.service.JobCompanyLogoService;

public interface GenericControllerWithJobCompanyLogoInterface extends GenericControllerInterface {
	public static final Logger log = LoggerFactory.getLogger(GenericControllerWithJobCompanyLogoInterface.class);

	public JobCompanyLogoService getJobCompanyLogoService();

	default void setSelectedJobCompanyLogoForJobCompanyForm(JobCompanyDTO jobCompanyDTO, String jobCompanyLogoUrlParam, boolean forceNoLogoIfBlankUrlParam) {
		if(jobCompanyLogoUrlParam != null && !jobCompanyLogoUrlParam.isEmpty()) {
			final Long jobCompanyLogoId = Long.valueOf(jobCompanyLogoUrlParam);

			if(!Constants.NO_SELECTED_LOGO_ID.equals(jobCompanyLogoId)) {
				final JobCompanyLogo jobCompanyLogo = getJobCompanyLogoService().findByIdNotOptional(jobCompanyLogoId);
				if(jobCompanyLogo != null) {
					String selectedLogoFilePath = jobCompanyLogo.getFilePath();
					jobCompanyDTO.setSelectedLogo(true);
					jobCompanyDTO.setSelectedLogoId(jobCompanyLogoId);
					jobCompanyDTO.setSelectedLogoFilePath(selectedLogoFilePath);
				} else {
					jobCompanyDTO.setSelectedLogo(false);
					jobCompanyDTO.setSelectedLogoId(null);
				}
			} else {
				jobCompanyDTO.setSelectedLogo(true);
				jobCompanyDTO.setSelectedLogoId(Constants.NO_SELECTED_LOGO_ID);
				jobCompanyDTO.setSelectedLogoFilePath(Constants.NO_SELECTED_LOGO_FILE_PATH);
			}
		} else {
			jobCompanyDTO.setSelectedLogo(false);
			if(forceNoLogoIfBlankUrlParam) {
				jobCompanyDTO.setSelectedLogoId(null);
			}
		}
	}

	default void setSelectedJobCompanyLogoForJobVacancyForm(JobCompanyDTO jobCompanyDTO, String jobCompanyLogoUrlParam, boolean forceNoLogoIfBlankUrlParam) {
		if(jobCompanyLogoUrlParam != null && !jobCompanyLogoUrlParam.isEmpty()) {
			final Long jobCompanyLogoId = Long.valueOf(jobCompanyLogoUrlParam);

			if(!Constants.NO_SELECTED_LOGO_ID.equals(jobCompanyLogoId)) {
				final JobCompanyLogo jobCompanyLogo = getJobCompanyLogoService().findByIdNotOptional(jobCompanyLogoId);
				if(jobCompanyLogo != null) {
					String selectedLogoFilePath = jobCompanyLogo.getFilePath();
					jobCompanyDTO.setSelectedLogo(true);
					jobCompanyDTO.setSelectedLogoId(jobCompanyLogoId);
					jobCompanyDTO.setSelectedLogoFilePath(selectedLogoFilePath);
				} else {
					jobCompanyDTO.setSelectedLogo(false);
					jobCompanyDTO.setSelectedLogoId(Constants.NO_SELECTED_LOGO_ID);
					jobCompanyDTO.setSelectedLogoFilePath(Constants.NO_SELECTED_LOGO_FILE_PATH);
				}
			} else {
				jobCompanyDTO.setSelectedLogo(false);
				jobCompanyDTO.setSelectedLogoId(Constants.NO_SELECTED_LOGO_ID);
				jobCompanyDTO.setSelectedLogoFilePath(Constants.NO_SELECTED_LOGO_FILE_PATH);
			}
		} else {
			jobCompanyDTO.setSelectedLogo(false);
			if(forceNoLogoIfBlankUrlParam) {
				jobCompanyDTO.setSelectedLogoId(Constants.NO_SELECTED_LOGO_ID);
				jobCompanyDTO.setSelectedLogoFilePath(Constants.NO_SELECTED_LOGO_FILE_PATH);
			}
		}
	}
}

package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ViewsAllowedDTO implements Serializable  {
	private static final long serialVersionUID = -6405424443253852944L;

	private String[] anonymousViewsArray;
	private String[] userViewsArray;
	private String[] supervisorViewsArray;
	private String[] administratorViewsArray;
}

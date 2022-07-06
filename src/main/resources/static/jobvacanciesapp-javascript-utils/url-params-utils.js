function treatLangComboAndUrlParameter(langSessionAttribute, langModelAttribute) {
  //alert("treatLangComboAndUrlParameter(" + langSessionAttribute + ", " + langModelAttribute + ")");
  
  //Set the lang URL param if it doesn't exist to the lang model attribute (or "en" if it's null)
  let lang = getParameterFromUrl("lang");
  if (lang == null){
	if (langSessionAttribute != null && langSessionAttribute != ''){
	  lang = langSessionAttribute;
	} else if (langModelAttribute != null){
	  lang = langModelAttribute;
	} else {
      lang = "en";
	}

	addOrReplaceLangParameterInURL(lang);
  }

  //Set the langCombo to the value of the lang URL param (if the langCombo exists)
  let langCombo = getComboElementIfExists("langCombo");
  if(langCombo != null) {
    //When the langCombo element change, set the value of the lang URL param to the new selected value
    langCombo.change(function () {
      let selectedOption = langCombo.val();
      addOrReplaceLangParameterInURL(selectedOption);
    });
    
    langCombo.val(lang);
  }
}

function treatJobCompanyLogoComboAndUrlParameter(jobCompanyLogoModelAttribute) {
  //alert("treatJobCompanyLogoComboAndUrlParameter(" + jobCompanyLogoModelAttribute + ")");
  
  //Set the jobCompanyLogo URL param if it doesn't exist to the jobCompanyLogo model attribute
  let jobCompanyLogo = getParameterFromUrl("jobCompanyLogo");
  if (jobCompanyLogo == null){
	jobCompanyLogo = jobCompanyLogoModelAttribute;

	addOrReplaceJobCompanyLogoParameterInURL(jobCompanyLogo);
  }

  //Set the jobCompanyLogoCombo to the value of the jobCompanyLogo URL param (if the jobCompanyLogoCombo exists)
  let jobCompanyLogoCombo = getComboElementIfExists("jobCompanyLogoCombo");
  if(jobCompanyLogoCombo != null) {
    //When the jobCompanyLogoCombo element change, set the value of the jobCompanyLogo URL param to the new selected value
    jobCompanyLogoCombo.change(function () {
      let selectedOption = jobCompanyLogoCombo.val();
      addOrReplaceJobCompanyLogoParameterInURL(selectedOption);
    });
    
    jobCompanyLogoCombo.val(jobCompanyLogo);
  }
}

function treatFilterAndPaginationCombosAndUrlParameters(tableFieldCodeModelAttribute, tableFieldValueModelAttribute, tableOrderCodeModelAttribute, tablePageSizeModelAttribute, pageNumberModelAttribute) {
  //alert("treatFilterAndPaginationCombosAndUrlParameters(" + tableFieldCodeModelAttribute + ", " + tableFieldValueModelAttribute + ", " + tableOrderCodeModelAttribute + ", " + tablePageSizeModelAttribute + ", " + pageNumberModelAttribute + ")");
  
  let addOrReplaceNeeded = false;

  //Set tableFieldCode URL param if it doesn't exist to the tableFieldCode model attribute (or "" if it's null)
  let tableFieldCode = getParameterFromUrl("tableFieldCode");

  if (tableFieldCode == null){
	if (tableFieldCodeModelAttribute != null){
	  tableFieldCode = tableFieldCodeModelAttribute;
	} else {
      tableFieldCode = "";
	}

	addOrReplaceNeeded = true;
  }

  //Set tableFieldValue URL param if it doesn't exist to the tableFieldValue model attribute (or "" if it's null)
  let tableFieldValue = getParameterFromUrl("tableFieldValue");

  if (tableFieldValue == null){
	if (tableFieldValueModelAttribute != null){
	  tableFieldValue = tableFieldValueModelAttribute;
	} else {
      tableFieldValue = "";
	}

	addOrReplaceNeeded = true;
  }

  //Set tableOrderCode URL param if it doesn't exist to the tableOrderCode model attribute (or "idAsc" if it's null)
  let tableOrderCode = getParameterFromUrl("tableOrderCode");

  if (tableOrderCode == null){
	if (tableOrderCodeModelAttribute != null){
	  tableOrderCode = tableOrderCodeModelAttribute;
	} else {
      tableOrderCode = "idAsc";
	}

	addOrReplaceNeeded = true;
  }

  //Set size URL param if it doesn't exist to the tablePageSize model attribute (or "5" if it's null)
  let tablePageSize = getParameterFromUrl("size");

  if (tablePageSize == null){
	if (tablePageSizeModelAttribute != null){
	  tablePageSize = tablePageSizeModelAttribute;
	} else {
      tablePageSize = "5";
	}

	addOrReplaceNeeded = true;
  }

  //Set page URL param if it doesn't exist to the pageNumber model attribute (or "0" if it's null)
  let pageNumber = getParameterFromUrl("page");

  if (pageNumber == null){
	if (pageNumberModelAttribute != null){
	  pageNumber = pageNumberModelAttribute;
	} else {
      pageNumber = "0";
	}

	addOrReplaceNeeded = true;
  }

  if(addOrReplaceNeeded) {
    addOrReplacePaginationParametersInURL(tableFieldCode, tableFieldValue, tableOrderCode, tablePageSize, pageNumber);
  }

  //Set the tableFieldCombo to the value of the tableField URL param (if the tableFieldCombo exists)
  let tableFieldCombo = getComboElementIfExists("tableFieldCombo");
  if(tableFieldCombo != null) {
    //When the tableFieldCombo element change, set the value of the tableField URL param to the new selected value
    tableFieldCombo.change(function () {
      let selectedOption = tableFieldCombo.val();
      addOrReplacePaginationParametersInURL(selectedOption, tableFieldValue, tableOrderCode, tablePageSize, "0");
    });
    
    tableFieldCombo.val(tableFieldCode);
  }

  //Set the tableOrderCombo to the value of the tableOrder URL param (if the tableOrderCombo exists)
  let tableOrderCombo = getComboElementIfExists("tableOrderCombo");
  if(tableOrderCombo != null) {
    //When the tableOrderCombo element change, set the value of the tableOrder URL param to the new selected value
    tableOrderCombo.change(function () {
      let selectedOption = tableOrderCombo.val();
      addOrReplacePaginationParametersInURL(tableFieldCode, tableFieldValue, selectedOption, tablePageSize, "0");
    });
    
    tableOrderCombo.val(tableOrderCode);
  }

  //Set the tablePageSizeCombo to the value of the size URL param (if the tablePageSizeCombo exists)
  let tablePageSizeCombo = getComboElementIfExists("tablePageSizeCombo");
  if(tablePageSizeCombo != null) {
    //When the tablePageSizeCombo element change, set the value of the size URL param to the new selected value
    tablePageSizeCombo.change(function () {
      let selectedOption = tablePageSizeCombo.val();
      addOrReplacePaginationParametersInURL(tableFieldCode, tableFieldValue, tableOrderCode, selectedOption, "0");
    });
    
    tablePageSizeCombo.val(tablePageSize);
  }
}

//---------------------------------------------------------------------------------------------------------------------------

function getComboElementIfExists(comboId) {
  let combo = $('#' + comboId);
  if(combo !== undefined) {
    let comboValue = combo.val();
    if(comboValue !== undefined) {
      return combo;
    }
  }
  return null;
};

function getParameterFromUrl(key) {
  const params = new Proxy(new URLSearchParams(window.location.search), {
    get: (searchParams, prop) => searchParams.get(prop),
  });
  let param = params[key];
  return param;
}

function addOrReplaceLangParameterInURL(langParamValue) {
  let searchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInURLSearchParams(searchParams, "lang", langParamValue);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + searchParams.toString();
}

function addOrReplaceJobCompanyLogoParameterInURL(jobCompanyLogoParamValue) {
  let searchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInURLSearchParams(searchParams, "jobCompanyLogo", jobCompanyLogoParamValue);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + searchParams.toString();
}

function addOrReplacePaginationParametersInURL(tableFieldCodeParamValue, tableFieldValueParamValue, tableOrderCodeParamValue, tablePageSizeParamValue, pageNumberParamValue) {
  let searchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInURLSearchParams(searchParams, "tableFieldCode", tableFieldCodeParamValue);
  addOrReplaceParameterInURLSearchParams(searchParams, "tableFieldValue", tableFieldValueParamValue);
  addOrReplaceParameterInURLSearchParams(searchParams, "tableOrderCode", tableOrderCodeParamValue);
  addOrReplaceParameterInURLSearchParams(searchParams, "size", tablePageSizeParamValue);
  addOrReplaceParameterInURLSearchParams(searchParams, "page", pageNumberParamValue);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + searchParams.toString();
}

function addOrReplaceParameterInURLSearchParams(searchParams, paramName, paramValue) {
  if(paramValue != null) {
    searchParams.set(paramName, paramValue);
  } else {
	searchParams.set(paramName, "");
  }
}
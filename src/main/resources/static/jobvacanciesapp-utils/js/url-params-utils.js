function getIsSearchJobVacanciesFromHome(descriptionModelAttribute, jobCategoryIdModelAttribute) {
  //alert("getIsSearchJobVacanciesFromHome(" + descriptionModelAttribute + ", " + jobCategoryIdModelAttribute + ")");

  let pagePath = window.location.pathname;
  //alert("pagePath: " + pagePath);

  if(pagePath === "/search") {
    if(descriptionModelAttribute != null || jobCategoryIdModelAttribute != null) {
      return true;
    }
  }

  return false;
}

function getIsPageWithTable() {
  //If the page contains a tableFieldCombo, it is a page with table
  let tableFieldCombo = getComboElementIfExists("tableFieldCombo");
  if(tableFieldCombo != null) {
    return true;
  }

  //If the page contains a tableOrderCombo, it is a page with table
  let tableOrderCombo = getComboElementIfExists("tableOrderCombo");
  if(tableOrderCombo != null) {
    return true;
  }

  //If the page contains a pageSizeCombo, it is a page with table
  let pageSizeCombo = getComboElementIfExists("pageSizeCombo");
  if(pageSizeCombo != null) {
    return true;
  }

  return false;
}

//---------------------------------------------------------------------------------------------------------------------------

function treatSearchJobVacanciesFromHome(languageSessionAttribute, languageModelAttribute, defaultLanguage, descriptionModelAttribute, jobCategoryIdModelAttribute) {
  //alert("treatSearchJobVacanciesFromHome(" + languageSessionAttribute + ", " + languageModelAttribute + ", " + defaultLanguage + ", " + descriptionModelAttribute + ", " + jobCategoryIdModelAttribute + ")");

  let addOrReplaceNeeded = false;

  //Set the languageParam URL param to the value from the URL param or the session or the model attribute or the default language
  let languageCode = getParameterFromUrl("languageParam");
  if (languageCode == null || languageCode == ''){
    if (languageSessionAttribute != null && languageSessionAttribute != ''){
      languageCode = languageSessionAttribute;
    } else if (languageModelAttribute != null && languageModelAttribute != ''){
      languageCode = languageModelAttribute;
    } else if (defaultLanguage != null && defaultLanguage != ''){
      languageCode = defaultLanguage;
    } else {
      languageCode = "en";
    }

    addOrReplaceNeeded = true;
  }

  //Set description URL param if it doesn't exist to the description model attribute (or "" if it's null)
  let description = getParameterFromUrl("description");

  if (description == null){
    if (descriptionModelAttribute != null){
      description = descriptionModelAttribute;
    } else {
      description = "";
    }

    addOrReplaceNeeded = true;
  }

  //Set jobCategoryId URL param if it doesn't exist to the jobCategoryId model attribute (or "" if it's null)
  let jobCategoryId = getParameterFromUrl("jobCategoryId");

  if (jobCategoryId == null){
    if (jobCategoryIdModelAttribute != null){
      jobCategoryId = jobCategoryIdModelAttribute;
    } else {
      jobCategoryId = "";
    }

    addOrReplaceNeeded = true;
  }

  if(addOrReplaceNeeded) {
    addOrReplaceHomeParametersInURL(languageCode, description, jobCategoryId);
  }

  //Set the languageCombo to the value of the language URL param (if the languageCombo exists)
  let languageCombo = getComboElementIfExists("languageCombo");
  if(languageCombo != null) {
    //When the languageCombo element change, set the value of the language URL param to the new selected value
    languageCombo.change(function () {
      let selectedOption = languageCombo.val();
      addOrReplaceHomeParametersInURL(selectedOption, description, jobCategoryId);
    });

    languageCombo.val(languageCode);
  }

  //Set the jobCategoryIdCombo to the value of the jobCategoryId URL param (if the jobCategoryIdCombo exists)
  let jobCategoryIdCombo = getComboElementIfExists("jobCategoryIdCombo");
  if(jobCategoryIdCombo != null) {
    //When the jobCategoryIdCombo element change, set the value of the jobCategoryId URL param to the new selected value
    jobCategoryIdCombo.change(function () {
      let selectedOption = jobCategoryIdCombo.val();
      addOrReplaceHomeParametersInURL(languageCode, description, selectedOption);
    });

    jobCategoryIdCombo.val(jobCategoryId);
  }
}

function treatFilterAndPaginationCombosAndUrlParameters(languageSessionAttribute, languageModelAttribute, defaultLanguage, tableFieldCodeModelAttribute, tableFieldValueModelAttribute, tableOrderCodeModelAttribute, pageSizeModelAttribute, pageNumberModelAttribute) {
  //alert("treatFilterAndPaginationCombosAndUrlParameters(" + languageSessionAttribute + ", " + languageModelAttribute + ", " + defaultLanguage + ", " + tableFieldCodeModelAttribute + ", " + tableFieldValueModelAttribute + ", " + tableOrderCodeModelAttribute + ", " + pageSizeModelAttribute + ", " + pageNumberModelAttribute + ")");

  let addOrReplaceNeeded = false;

  //Set the languageParam URL param to the value from the URL param or the session or the model attribute or the default language
  let languageCode = getParameterFromUrl("languageParam");
  if (languageCode == null || languageCode == ''){
    if (languageSessionAttribute != null && languageSessionAttribute != ''){
      languageCode = languageSessionAttribute;
    } else if (languageModelAttribute != null && languageModelAttribute != ''){
      languageCode = languageModelAttribute;
    } else if (defaultLanguage != null && defaultLanguage != ''){
      languageCode = defaultLanguage;
    } else {
      languageCode = "en";
    }

    addOrReplaceNeeded = true;
  }

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

  //Set pageSize URL param if it doesn't exist to the pageSize model attribute (or "5" if it's null)
  let pageSize = getParameterFromUrl("pageSize");

  if (pageSize == null){
    if (pageSizeModelAttribute != null){
      pageSize = pageSizeModelAttribute;
    } else {
      pageSize = "5";
    }

    addOrReplaceNeeded = true;
  }

  //Set pageNumber URL param if it doesn't exist to the pageNumber model attribute (or "0" if it's null)
  let pageNumber = getParameterFromUrl("pageNumber");

  if (pageNumber == null){
    if (pageNumberModelAttribute != null){
      pageNumber = pageNumberModelAttribute;
    } else {
      pageNumber = "0";
    }

    addOrReplaceNeeded = true;
  }

  if(addOrReplaceNeeded) {
    addOrReplacePaginationParametersInURL(languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
  }

  //Set the languageCombo to the value of the language URL param (if the languageCombo exists)
  let languageCombo = getComboElementIfExists("languageCombo");
  if(languageCombo != null) {
    //When the languageCombo element change, set the value of the language URL param to the new selected value
    languageCombo.change(function () {
      let selectedOption = languageCombo.val();
      addOrReplacePaginationParametersInURL(selectedOption, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, "0");
    });

    languageCombo.val(languageCode);
  }

  //Set the tableFieldCombo to the value of the tableField URL param (if the tableFieldCombo exists)
  let tableFieldCombo = getComboElementIfExists("tableFieldCombo");
  if(tableFieldCombo != null) {
    //When the tableFieldCombo element change, set the value of the tableField URL param to the new selected value
    tableFieldCombo.change(function () {
      let selectedOption = tableFieldCombo.val();
      addOrReplacePaginationParametersInURL(languageCode, selectedOption, tableFieldValue, tableOrderCode, pageSize, "0");
    });

    tableFieldCombo.val(tableFieldCode);
  }

  //Set the tableOrderCombo to the value of the tableOrder URL param (if the tableOrderCombo exists)
  let tableOrderCombo = getComboElementIfExists("tableOrderCombo");
  if(tableOrderCombo != null) {
    //When the tableOrderCombo element change, set the value of the tableOrder URL param to the new selected value
    tableOrderCombo.change(function () {
      let selectedOption = tableOrderCombo.val();
      addOrReplacePaginationParametersInURL(languageCode, tableFieldCode, tableFieldValue, selectedOption, pageSize, "0");
    });

    tableOrderCombo.val(tableOrderCode);
  }

  //Set the pageSizeCombo to the value of the size URL param (if the pageSizeCombo exists)
  let pageSizeCombo = getComboElementIfExists("pageSizeCombo");
  if(pageSizeCombo != null) {
    //When the pageSizeCombo element change, set the value of the size URL param to the new selected value
    pageSizeCombo.change(function () {
      let selectedOption = pageSizeCombo.val();
      addOrReplacePaginationParametersInURL(languageCode, tableFieldCode, tableFieldValue, tableOrderCode, selectedOption, "0");
    });

    pageSizeCombo.val(pageSize);
  }
}

function treatLanguageComboAndUrlParameter(languageSessionAttribute, languageModelAttribute, defaultLanguage) {
  //alert("treatLanguageComboAndUrlParameter(" + languageSessionAttribute + ", " + languageModelAttribute + ", " + defaultLanguage + ")");

  //Set the languageParam URL param to the value from the URL param or the session or the model attribute or the default language
  let languageCode = getParameterFromUrl("languageParam");
  if (languageCode == null || languageCode == ''){
    if (languageSessionAttribute != null && languageSessionAttribute != ''){
      languageCode = languageSessionAttribute;
    } else if (languageModelAttribute != null && languageModelAttribute != ''){
      languageCode = languageModelAttribute;
    } else if (defaultLanguage != null && defaultLanguage != ''){
      languageCode = defaultLanguage;
    } else {
      languageCode = "en";
    }

    addOrReplaceLanguageParamInURL(languageCode);
  }

  //Set the languageCombo to the value of the language URL param (if the languageCombo exists)
  let languageCombo = getComboElementIfExists("languageCombo");
  if(languageCombo != null) {
    //When the languageCombo element change, set the value of the language URL param to the new selected value
    languageCombo.change(function () {
      let selectedOption = languageCombo.val();
      addOrReplaceLanguageParamInURL(selectedOption);
    });

    languageCombo.val(languageCode);
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

function deleteParameterInURL(paramName) {
  let searchParams = new URLSearchParams(window.location.search);
  searchParams.delete(paramName);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + searchParams.toString();
}

function addOrReplaceLanguageParamInURL(languageCode) {
  let searchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInURLSearchParams(searchParams, "languageParam", languageCode);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + searchParams.toString();
}

function addOrReplaceJobCompanyLogoParameterInURL(jobCompanyLogo) {
  let searchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInURLSearchParams(searchParams, "jobCompanyLogo", jobCompanyLogo);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + searchParams.toString();
}

function addOrReplaceHomeParametersInURL(languageCode, description, jobCategoryId) {
  let searchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInURLSearchParams(searchParams, "languageParam", languageCode);
  addOrReplaceParameterInURLSearchParams(searchParams, "description", description);
  addOrReplaceParameterInURLSearchParams(searchParams, "jobCategoryId", jobCategoryId);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + searchParams.toString();
}

function addOrReplacePaginationParametersInURL(languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber) {
  let searchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInURLSearchParams(searchParams, "languageParam", languageCode);
  addOrReplaceParameterInURLSearchParams(searchParams, "tableFieldCode", tableFieldCode);
  addOrReplaceParameterInURLSearchParams(searchParams, "tableFieldValue", tableFieldValue);
  addOrReplaceParameterInURLSearchParams(searchParams, "tableOrderCode", tableOrderCode);
  addOrReplaceParameterInURLSearchParams(searchParams, "pageSize", pageSize);
  addOrReplaceParameterInURLSearchParams(searchParams, "pageNumber", pageNumber);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + searchParams.toString();
}

function addOrReplaceParameterInURLSearchParams(searchParams, paramName, paramValue) {
  searchParams.delete(paramName);
  if(paramValue != null) {
    searchParams.set(paramName, paramValue);
  } else {
    searchParams.set(paramName, "");
  }
}
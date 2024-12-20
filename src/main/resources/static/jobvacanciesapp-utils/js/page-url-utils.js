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

//---------------------------------------------------------------------------------------------------------------------------

function treatSearchJobVacanciesFromHome(languageSessionAttribute, languageModelAttribute, defaultLanguage, descriptionModelAttribute, jobCategoryIdModelAttribute) {
  //alert("treatSearchJobVacanciesFromHome(" + languageSessionAttribute + ", " + languageModelAttribute + ", " + defaultLanguage + ", " + descriptionModelAttribute + ", " + jobCategoryIdModelAttribute + ")");

  let urlSearchParams = new URLSearchParams(window.location.search);

  let addOrReplaceNeeded = false;

  //Set the languageParam URL param to the value from the URL param or the session or the model attribute or the default language
  let languageCode = getParameterFromUrlSearchParams(urlSearchParams, "languageParam");
  if(languageCode == null || languageCode == '' || languageCode == '--') {
    if(languageSessionAttribute != null && languageSessionAttribute != '' && languageSessionAttribute != '--') {
      languageCode = languageSessionAttribute;
    } else if(languageModelAttribute != null && languageModelAttribute != '' && languageModelAttribute != '--') {
      languageCode = languageModelAttribute;
    } else if(defaultLanguage != null && defaultLanguage != '' && defaultLanguage != '--') {
      languageCode = defaultLanguage;
    } else {
      languageCode = "en";
    }

    addOrReplaceNeeded = true;
  }

  //Set description URL param if it doesn't exist to the description model attribute (or "" if it's null)
  let description = getParameterFromUrlSearchParams(urlSearchParams, "description");
  if(description == null) {
    if(descriptionModelAttribute != null) {
      description = descriptionModelAttribute;
    } else {
      description = "";
    }

    addOrReplaceNeeded = true;
  }

  //Set jobCategoryId URL param if it doesn't exist to the jobCategoryId model attribute (or "" if it's null)
  let jobCategoryId = getParameterFromUrlSearchParams(urlSearchParams, "jobCategoryId");
  if(jobCategoryId == null) {
    if(jobCategoryIdModelAttribute != null) {
      jobCategoryId = jobCategoryIdModelAttribute;
    } else {
      jobCategoryId = "";
    }

    addOrReplaceNeeded = true;
  }

  if(addOrReplaceNeeded) {
    addOrReplaceHomeParametersInUrl(languageCode, description, jobCategoryId);
  }

  //Set the languageCombo to the value of the language URL param (if the languageCombo exists)
  let languageCombo = getComboElementIfExists("languageCombo");
  if(languageCombo != null) {
    //When the languageCombo element change, set the value of the language URL param to the new selected value
    languageCombo.change(function () {
      let selectedOption = languageCombo.val();
      addOrReplaceHomeParametersInUrl(selectedOption, description, jobCategoryId);
    });

    languageCombo.val(languageCode);
  }

  //Set the jobCategoryIdCombo to the value of the jobCategoryId URL param (if the jobCategoryIdCombo exists)
  let jobCategoryIdCombo = getComboElementIfExists("jobCategoryIdCombo");
  if(jobCategoryIdCombo != null) {
    //When the jobCategoryIdCombo element change, set the value of the jobCategoryId URL param to the new selected value
    jobCategoryIdCombo.change(function () {
      let selectedOption = jobCategoryIdCombo.val();
      addOrReplaceHomeParametersInUrl(languageCode, description, selectedOption);
    });

    jobCategoryIdCombo.val(jobCategoryId);
  }
}

function treatFilterAndPaginationCombosAndUrlParameters(languageSessionAttribute, languageModelAttribute, defaultLanguage, filterNameModelAttribute, filterValueModelAttribute, sortingFieldModelAttribute, sortingDirectionModelAttribute, pageSizeModelAttribute, pageNumberModelAttribute) {
  //alert("treatFilterAndPaginationCombosAndUrlParameters(" + languageSessionAttribute + ", " + languageModelAttribute + ", " + defaultLanguage + ", " + filterNameModelAttribute + ", " + filterValueModelAttribute + ", " + sortingFieldModelAttribute + ", " + sortingDirectionModelAttribute + ", " + pageSizeModelAttribute + ", " + pageNumberModelAttribute + ")");

  let urlSearchParams = new URLSearchParams(window.location.search);

  let addOrReplaceNeeded = false;

  //Set the languageParam URL param to the value from the URL param or the session or the model attribute or the default language
  let languageCode = getParameterFromUrlSearchParams(urlSearchParams, "languageParam");
  if(languageCode == null || languageCode == '' || languageCode == '--') {
    if(languageSessionAttribute != null && languageSessionAttribute != '' && languageSessionAttribute != '--') {
      languageCode = languageSessionAttribute;
    } else if(languageModelAttribute != null && languageModelAttribute != '' && languageModelAttribute != '--') {
      languageCode = languageModelAttribute;
    } else if(defaultLanguage != null && defaultLanguage != '' && defaultLanguage != '--') {
      languageCode = defaultLanguage;
    } else {
      languageCode = "en";
    }

    addOrReplaceNeeded = true;
  }

  //Set filterName URL param if it doesn't exist to the filterName model attribute (or "" if it's null)
  let filterName = getParameterFromUrlSearchParams(urlSearchParams, "filterName");
  if(filterName == null) {
    if(filterNameModelAttribute != null) {
      filterName = filterNameModelAttribute;
    } else {
      filterName = "";
    }

    addOrReplaceNeeded = true;
  }

  //Set filterValue URL param if it doesn't exist to the filterValue model attribute (or "" if it's null)
  let filterValue = getParameterFromUrlSearchParams(urlSearchParams, "filterValue");
  if(filterValue == null) {
    if(filterValueModelAttribute != null) {
      filterValue = filterValueModelAttribute;
    } else {
      filterValue = "";
    }

    addOrReplaceNeeded = true;
  }

  //Set sortingField URL param if it doesn't exist to the sortingField model attribute (or "id" if it's null)
  let sortingField = getParameterFromUrlSearchParams(urlSearchParams, "sortingField");
  if(sortingField == null || sortingField == '') {
    if(sortingFieldModelAttribute != null && sortingFieldModelAttribute != '') {
      sortingField = sortingFieldModelAttribute;
    } else {
      sortingField = "id";
    }

    addOrReplaceNeeded = true;
  }

  //Set sortingDirection URL param if it doesn't exist to the sortingDirection model attribute (or "asc" if it's null)
  let sortingDirection = getParameterFromUrlSearchParams(urlSearchParams, "sortingDirection");
  if(sortingDirection == null || sortingDirection == '') {
    if(sortingDirectionModelAttribute != null && sortingDirectionModelAttribute != '') {
      sortingDirection = sortingDirectionModelAttribute;
    } else {
      sortingDirection = "asc";
    }

    addOrReplaceNeeded = true;
  }

  //Set pageSize URL param if it doesn't exist to the pageSize model attribute (or "5" if it's null)
  let pageSize = getParameterFromUrlSearchParams(urlSearchParams, "pageSize");
  if(pageSize == null || pageSize == '') {
    if(pageSizeModelAttribute != null && pageSizeModelAttribute != '') {
      pageSize = pageSizeModelAttribute;
    } else {
      pageSize = "5";
    }

    addOrReplaceNeeded = true;
  }

  //Set pageNumber URL param if it doesn't exist to the pageNumber model attribute (or "0" if it's null)
  let pageNumber = getParameterFromUrlSearchParams(urlSearchParams, "pageNumber");
  if(pageNumber == null || pageNumber == '') {
    if(pageNumberModelAttribute != null && pageNumberModelAttribute != '') {
      pageNumber = pageNumberModelAttribute;
    } else {
      pageNumber = "0";
    }

    addOrReplaceNeeded = true;
  }

  if(addOrReplaceNeeded) {
    addOrReplacePaginationParametersInUrl(languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
  }

  //Set the languageCombo to the value of the language URL param (if the languageCombo exists)
  let languageCombo = getComboElementIfExists("languageCombo");
  if(languageCombo != null) {
    //When the languageCombo element change, set the value of the language URL param to the new selected value
    languageCombo.change(function () {
      let selectedOption = languageCombo.val();
      addOrReplacePaginationParametersInUrl(selectedOption, filterName, filterValue, sortingField, sortingDirection, pageSize, "0");
    });

    languageCombo.val(languageCode);
  }

  //Set the tableFieldCombo to the value of the tableField URL param (if the tableFieldCombo exists)
  let tableFieldCombo = getComboElementIfExists("tableFieldCombo");
  if(tableFieldCombo != null) {
    //When the tableFieldCombo element change, set the value of the tableField URL param to the new selected value
    tableFieldCombo.change(function () {
      let selectedOption = tableFieldCombo.val();
      addOrReplacePaginationParametersInUrl(languageCode, selectedOption, filterValue, sortingField, sortingDirection, pageSize, "0");
    });

    tableFieldCombo.val(filterName);
  }

  //Set the sortingFieldCombo to the value of the sortingField URL param (if the sortingFieldCombo exists)
  let sortingFieldCombo = getComboElementIfExists("sortingFieldCombo");
  if(sortingFieldCombo != null) {
    //When the sortingFieldCombo element change, set the value of the sortingField URL param to the new selected value
    sortingFieldCombo.change(function () {
      let selectedOption = sortingFieldCombo.val();
      addOrReplacePaginationParametersInUrl(languageCode, filterName, filterValue, selectedOption, sortingDirection, pageSize, "0");
    });

    sortingFieldCombo.val(sortingField);
  }

  //Set the sortingDirectionCombo to the value of the sortingDirection URL param (if the sortingDirectionCombo exists)
  let sortingDirectionCombo = getComboElementIfExists("sortingDirectionCombo");
  if(sortingDirectionCombo != null) {
    //When the sortingDirectionCombo element change, set the value of the sortingDirection URL param to the new selected value
    sortingDirectionCombo.change(function () {
      let selectedOption = sortingDirectionCombo.val();
      addOrReplacePaginationParametersInUrl(languageCode, filterName, filterValue, sortingField, selectedOption, pageSize, "0");
    });

    sortingDirectionCombo.val(sortingDirection);
  }

  //Set the pageSizeCombo to the value of the size URL param (if the pageSizeCombo exists)
  let pageSizeCombo = getComboElementIfExists("pageSizeCombo");
  if(pageSizeCombo != null) {
    //When the pageSizeCombo element change, set the value of the size URL param to the new selected value
    pageSizeCombo.change(function () {
      let selectedOption = pageSizeCombo.val();
      addOrReplacePaginationParametersInUrl(languageCode, filterName, filterValue, sortingField, sortingDirection, selectedOption, "0");
    });

    pageSizeCombo.val(pageSize);
  }
}

function treatLanguageComboAndUrlParameter(languageSessionAttribute, languageModelAttribute, defaultLanguage) {
  //alert("treatLanguageComboAndUrlParameter(" + languageSessionAttribute + ", " + languageModelAttribute + ", " + defaultLanguage + ")");

  let urlSearchParams = new URLSearchParams(window.location.search);

  //Set the languageParam URL param to the value from the URL param or the session or the model attribute or the default language
  let languageCode = getParameterFromUrlSearchParams(urlSearchParams, "languageParam");
  if(languageCode == null || languageCode == '' || languageCode == '--') {
    if(languageSessionAttribute != null && languageSessionAttribute != '' && languageSessionAttribute != '--') {
      languageCode = languageSessionAttribute;
    } else if(languageModelAttribute != null && languageModelAttribute != '' && languageModelAttribute != '--') {
      languageCode = languageModelAttribute;
    } else if(defaultLanguage != null && defaultLanguage != '' && defaultLanguage != '--') {
      languageCode = defaultLanguage;
    } else {
      languageCode = "en";
    }

    addOrReplaceLanguageParamInUrl(languageCode);
  }

  //Set the languageCombo to the value of the language URL param (if the languageCombo exists)
  let languageCombo = getComboElementIfExists("languageCombo");
  if(languageCombo != null) {
    //When the languageCombo element change, set the value of the language URL param to the new selected value
    languageCombo.change(function () {
      let selectedOption = languageCombo.val();
      addOrReplaceLanguageParamInUrl(selectedOption);
    });

    languageCombo.val(languageCode);
  }
}

function treatJobCompanyLogoComboAndUrlParameter(jobCompanyLogoModelAttribute) {
  //alert("treatJobCompanyLogoComboAndUrlParameter(" + jobCompanyLogoModelAttribute + ")");

  let urlSearchParams = new URLSearchParams(window.location.search);

  //Set the jobCompanyLogo URL param if it doesn't exist to the jobCompanyLogo model attribute
  let jobCompanyLogo = getParameterFromUrlSearchParams(urlSearchParams, "jobCompanyLogo");
  if(jobCompanyLogo == null) {
    jobCompanyLogo = jobCompanyLogoModelAttribute;
    addOrReplaceJobCompanyLogoParameterInUrl(jobCompanyLogo);
  }

  //Set the jobCompanyLogoCombo to the value of the jobCompanyLogo URL param (if the jobCompanyLogoCombo exists)
  let jobCompanyLogoCombo = getComboElementIfExists("jobCompanyLogoCombo");
  if(jobCompanyLogoCombo != null) {
    //When the jobCompanyLogoCombo element change, set the value of the jobCompanyLogo URL param to the new selected value
    jobCompanyLogoCombo.change(function () {
      let selectedOption = jobCompanyLogoCombo.val();
      addOrReplaceJobCompanyLogoParameterInUrl(selectedOption);
    });

    jobCompanyLogoCombo.val(jobCompanyLogo);
  }
}

//---------------------------------------------------------------------------------------------------------------------------

function deleteParameterInUrl(paramName) {
  let urlSearchParams = new URLSearchParams(window.location.search);
  urlSearchParams.delete(paramName);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + urlSearchParams.toString();
}

function addOrReplaceLanguageParamInUrl(languageCode) {
  let urlSearchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "languageParam", languageCode);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + urlSearchParams.toString();
}

function addOrReplaceJobCompanyLogoParameterInUrl(jobCompanyLogo) {
  let urlSearchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "jobCompanyLogo", jobCompanyLogo);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + urlSearchParams.toString();
}

function addOrReplaceHomeParametersInUrl(languageCode, description, jobCategoryId) {
  let urlSearchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "languageParam", languageCode);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "description", description);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "jobCategoryId", jobCategoryId);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + urlSearchParams.toString();
}

function addOrReplacePaginationParametersInUrl(languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber) {
  let urlSearchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "languageParam", languageCode);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "filterName", filterName);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "filterValue", filterValue);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "sortingField", sortingField);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "sortingDirection", sortingDirection);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "pageSize", pageSize);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "pageNumber", pageNumber);
  window.location = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + urlSearchParams.toString();
}

function addOrReplaceParameterInUrlSearchParams(urlSearchParams, paramName, paramValue) {
  urlSearchParams.delete(paramName);
  if(paramValue != null) {
    urlSearchParams.set(paramName, paramValue);
  } else {
    urlSearchParams.set(paramName, "");
  }
}

//If not all pagination URL parameters -> empty table (in JavaScript)
function getHasAllPaginationParametersInUrl() {
  let urlSearchParams = new URLSearchParams(window.location.search);

  let languageCode = getParameterFromUrlSearchParams(urlSearchParams, "languageParam");
  if(languageCode == null || languageCode == '' || languageCode == '--') {
    return false;
  }

  let filterName = getParameterFromUrlSearchParams(urlSearchParams, "filterName");
  if(filterName == null) {
    return false;
  }

  let filterValue = getParameterFromUrlSearchParams(urlSearchParams, "filterValue");
  if(filterValue == null) {
    return false;
  }

  let sortingField = getParameterFromUrlSearchParams(urlSearchParams, "sortingField");
  if(sortingField == null || sortingField == '') {
    return false;
  }

  let sortingDirection = getParameterFromUrlSearchParams(urlSearchParams, "sortingDirection");
  if(sortingDirection == null || sortingDirection == '') {
    return false;
  }

  let pageSize = getParameterFromUrlSearchParams(urlSearchParams, "pageSize");
  if(pageSize == null || pageSize == '') {
    return false;
  }

  let pageNumber = getParameterFromUrlSearchParams(urlSearchParams, "pageNumber");
  if(pageNumber == null || pageNumber == '') {
    return false;
  }

  return true;
}

function getParameterFromUrlSearchParams(urlSearchParams, key) {
  const params = new Proxy(urlSearchParams, {
    get: (searchParams, prop) => searchParams.get(prop),
  });
  let param = params[key];
  return param;
}

function getParameterFromUrl(key) {
  let urlSearchParams = new URLSearchParams(window.location.search);
  const params = new Proxy(urlSearchParams, {
    get: (searchParams, prop) => searchParams.get(prop),
  });
  let param = params[key];
  return param;
}
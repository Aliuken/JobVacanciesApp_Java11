"use strict";

// Custom types and constants

const comboOnChangeEventListenerMap = new Map();

const DEFAULT_PAGE_SIZE = "5";
const DEFAULT_PAGE_NUMBER = "0";
const DEFAULT_SORTING_FIELD = "id";
const DEFAULT_SORTING_DIRECTION = "asc";
const EMPTY_STRING = "";
const FIRST_PAGE_NUMBER = "0";
const LANGUAGE_BY_DEFAULT = "--";
const ENGLISH_LANGUAGE = "en";

//---------------------------------------------------------------------------------------------------------------------------

// Main functions

function getIsPageWithTable() {
  //alert("getIsPageWithTable()");

  const tableFilterAndPaginationForm = getElementIfExists("tableFilterAndPaginationForm");
  if (tableFilterAndPaginationForm !== null) {
    return true;
  }

  const tablePaginationNav = getElementIfExists("tablePaginationNav");
  if (tablePaginationNav !== null) {
    return true;
  }

  return false;
}

function getIsSearchJobVacanciesFromHome(descriptionModelAttribute, jobCategoryIdModelAttribute) {
  //alert("getIsSearchJobVacanciesFromHome(" + descriptionModelAttribute + ", " + jobCategoryIdModelAttribute + ")");

  const pagePath = window.location.pathname;
  //alert("pagePath: " + pagePath);

  if (pagePath === "/search") {
    const result = (isValidField(descriptionModelAttribute) || isValidField(jobCategoryIdModelAttribute));
    return result;
  }
  return false;
}

function treatSearchJobVacanciesFromHome(languageCodeElementValue, descriptionModelAttribute, jobCategoryIdModelAttribute) {
  //alert("treatSearchJobVacanciesFromHome(" + languageCodeElementValue + ", " + descriptionModelAttribute + ", " + jobCategoryIdModelAttribute + ")");

  const urlSearchParams = new URLSearchParams(window.location.search);
  let addOrReplaceNeeded = false;

  //Set the languageParam URL param to the value from the URL param or the session or the model attribute or the default language or ENGLISH_LANGUAGE
  let languageCode = getParameterFromUrlSearchParams(urlSearchParams, "languageParam");
  if (!isValidLanguageCode(languageCode)) {
    languageCode = isValidLanguageCode(languageCodeElementValue) ? languageCodeElementValue : ENGLISH_LANGUAGE;
    addOrReplaceNeeded = true;
  }

  //Set description URL param if it does not exist to the description model attribute (or EMPTY_STRING if it is null)
  let description = getParameterFromUrlSearchParams(urlSearchParams, "description");
  if (description === null) {
    description = descriptionModelAttribute ?? EMPTY_STRING;
    addOrReplaceNeeded = true;
  }

  //Set jobCategoryId URL param if it does not exist to the jobCategoryId model attribute (or EMPTY_STRING if it is null)
  let jobCategoryIdString = getParameterFromUrlSearchParams(urlSearchParams, "jobCategoryId");
  if (jobCategoryIdString === null) {
    jobCategoryIdString = jobCategoryIdModelAttribute?.toString() ?? EMPTY_STRING;
    addOrReplaceNeeded = true;
  }

  if (addOrReplaceNeeded) {
    addOrReplaceHomeParametersInUrl(languageCode, description, jobCategoryIdString);
  }

  //Set the combos onChange callback to replace the corresponding URL param with the selected value
  const comboOnChangeCallbackDataArray = [
    {
      comboElementId: "languageCombo",
      urlParamValue: languageCode,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplaceHomeParametersInUrl(selectedOption, description, jobCategoryIdString)
    },
    {
      comboElementId: "jobCategoryIdCombo",
      urlParamValue: jobCategoryIdString,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplaceHomeParametersInUrl(languageCode, description, selectedOption)
    }
  ];
  setCombosOnChangeCallback(comboOnChangeCallbackDataArray);
}

function treatFilterAndPaginationCombosAndUrlParameters(languageCodeElementValue, filterNameModelAttribute, filterValueModelAttribute, sortingFieldModelAttribute, sortingDirectionModelAttribute, pageSizeModelAttribute, pageNumberModelAttribute) {
  //alert("treatFilterAndPaginationCombosAndUrlParameters(" + languageCodeElementValue + ", " + filterNameModelAttribute + ", " + filterValueModelAttribute + ", " + sortingFieldModelAttribute + ", " + sortingDirectionModelAttribute + ", " + pageSizeModelAttribute + ", " + pageNumberModelAttribute + ")");

  const urlSearchParams = new URLSearchParams(window.location.search);
  let addOrReplaceNeeded = false;

  //Set the languageParam URL param to the value from the URL param or the session or the model attribute or the default language or ENGLISH_LANGUAGE
  let languageCode = getParameterFromUrlSearchParams(urlSearchParams, "languageParam");
  if (!isValidLanguageCode(languageCode)) {
    languageCode = isValidLanguageCode(languageCodeElementValue) ? languageCodeElementValue : ENGLISH_LANGUAGE;
    addOrReplaceNeeded = true;
  }

  //Set filterName URL param if it does not exist to the filterName model attribute (or EMPTY_STRING if it is null)
  let filterName = getParameterFromUrlSearchParams(urlSearchParams, "filterName");
  if (filterName === null) {
    filterName = filterNameModelAttribute ?? EMPTY_STRING;
    addOrReplaceNeeded = true;
  }

  //Set filterValue URL param if it does not exist to the filterValue model attribute (or EMPTY_STRING if it is null)
  let filterValue = getParameterFromUrlSearchParams(urlSearchParams, "filterValue");
  if (filterValue === null) {
    filterValue = filterValueModelAttribute ?? EMPTY_STRING;
    addOrReplaceNeeded = true;
  }

  //Set sortingField URL param if it does not exist to the sortingField model attribute (or DEFAULT_SORTING_FIELD if it is null)
  let sortingField = getParameterFromUrlSearchParams(urlSearchParams, "sortingField");
  if (!isValidStringField(sortingField)) {
    sortingField = isValidStringField(sortingFieldModelAttribute) ? sortingFieldModelAttribute : DEFAULT_SORTING_FIELD;
    addOrReplaceNeeded = true;
  }

  //Set sortingDirection URL param if it does not exist to the sortingDirection model attribute (or DEFAULT_SORTING_DIRECTION if it is null)
  let sortingDirection = getParameterFromUrlSearchParams(urlSearchParams, "sortingDirection");
  if (!isValidStringField(sortingDirection)) {
    sortingDirection = isValidStringField(sortingDirectionModelAttribute) ? sortingDirectionModelAttribute : DEFAULT_SORTING_DIRECTION;
    addOrReplaceNeeded = true;
  }

  //Set pageSize URL param if it does not exist to the pageSize model attribute (or DEFAULT_PAGE_SIZE if it is null)
  let pageSize = getParameterFromUrlSearchParams(urlSearchParams, "pageSize");
  if (!isValidStringField(pageSize)) {
    pageSize = isValidStringField(pageSizeModelAttribute) ? pageSizeModelAttribute : DEFAULT_PAGE_SIZE;
    addOrReplaceNeeded = true;
  }

  //Set pageNumber URL param if it does not exist to the pageNumber model attribute (or DEFAULT_PAGE_NUMBER if it is null)
  let pageNumber = getParameterFromUrlSearchParams(urlSearchParams, "pageNumber");
  if (!isValidStringField(pageNumber)) {
    pageNumber = isValidStringField(pageNumberModelAttribute) ? pageNumberModelAttribute : DEFAULT_PAGE_NUMBER;
    addOrReplaceNeeded = true;
  }

  if (addOrReplaceNeeded) {
    addOrReplacePaginationParametersInUrl(languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
  }

  //Set the combos onChange callback to replace the corresponding URL param with the selected value
  const comboOnChangeCallbackDataArray = [
    {
      comboElementId: "languageCombo",
      urlParamValue: languageCode,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplacePaginationParametersInUrl(selectedOption, filterName, filterValue, sortingField, sortingDirection, pageSize, FIRST_PAGE_NUMBER)
    },
    {
      comboElementId: "tableFieldCombo",
      urlParamValue: filterName,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplacePaginationParametersInUrl(languageCode, selectedOption, filterValue, sortingField, sortingDirection, pageSize, FIRST_PAGE_NUMBER)
    },
    {
      comboElementId: "sortingFieldCombo",
      urlParamValue: sortingField,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplacePaginationParametersInUrl(languageCode, filterName, filterValue, selectedOption, sortingDirection, pageSize, FIRST_PAGE_NUMBER)
    },
    {
      comboElementId: "sortingDirectionCombo",
      urlParamValue: sortingDirection,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplacePaginationParametersInUrl(languageCode, filterName, filterValue, sortingField, selectedOption, pageSize, FIRST_PAGE_NUMBER)
    },
    {
      comboElementId: "pageSizeCombo",
      urlParamValue: pageSize,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplacePaginationParametersInUrl(languageCode, filterName, filterValue, sortingField, sortingDirection, selectedOption, FIRST_PAGE_NUMBER)
    }
  ];
  setCombosOnChangeCallback(comboOnChangeCallbackDataArray);
}

function treatLanguageComboAndUrlParameter(languageCodeElementValue) {
  //alert("treatLanguageComboAndUrlParameter(" + languageCodeElementValue + ")");

  const urlSearchParams = new URLSearchParams(window.location.search);

  // Set the languageParam URL param to the value from the URL param or the session or the model attribute or the default language or ENGLISH_LANGUAGE
  let languageCode = getParameterFromUrlSearchParams(urlSearchParams, "languageParam");
  if (!isValidLanguageCode(languageCode)) {
    languageCode = isValidLanguageCode(languageCodeElementValue) ? languageCodeElementValue : ENGLISH_LANGUAGE;
    addOrReplaceLanguageParamInUrl(languageCode);
  }

  //Set the combos onChange callback to replace the corresponding URL param with the selected value
  const comboOnChangeCallbackDataArray = [
    {
      comboElementId: "languageCombo",
      urlParamValue: languageCode,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplaceLanguageParamInUrl(selectedOption)
    }
  ];
  setCombosOnChangeCallback(comboOnChangeCallbackDataArray);
}

function treatJobCompanyLogoComboAndUrlParameter(jobCompanyLogoModelAttribute) {
  //alert("treatJobCompanyLogoComboAndUrlParameter(" + jobCompanyLogoModelAttribute + ")");

  const urlSearchParams = new URLSearchParams(window.location.search);

  // Set the jobCompanyLogo URL param if it does not exist to the jobCompanyLogo model attribute
  let jobCompanyLogo = getParameterFromUrlSearchParams(urlSearchParams, "jobCompanyLogo");
  if (jobCompanyLogo === null) {
    jobCompanyLogo = jobCompanyLogoModelAttribute;
    addOrReplaceJobCompanyLogoParameterInUrl(jobCompanyLogo);
  }

  //Set the combos onChange callback to replace the corresponding URL param with the selected value
  const comboOnChangeCallbackDataArray = [
    {
      comboElementId: "jobCompanyLogoCombo",
      urlParamValue: jobCompanyLogo,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplaceJobCompanyLogoParameterInUrl(selectedOption)
    }
  ];
  setCombosOnChangeCallback(comboOnChangeCallbackDataArray);
}

function resetPageNumberParameterInUrl() {
  //alert("resetPageNumberParameterInUrl()");

  const urlSearchParams = new URLSearchParams(window.location.search);
  const filterValueParam = getParameterFromUrlSearchParams(urlSearchParams, "filterValue");
  const pageNumberParam = getParameterFromUrlSearchParams(urlSearchParams, "pageNumber");

  const filterValueElem = getInputElementIfExists("filterValue");
  const filterValueElemCurrentVal = filterValueElem?.value ?? null;

  let resetDone = false;

  if (filterValueElemCurrentVal !== null) {
    if (filterValueParam !== filterValueElemCurrentVal) {
      addOrReplaceParameterInUrlSearchParams(urlSearchParams, "filterValue", filterValueElemCurrentVal);
      resetDone = true;
    }
  } else {
    deleteParameterInUrl(urlSearchParams, "filterValue");
    resetDone = true;
  }

  if (pageNumberParam !== FIRST_PAGE_NUMBER) {
    addOrReplaceParameterInUrlSearchParams(urlSearchParams, "pageNumber", FIRST_PAGE_NUMBER);
    resetDone = true;
  }

  if (resetDone) {
    reloadPage(urlSearchParams);
  }
  return resetDone;
}

function getHasAllPaginationParametersInUrl() {
  //alert("getHasAllPaginationParametersInUrl()");

  const urlSearchParams = new URLSearchParams(window.location.search);

  const languageCode = getParameterFromUrlSearchParams(urlSearchParams, "languageParam");
  if (!isValidLanguageCode(languageCode)) {
    return false;
  }

  const filterName = getParameterFromUrlSearchParams(urlSearchParams, "filterName");
  if (filterName === null) {
    return false;
  }

  const filterValue = getParameterFromUrlSearchParams(urlSearchParams, "filterValue");
  if (filterValue === null) {
    return false;
  }

  const sortingField = getParameterFromUrlSearchParams(urlSearchParams, "sortingField");
  if (!isValidStringField(sortingField)) {
    return false;
  }

  const sortingDirection = getParameterFromUrlSearchParams(urlSearchParams, "sortingDirection");
  if (!isValidStringField(sortingDirection)) {
    return false;
  }

  const pageSize = getParameterFromUrlSearchParams(urlSearchParams, "pageSize");
  if (!isValidStringField(pageSize)) {
    return false;
  }

  const pageNumber = getParameterFromUrlSearchParams(urlSearchParams, "pageNumber");
  if (!isValidStringField(pageNumber)) {
    return false;
  }

  return true;
}

//---------------------------------------------------------------------------------------------------------------------------

// Helper functions

function addOrReplaceLanguageParamInUrl(languageCode) {
  const urlSearchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "languageParam", languageCode);
  reloadPage(urlSearchParams);
}

function addOrReplaceJobCompanyLogoParameterInUrl(jobCompanyLogo) {
  const urlSearchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "jobCompanyLogo", jobCompanyLogo);
  reloadPage(urlSearchParams);
}

function addOrReplaceHomeParametersInUrl(languageCode, description, jobCategoryIdString) {
  const urlSearchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "languageParam", languageCode);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "description", description);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "jobCategoryId", jobCategoryIdString);
  reloadPage(urlSearchParams);
}

function addOrReplacePaginationParametersInUrl(languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber) {
  const urlSearchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "languageParam", languageCode);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "filterName", filterName);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "filterValue", filterValue);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "sortingField", sortingField);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "sortingDirection", sortingDirection);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "pageSize", pageSize);
  addOrReplaceParameterInUrlSearchParams(urlSearchParams, "pageNumber", pageNumber);
  reloadPage(urlSearchParams);
}

function deleteParameterInUrl(urlSearchParams, paramName) {
  urlSearchParams.delete(paramName);
}

function getParameterFromUrlSearchParams(urlSearchParams, paramName) {
  return urlSearchParams.get(paramName);
}

function addOrReplaceParameterInUrlSearchParams(urlSearchParams, paramName, paramValue) {
  const finalParamValue = paramValue ?? EMPTY_STRING;
  urlSearchParams.set(paramName, finalParamValue);
}

function reloadPage(urlSearchParams) {
  const urlQuery = urlSearchParams.toString();
  const urlQueryParams = urlQuery ? `?${urlQuery}` : EMPTY_STRING;
  const newUrl = `${window.location.pathname}${urlQueryParams}`;
  window.history.replaceState(null, EMPTY_STRING, newUrl);
  window.location.reload();
}

function setCombosOnChangeCallback(comboOnChangeCallbackDataArray) {
  const combosCallbackFunction = ({ comboElementId, urlParamValue, onChangeReplaceUrlParamCallback }) => {
    const comboElement = getComboElementIfExists(comboElementId);
    if (comboElement !== null) {
      //Set the value of the comboElement to the given urlParamValue (if the comboElement exists)
      comboElement.value = urlParamValue;

      //When the comboElement "selected value" changes, replaces the corresponding URL param with the selected value
      const onChangeEventListener = (event) => {
        const selectedOption = comboElement.value;
        onChangeReplaceUrlParamCallback(selectedOption);
      };
      updateOnChangeEventListener(comboElement, onChangeEventListener);
    }
  };
  comboOnChangeCallbackDataArray.forEach(combosCallbackFunction);
}

function updateOnChangeEventListener(comboElement, onChangeEventListener) {
  const comboElementId = comboElement.id;

  const existingListener = comboOnChangeEventListenerMap.get(comboElementId);
  if (existingListener) {
    comboElement.removeEventListener("change", existingListener);
  }

  comboElement.addEventListener("change", onChangeEventListener);
  comboOnChangeEventListenerMap.set(comboElementId, onChangeEventListener);
}

function isValidLanguageCode(languageCode) {
  const result = (isValidStringField(languageCode) && languageCode !== LANGUAGE_BY_DEFAULT);
  return result;
}

function isValidStringField(stringField) {
  const result = (isValidField(stringField) && stringField !== EMPTY_STRING);
  return result;
}

function isValidField(field) {
  const result = (field !== undefined && field !== null);
  return result;
}

function getComboElementIfExists(comboElementId) {
  const comboElement = getElementIfExists(comboElementId);
  const finalComboElement = comboElement instanceof HTMLSelectElement ? comboElement : null;
  return finalComboElement;
}

function getInputElementIfExists(inputElementId) {
  const inputElement = getElementIfExists(inputElementId);
  const finalInputElement = inputElement instanceof HTMLInputElement ? inputElement : null;
  return finalInputElement;
}

function getElementIfExists(elementId) {
  const element = document.getElementById(elementId);
  return element;
}

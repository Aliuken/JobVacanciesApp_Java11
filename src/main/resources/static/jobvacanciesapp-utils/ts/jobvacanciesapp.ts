// Custom types and constants

type Nullable<T> = T | null;
type Undefinable<T> = T | null | undefined;
type ConsumerFunction<T> = (t: T) => void;

const comboOnChangeEventListenerMapTS: Map<string, EventListener> = new Map<string, EventListener>();

interface ComboOnChangeCallbackData {
  comboElementId: string;
  urlParamValue: string;
  onChangeReplaceUrlParamCallback: (selectedOption: string) => void;
}

const DEFAULT_PAGE_SIZE_TS: string = "5";
const DEFAULT_PAGE_NUMBER_TS: string = "0";
const DEFAULT_SORTING_FIELD_TS: string = "id";
const DEFAULT_SORTING_DIRECTION_TS: string = "asc";
const EMPTY_STRING_TS: string = "";
const FIRST_PAGE_NUMBER_TS: string = "0";
const LANGUAGE_BY_DEFAULT_TS: string = "--";
const ENGLISH_LANGUAGE_TS: string = "en";

//---------------------------------------------------------------------------------------------------------------------------

// Main functions

function treatJobCompanyLogoComboWithAjaxTS(
  jobCompanyIdModelAttribute: Undefinable<number>,
  jobCompanyLogoModelAttribute: Undefinable<string>,
  refreshUrlModelAttribute: Undefinable<string>
): void {
  //alert("treatJobCompanyLogoComboWithAjaxTS(" + jobCompanyIdModelAttribute + ", " + jobCompanyLogoModelAttribute + ", " + refreshUrlModelAttribute + ")");

  const jobCompanyLogoCombo: Nullable<HTMLSelectElement> = getComboElementIfExistsTS("jobCompanyLogoCombo");
  if (jobCompanyLogoCombo !== null) {
    jobCompanyLogoCombo.value = jobCompanyLogoModelAttribute ?? EMPTY_STRING_TS;
    if (isValidStringFieldTS(refreshUrlModelAttribute)) {
      let jobCompanyLogoFragment: Nullable<HTMLElement> = getElementIfExistsTS("jobCompanyLogoFragment");
      if (jobCompanyLogoFragment !== null) {

        const onChangeEventListener: EventListener = (event) => {
          const selectedOption: string = jobCompanyLogoCombo.value;

          const urlSearchParams: URLSearchParams = new URLSearchParams();
          urlSearchParams.append("jobCompanyId", jobCompanyIdModelAttribute?.toString() ?? EMPTY_STRING_TS);
          urlSearchParams.append("jobCompanyLogo", selectedOption);

          const url: URL = new URL(refreshUrlModelAttribute!, window.location.href);
          url.search = urlSearchParams.toString();
          const urlString: string = url.toString();

          fetch(urlString)
            .then((response: Response) => {
              if (!response.ok) {
                throw new Error(`HTTP error ${response.status}`);
              }
              return response.text();
            })
            .then((result: string) => {
              if (jobCompanyLogoFragment !== null) {
                jobCompanyLogoFragment.outerHTML = result;
              }
              jobCompanyLogoFragment = getElementIfExistsTS("jobCompanyLogoFragment");
            })
            .catch((error: unknown) => {
              if (error instanceof Error) {
                console.error("Fetch error:", error.message);
              } else {
                console.error("Unknown error:", error);
              }
            });
        };
        updateOnChangeEventListenerTS(jobCompanyLogoCombo, onChangeEventListener);

      }
    }
  }
}

function getIsPageWithTableTS(): boolean {
  //alert("getIsPageWithTableTS()");

  const tableFilterAndPaginationForm: Nullable<HTMLElement> = getElementIfExistsTS("tableFilterAndPaginationForm")
  if (tableFilterAndPaginationForm !== null) {
    return true;
  }

  const tablePaginationNav: Nullable<HTMLElement> = getElementIfExistsTS("tablePaginationNav");
  if (tablePaginationNav !== null) {
    return true;
  }

  return false;
}

function getIsSearchJobVacanciesFromHomeTS(
  descriptionModelAttribute: Undefinable<string>,
  jobCategoryIdModelAttribute: Undefinable<number>
): boolean {
  //alert("getIsSearchJobVacanciesFromHomeTS(" + descriptionModelAttribute + ", " + jobCategoryIdModelAttribute + ")");

  const pagePath: string = window.location.pathname;
  //alert("pagePath: " + pagePath);

  if (pagePath === "/search") {
    const result: boolean = (isValidFieldTS(descriptionModelAttribute) || isValidFieldTS(jobCategoryIdModelAttribute));
    return result;
  }
  return false;
}

function treatSearchJobVacanciesFromHomeTS(
  languageSessionAttribute: Undefinable<string>,
  languageModelAttribute: Undefinable<string>,
  defaultLanguage: Undefinable<string>,
  descriptionModelAttribute: Undefinable<string>,
  jobCategoryIdModelAttribute: Undefinable<number>
): void {
  //alert("treatSearchJobVacanciesFromHomeTS(" + languageSessionAttribute + ", " + languageModelAttribute + ", " + defaultLanguage + ", " + descriptionModelAttribute + ", " + jobCategoryIdModelAttribute + ")");

  const urlSearchParams: URLSearchParams = new URLSearchParams(window.location.search);
  let addOrReplaceNeeded = false;

  //Set the languageParam URL param to the value from the URL param or the session or the model attribute or the default language or ENGLISH_LANGUAGE_TS
  let languageCode: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "languageParam");
  if (!isValidLanguageCodeTS(languageCode)) {
    languageCode = [languageSessionAttribute, languageModelAttribute, defaultLanguage].find(isValidLanguageCodeTS) ?? ENGLISH_LANGUAGE_TS;
    addOrReplaceNeeded = true;
  }

  //Set description URL param if it does not exist to the description model attribute (or EMPTY_STRING_TS if it is null)
  let description: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "description");
  if (description === null) {
    description = descriptionModelAttribute ?? EMPTY_STRING_TS;
    addOrReplaceNeeded = true;
  }

  //Set jobCategoryId URL param if it does not exist to the jobCategoryId model attribute (or EMPTY_STRING_TS if it is null)
  let jobCategoryIdString: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "jobCategoryId");
  if (jobCategoryIdString === null) {
    jobCategoryIdString = jobCategoryIdModelAttribute?.toString() ?? EMPTY_STRING_TS;
    addOrReplaceNeeded = true;
  }

  if (addOrReplaceNeeded) {
    addOrReplaceHomeParametersInUrlTS(languageCode!, description!, jobCategoryIdString!);
  }

  //Set the combos onChange callback to replace the corresponding URL param with the selected value
  const comboOnChangeCallbackDataArray: ComboOnChangeCallbackData[] = [
    {
      comboElementId: "languageCombo",
      urlParamValue: languageCode!,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplaceHomeParametersInUrlTS(selectedOption, description!, jobCategoryIdString!)
    },
    {
      comboElementId: "jobCategoryIdCombo",
      urlParamValue: jobCategoryIdString!,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplaceHomeParametersInUrlTS(languageCode!, description!, selectedOption)
    }
  ];
  setCombosOnChangeCallbackTS(comboOnChangeCallbackDataArray);
}

function treatFilterAndPaginationCombosAndUrlParametersTS(
  languageSessionAttribute: Undefinable<string>,
  languageModelAttribute: Undefinable<string>,
  defaultLanguage: Undefinable<string>,
  filterNameModelAttribute: Undefinable<string>,
  filterValueModelAttribute: Undefinable<string>,
  sortingFieldModelAttribute: Undefinable<string>,
  sortingDirectionModelAttribute: Undefinable<string>,
  pageSizeModelAttribute: Undefinable<string>,
  pageNumberModelAttribute: Undefinable<string>
): void {
  //alert("treatFilterAndPaginationCombosAndUrlParametersTS(" + languageSessionAttribute + ", " + languageModelAttribute + ", " + defaultLanguage + ", " + filterNameModelAttribute + ", " + filterValueModelAttribute + ", " + sortingFieldModelAttribute + ", " + sortingDirectionModelAttribute + ", " + pageSizeModelAttribute + ", " + pageNumberModelAttribute + ")");

  const urlSearchParams: URLSearchParams = new URLSearchParams(window.location.search);
  let addOrReplaceNeeded = false;

  //Set the languageParam URL param to the value from the URL param or the session or the model attribute or the default language or ENGLISH_LANGUAGE_TS
  let languageCode: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "languageParam");
  if (!isValidLanguageCodeTS(languageCode)) {
    languageCode = [languageSessionAttribute, languageModelAttribute, defaultLanguage].find(isValidLanguageCodeTS) ?? ENGLISH_LANGUAGE_TS;
    addOrReplaceNeeded = true;
  }

  //Set filterName URL param if it does not exist to the filterName model attribute (or EMPTY_STRING_TS if it is null)
  let filterName: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "filterName");
  if (filterName === null) {
    filterName = filterNameModelAttribute ?? EMPTY_STRING_TS;
    addOrReplaceNeeded = true;
  }

  //Set filterValue URL param if it does not exist to the filterValue model attribute (or EMPTY_STRING_TS if it is null)
  let filterValue: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "filterValue");
  if (filterValue === null) {
    filterValue = filterValueModelAttribute ?? EMPTY_STRING_TS;
    addOrReplaceNeeded = true;
  }

  //Set sortingField URL param if it does not exist to the sortingField model attribute (or DEFAULT_SORTING_FIELD_TS if it is null)
  let sortingField: Undefinable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "sortingField");
  if (!isValidStringFieldTS(sortingField)) {
    sortingField = isValidStringFieldTS(sortingFieldModelAttribute) ? sortingFieldModelAttribute : DEFAULT_SORTING_FIELD_TS;
    addOrReplaceNeeded = true;
  }

  //Set sortingDirection URL param if it does not exist to the sortingDirection model attribute (or DEFAULT_SORTING_DIRECTION_TS if it is null)
  let sortingDirection: Undefinable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "sortingDirection");
  if (!isValidStringFieldTS(sortingDirection)) {
    sortingDirection = isValidStringFieldTS(sortingDirectionModelAttribute) ? sortingDirectionModelAttribute : DEFAULT_SORTING_DIRECTION_TS;
    addOrReplaceNeeded = true;
  }

  //Set pageSize URL param if it does not exist to the pageSize model attribute (or DEFAULT_PAGE_SIZE_TS if it is null)
  let pageSize: Undefinable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "pageSize");
  if (!isValidStringFieldTS(pageSize)) {
    pageSize = isValidStringFieldTS(pageSizeModelAttribute) ? pageSizeModelAttribute : DEFAULT_PAGE_SIZE_TS;
    addOrReplaceNeeded = true;
  }

  //Set pageNumber URL param if it does not exist to the pageNumber model attribute (or DEFAULT_PAGE_NUMBER_TS if it is null)
  let pageNumber: Undefinable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "pageNumber");
  if (!isValidStringFieldTS(pageNumber)) {
    pageNumber = isValidStringFieldTS(pageNumberModelAttribute) ? pageNumberModelAttribute : DEFAULT_PAGE_NUMBER_TS;
    addOrReplaceNeeded = true;
  }

  if (addOrReplaceNeeded) {
    addOrReplacePaginationParametersInUrlTS(languageCode!, filterName!, filterValue!, sortingField!, sortingDirection!, pageSize!, pageNumber!);
  }

  //Set the combos onChange callback to replace the corresponding URL param with the selected value
  const comboOnChangeCallbackDataArray: ComboOnChangeCallbackData[] = [
    {
      comboElementId: "languageCombo",
      urlParamValue: languageCode!,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplacePaginationParametersInUrlTS(selectedOption, filterName!, filterValue!, sortingField!, sortingDirection!, pageSize!, FIRST_PAGE_NUMBER_TS)
    },
    {
      comboElementId: "tableFieldCombo",
      urlParamValue: filterName!,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplacePaginationParametersInUrlTS(languageCode!, selectedOption, filterValue!, sortingField!, sortingDirection!, pageSize!, FIRST_PAGE_NUMBER_TS)
    },
    {
      comboElementId: "sortingFieldCombo",
      urlParamValue: sortingField!,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplacePaginationParametersInUrlTS(languageCode!, filterName!, filterValue!, selectedOption, sortingDirection!, pageSize!, FIRST_PAGE_NUMBER_TS)
    },
    {
      comboElementId: "sortingDirectionCombo",
      urlParamValue: sortingDirection!,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplacePaginationParametersInUrlTS(languageCode!, filterName!, filterValue!, sortingField!, selectedOption, pageSize!, FIRST_PAGE_NUMBER_TS)
    },
    {
      comboElementId: "pageSizeCombo",
      urlParamValue: pageSize!,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplacePaginationParametersInUrlTS(languageCode!, filterName!, filterValue!, sortingField!, sortingDirection!, selectedOption, FIRST_PAGE_NUMBER_TS)
    }
  ];
  setCombosOnChangeCallbackTS(comboOnChangeCallbackDataArray);
}

function treatLanguageComboAndUrlParameterTS(
  languageSessionAttribute: Undefinable<string>,
  languageModelAttribute: Undefinable<string>,
  defaultLanguage: Undefinable<string>
): void {
  //alert("treatLanguageComboAndUrlParameterTS(" + languageSessionAttribute + ", " + languageModelAttribute + ", " + defaultLanguage + ")");

  const urlSearchParams: URLSearchParams = new URLSearchParams(window.location.search);

  // Set the languageParam URL param to the value from the URL param or the session or the model attribute or the default language or ENGLISH_LANGUAGE_TS
  let languageCode: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "languageParam");
  if (!isValidLanguageCodeTS(languageCode)) {
    languageCode = [languageSessionAttribute, languageModelAttribute, defaultLanguage].find(isValidLanguageCodeTS) ?? ENGLISH_LANGUAGE_TS;
    addOrReplaceLanguageParamInUrlTS(languageCode);
  }

  //Set the combos onChange callback to replace the corresponding URL param with the selected value
  const comboOnChangeCallbackDataArray: ComboOnChangeCallbackData[] = [
    {
      comboElementId: "languageCombo",
      urlParamValue: languageCode!,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplaceLanguageParamInUrlTS(selectedOption)
    }
  ];
  setCombosOnChangeCallbackTS(comboOnChangeCallbackDataArray);
}

function treatJobCompanyLogoComboAndUrlParameterTS(jobCompanyLogoModelAttribute: Undefinable<string>): void {
  //alert("treatJobCompanyLogoComboAndUrlParameterTS(" + jobCompanyLogoModelAttribute + ")");

  const urlSearchParams: URLSearchParams = new URLSearchParams(window.location.search);

  // Set the jobCompanyLogo URL param if it does not exist to the jobCompanyLogo model attribute
  let jobCompanyLogo: Undefinable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "jobCompanyLogo");
  if (jobCompanyLogo === null) {
    jobCompanyLogo = jobCompanyLogoModelAttribute;
    addOrReplaceJobCompanyLogoParameterInUrlTS(jobCompanyLogo);
  }

  //Set the combos onChange callback to replace the corresponding URL param with the selected value
  const comboOnChangeCallbackDataArray: ComboOnChangeCallbackData[] = [
    {
      comboElementId: "jobCompanyLogoCombo",
      urlParamValue: jobCompanyLogo!,
      onChangeReplaceUrlParamCallback: (selectedOption) => addOrReplaceJobCompanyLogoParameterInUrlTS(selectedOption)
    }
  ];
  setCombosOnChangeCallbackTS(comboOnChangeCallbackDataArray);
}

function resetPageNumberParameterInUrlTS(): boolean {
  //alert("resetPageNumberParameterInUrlTS()");

  const urlSearchParams: URLSearchParams = new URLSearchParams(window.location.search);
  const filterValueParam: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "filterValue");
  const pageNumberParam: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "pageNumber");

  const filterValueElem: Nullable<HTMLInputElement> = getInputElementIfExistsTS("filterValue");
  const filterValueElemCurrentVal: Nullable<string> = filterValueElem?.value ?? null;

  let resetDone: boolean = false;

  if (filterValueElemCurrentVal !== null) {
    if (filterValueParam !== filterValueElemCurrentVal) {
      addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "filterValue", filterValueElemCurrentVal);
      resetDone = true;
    }
  } else {
    deleteParameterInUrlTS(urlSearchParams, "filterValue");
    resetDone = true;
  }

  if (pageNumberParam !== FIRST_PAGE_NUMBER_TS) {
    addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "pageNumber", FIRST_PAGE_NUMBER_TS);
    resetDone = true;
  }

  if (resetDone) {
    reloadPageTS(urlSearchParams);
  }
  return resetDone;
}

function getHasAllPaginationParametersInUrlTS(): boolean {
  //alert("getHasAllPaginationParametersInUrlTS()");

  const urlSearchParams: URLSearchParams = new URLSearchParams(window.location.search);

  const languageCode: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "languageParam");
  if (!isValidLanguageCodeTS(languageCode)) {
    return false;
  }

  const filterName: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "filterName");
  if (filterName === null) {
    return false;
  }

  const filterValue: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "filterValue");
  if (filterValue === null) {
    return false;
  }

  const sortingField: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "sortingField");
  if (!isValidStringFieldTS(sortingField)) {
    return false;
  }

  const sortingDirection: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "sortingDirection");
  if (!isValidStringFieldTS(sortingDirection)) {
    return false;
  }

  const pageSize: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "pageSize");
  if (!isValidStringFieldTS(pageSize)) {
    return false;
  }

  const pageNumber: Nullable<string> = getParameterFromUrlSearchParamsTS(urlSearchParams, "pageNumber");
  if (!isValidStringFieldTS(pageNumber)) {
    return false;
  }

  return true;
}

//---------------------------------------------------------------------------------------------------------------------------

// Helper functions

function addOrReplaceLanguageParamInUrlTS(languageCode: Undefinable<string>): void {
  const urlSearchParams: URLSearchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "languageParam", languageCode);
  reloadPageTS(urlSearchParams);
}

function addOrReplaceJobCompanyLogoParameterInUrlTS(jobCompanyLogo: Undefinable<string>): void {
  const urlSearchParams: URLSearchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "jobCompanyLogo", jobCompanyLogo);
  reloadPageTS(urlSearchParams);
}

function addOrReplaceHomeParametersInUrlTS(
  languageCode: Undefinable<string>,
  description: Undefinable<string>,
  jobCategoryIdString: Undefinable<string>
): void {
  const urlSearchParams: URLSearchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "languageParam", languageCode);
  addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "description", description);
  addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "jobCategoryId", jobCategoryIdString);
  reloadPageTS(urlSearchParams);
}

function addOrReplacePaginationParametersInUrlTS(
  languageCode: Undefinable<string>,
  filterName: Undefinable<string>,
  filterValue: Undefinable<string>,
  sortingField: Undefinable<string>,
  sortingDirection: Undefinable<string>,
  pageSize: Undefinable<string>,
  pageNumber: Undefinable<string>
): void {
  const urlSearchParams: URLSearchParams = new URLSearchParams(window.location.search);
  addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "languageParam", languageCode);
  addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "filterName", filterName);
  addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "filterValue", filterValue);
  addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "sortingField", sortingField);
  addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "sortingDirection", sortingDirection);
  addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "pageSize", pageSize);
  addOrReplaceParameterInUrlSearchParamsTS(urlSearchParams, "pageNumber", pageNumber);
  reloadPageTS(urlSearchParams);
}

function deleteParameterInUrlTS(urlSearchParams: URLSearchParams, paramName: string): void {
  urlSearchParams.delete(paramName);
}

function getParameterFromUrlSearchParamsTS(
  urlSearchParams: URLSearchParams,
  paramName: string
): Nullable<string> {
  return urlSearchParams.get(paramName);
}

function addOrReplaceParameterInUrlSearchParamsTS(
  urlSearchParams: URLSearchParams,
  paramName: string,
  paramValue: Undefinable<string>
): void {
  const finalParamValue = paramValue ?? EMPTY_STRING_TS;
  urlSearchParams.set(paramName, finalParamValue);
}

function reloadPageTS(urlSearchParams: URLSearchParams): void {
  const urlQuery: string = urlSearchParams.toString();
  const urlQueryParams: string = urlQuery ? `?${urlQuery}` : EMPTY_STRING_TS;
  const newUrl = `${window.location.pathname}${urlQueryParams}`;
  window.history.replaceState(null, EMPTY_STRING_TS, newUrl);
  window.location.reload();
}

function setCombosOnChangeCallbackTS(comboOnChangeCallbackDataArray: ComboOnChangeCallbackData[]): void {
  const combosCallbackFunction: ConsumerFunction<ComboOnChangeCallbackData> = ({comboElementId, urlParamValue, onChangeReplaceUrlParamCallback}: ComboOnChangeCallbackData) => {
    const comboElement: Nullable<HTMLSelectElement> = getComboElementIfExistsTS(comboElementId);
    if (comboElement !== null) {
      //Set the value of the comboElement to the given urlParamValue (if the comboElement exists)
      comboElement.value = urlParamValue;

      //When the comboElement "selected value" changes, replaces the corresponding URL param with the selected value
      const onChangeEventListener: EventListener = (event) => {
        const selectedOption: string = comboElement.value;
        onChangeReplaceUrlParamCallback(selectedOption);
      };
      updateOnChangeEventListenerTS(comboElement!, onChangeEventListener);
    }
  };
  comboOnChangeCallbackDataArray.forEach(combosCallbackFunction);
}

function updateOnChangeEventListenerTS(
  comboElement: HTMLSelectElement,
  onChangeEventListener: EventListener
): void {
  const comboElementId: string = comboElement.id;

  const existingListener: Undefinable<EventListener> = comboOnChangeEventListenerMapTS.get(comboElementId);
  if (existingListener) {
    comboElement.removeEventListener("change", existingListener);
  }

  comboElement.addEventListener("change", onChangeEventListener);
  comboOnChangeEventListenerMapTS.set(comboElementId, onChangeEventListener);
}

function isValidLanguageCodeTS(languageCode: Undefinable<string>): boolean {
  const result: boolean = (isValidStringFieldTS(languageCode) && languageCode !== LANGUAGE_BY_DEFAULT_TS);
  return result;
}

function isValidStringFieldTS(stringField: Undefinable<string>): boolean {
  const result: boolean = (isValidFieldTS(stringField) && stringField !== EMPTY_STRING_TS);
  return result;
}

function isValidFieldTS(field: unknown): boolean {
  const result: boolean = (field !== undefined && field !== null);
  return result;
}

function getComboElementIfExistsTS(comboElementId: string): Nullable<HTMLSelectElement> {
  const comboElement: Nullable<HTMLElement> = getElementIfExistsTS(comboElementId);
  const finalComboElement: Nullable<HTMLSelectElement> = comboElement instanceof HTMLSelectElement ? comboElement : null;
  return finalComboElement;
}

function getInputElementIfExistsTS(inputElementId: string): Nullable<HTMLInputElement> {
  const inputElement: Nullable<HTMLElement> = getElementIfExistsTS(inputElementId);
  const finalInputElement: Nullable<HTMLInputElement> = inputElement instanceof HTMLInputElement ? inputElement : null;
  return finalInputElement;
}

function getElementIfExistsTS(elementId: string): Nullable<HTMLElement> {
  const element: Nullable<HTMLElement> = document.getElementById(elementId);
  return element;
}

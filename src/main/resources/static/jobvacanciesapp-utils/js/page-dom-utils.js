function getIsPageWithTable() {
  let tableFilterAndPaginationForm = document.getElementById("tableFilterAndPaginationForm");
  if(tableFilterAndPaginationForm != null) {
    return true;
  }

  let tablePaginationNav = document.getElementById("tablePaginationNav");
  if(tablePaginationNav != null) {
    return true;
  }

  return false;
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

function getElementIfExists(elementId) {
  let element = $('#' + elementId);
  if(element !== undefined) {
    return element;
  }
  return null;
};
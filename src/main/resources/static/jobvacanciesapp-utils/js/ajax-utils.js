function treatJobCompanyLogoComboWithAjax(jobCompanyIdModelAttribute, jobCompanyLogoModelAttribute, refreshUrlModelAttribute) {
  //alert("treatJobCompanyLogoComboWithAjax(" + jobCompanyIdModelAttribute + ", " + jobCompanyLogoModelAttribute + ", " + refreshUrlModelAttribute + ")");

  let jobCompanyLogoCombo = getComboElementIfExists("jobCompanyLogoCombo");
  if(jobCompanyLogoCombo != null) {
    let jobCompanyLogoFragment = getElementIfExists("jobCompanyLogoFragment");
    if(jobCompanyLogoFragment != null) {
      jobCompanyLogoCombo.change(function () {
        let selectedOption = jobCompanyLogoCombo.val();

        $.get({
          url: refreshUrlModelAttribute,
          data: {jobCompanyId: jobCompanyIdModelAttribute, jobCompanyLogo: selectedOption}
        }).then(function(result) {
          jobCompanyLogoFragment.replaceWith(result);
          jobCompanyLogoFragment = getElementIfExists("jobCompanyLogoFragment");
        });
      });
    }
    jobCompanyLogoCombo.val(jobCompanyLogoModelAttribute);
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

function getElementIfExists(elementId) {
  let element = $('#' + elementId);
  if(element !== undefined) {
    return element;
  }
  return null;
};
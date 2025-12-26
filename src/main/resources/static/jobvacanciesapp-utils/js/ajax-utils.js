function treatJobCompanyLogoComboWithAjax(jobCompanyIdModelAttribute, jobCompanyLogoModelAttribute, refreshUrlModelAttribute) {
  //alert("treatJobCompanyLogoComboWithAjax(" + jobCompanyIdModelAttribute + ", " + jobCompanyLogoModelAttribute + ", " + refreshUrlModelAttribute + ")");

  const jobCompanyLogoCombo = getComboElementIfExists("jobCompanyLogoCombo");
  if (jobCompanyLogoCombo !== null) {
    jobCompanyLogoCombo.value = jobCompanyLogoModelAttribute ?? EMPTY_STRING;
    if (isValidStringField(refreshUrlModelAttribute)) {
      let jobCompanyLogoFragment = getElementIfExists("jobCompanyLogoFragment");
      if (jobCompanyLogoFragment !== null) {

        const onChangeEventListener = (event) => {
          const selectedOption = jobCompanyLogoCombo.value;

          const urlSearchParams = new URLSearchParams();
          urlSearchParams.append("jobCompanyId", jobCompanyIdModelAttribute?.toString() ?? EMPTY_STRING);
          urlSearchParams.append("jobCompanyLogo", selectedOption);

          const url = new URL(refreshUrlModelAttribute, window.location.href);
          url.search = urlSearchParams.toString();
          const urlString = url.toString();

          fetch(urlString)
            .then((response) => {
              if (!response.ok) {
                throw new Error(`HTTP error ${response.status}`);
              }
              return response.text(); // HTML text (no XML nor JSON are used)
            })
            .then((result) => {
              if (jobCompanyLogoFragment !== null) {
                jobCompanyLogoFragment.outerHTML = result; // The HTML text is inserted directly into the DOM
              }
              jobCompanyLogoFragment = getElementIfExists("jobCompanyLogoFragment");
            })
            .catch((error) => {
              if (error instanceof Error) {
                console.error("Fetch error:", error.message);
              } else {
                console.error("Unknown error:", error);
              }
            });
        };
        updateOnChangeEventListener(jobCompanyLogoCombo, onChangeEventListener);

      }
    }
  }
}

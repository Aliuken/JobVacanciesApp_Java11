function calendarInputUIElement(calendarInputUIElementId) {
  $("#" + calendarInputUIElementId).datetimepicker({
    dateFormat: 'dd-mm-yy',
    timeFormat: 'HH:mm:ss',
    showSecond:false,
    showMillisec:false,
    showMicrosec:false,
    showTimezone:false
  });
}
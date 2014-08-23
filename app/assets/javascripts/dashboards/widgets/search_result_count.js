function addWidget_search_result_count(dashboardId, description, eventElem) {
    var params = originalUniversalSearchSettings();
    params.widgetType = "SEARCH_RESULT_COUNT";

    addWidget(dashboardId, description, params);
}

function updateWidget_search_result_count(widget, data) {
    $(".value", widget).text(numeral(data.result).format());
}
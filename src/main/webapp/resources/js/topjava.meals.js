const mealAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl
};

function updateTable() {
    $.ajax({
        type: "GET",
        url: mealAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function resetFilters() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                { "data": "dateTime" },
                { "data": "description" },
                { "data": "calories" },
                { "defaultContent": "Edit", "orderable": false },
                { "defaultContent": "Delete", "orderable": false }
            ],
            "order": [[ 0, "desc" ]]
        })
    );

    $("#filter-button").on("click", function (event) {
        event.preventDefault();
        updateTable();
    });
});


const mealAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function updateTableByData(data) {
    $('#datatable').DataTable().clear().rows.add(data).draw();
}

function resetFilters() {
    document.getElementById("filter").reset();
    ctx.updateTable();
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
        ctx.updateTable();
    });
});


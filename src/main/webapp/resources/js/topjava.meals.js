const mealAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTableFiltered: function () {
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
    ctx.updateTableFiltered();
}

function save() {
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        ctx.updateTableFiltered();
        successNoty("Saved");
    });
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
        ctx.updateTableFiltered();
    });
});


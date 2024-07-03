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
});

function deleteRow(id) {
    if (confirm('Are you sure?')) {
        $.ajax({
            url: ctx.ajaxUrl + id,
            type: "DELETE"
        }).done(function () {
            updateTable();
            successNoty("Deleted");
        });
    }
}


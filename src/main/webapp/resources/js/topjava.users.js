const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable: updateTable
};

function updateTable() {
    $.get(userAjaxUrl, updateTableByData);
}

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function toggle(toggle, id) {
    const toggled = toggle.is(":checked");
    $.ajax({
        url: ctx.ajaxUrl + id,
        type: "POST",
        data: {enabled: toggled},
        success: function () {
            const row = toggle.closest("tr");
            row.toggleClass("inactive-user", !toggled);
            successNoty(toggled ? "Toggled On" : "Toggled Off");
        },
        error: function () {
            $(toggle).prop("checked", !toggled);
            failNoty("Failed to change status");
        }
    });
}

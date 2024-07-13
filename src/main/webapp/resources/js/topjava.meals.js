const mealAjaxUrl = "profile/meals/";

$('#startDate, #endDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d',
    formatDate: 'Y-m-d'
});

$('#startTime, #endTime').datetimepicker({
    datepicker: false,
    format: 'H:i',
    step: 15
});

// https://stackoverflow.com/a/5064235/548473
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

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return formatDateTime(data);
                        }
                        return data;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [0, "desc"]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );

    initializeDateTimePickersForMeals();
});

function formatDateTime(dateTime) {
    return moment(dateTime).format('YYYY-MM-DD HH:mm');
}

function initializeDateTimePickersForMeals() {
    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    });

    $('#editRow').on('show.bs.modal', function () {
        $('#dateTime').datetimepicker('reset');
    });
}
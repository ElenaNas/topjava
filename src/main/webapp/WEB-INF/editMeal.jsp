<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Meal</title>
    <style>
        table {
            width: 80%;
            border-collapse: collapse;
            border: 1px solid #ddd;
        }

        th, td {
            padding: 10px;
            text-align: left;
            border: 1px solid #ddd;
        }

        .button-container {
            margin-top: 20px;
        }

        .button-container input {
            margin-right: 10px;
        }

        input[type="text"].long-description {
            width: 300px;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>

<% if ("edit".equals(request.getParameter("action")))
{ %><h2>Edit Meal</h2><% }
else { %><h2>Add Meal</h2><% } %>

<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="action" value="edit">
    <input type="hidden" name="id" value="${meal.id}">
    <label for="dateTime">Date and Time:</label>
    <input type="datetime-local" id="dateTime" name="dateTime" value="${meal.dateTime}"><br>
    <label for="description">Description:</label>
    <input type="text" id="description" name="description" value="${meal.description}" class="long-description"><br>
    <label for="calories">Calories:</label>
    <input type="number" id="calories" name="calories" value="${meal.calories}" min="0" step="1"><br>

    <div class="button-container">
        <input type="submit" value="Save">
        <input type="button" value="Cancel" onclick="window.history.back();">
    </div>
</form>
</body>
</html>



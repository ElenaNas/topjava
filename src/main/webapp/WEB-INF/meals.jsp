<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        .red {
            color: red;
        }

        .green {
            color: green;
        }

        table {
            width: 80%;
            border-collapse: collapse;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        .action-links {
            display: inline-block;
            margin-right: 10px;
        }

        .add-meal-link {
            display: inline-block;
            margin-bottom: 20px;
            margin-right: 20px;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>All The Meals</h2>
<div class="add-meal-link">
    <a href="meals?action=add">Add Meal</a>
</div>
<table border="1">
    <tr>
        <th>Date Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Update</th>
        <th>Delete</th>
    </tr>
    <c:forEach var="meal" items="${meals}">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr>
            <td class="${meal.excess ? 'red' : 'green'}">${TimeUtil.formatDateTime(meal.dateTime)}</td>
            <td class="${meal.excess ? 'red' : 'green'}">${meal.description}</td>
            <td class="${meal.excess ? 'red' : 'green'}">${meal.calories}</td>
            <td>
                <div class="action-links">
                    <a href="meals?action=edit&id=${meal.id}">Update</a>
                </div>
            </td>
            <td>
                <div class="action-links">
                    <a href="meals?action=delete&id=${meal.id}">Delete</a>
                </div>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>


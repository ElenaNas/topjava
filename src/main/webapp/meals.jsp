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
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border="1">
    <tr>
        <th>Date Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Excess</th>
    </tr>
    <c:forEach var="meal" items="${meals}">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr>
            <td class="${meal.excess ? 'red' : 'green'}">${TimeUtil.formatDateTime(meal.dateTime)}</td>
            <td class="${meal.excess ? 'red' : 'green'}">${meal.description}</td>
            <td class="${meal.excess ? 'red' : 'green'}">${meal.calories}</td>
            <td class="${meal.excess ? 'red' : 'green'}">${meal.excess}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
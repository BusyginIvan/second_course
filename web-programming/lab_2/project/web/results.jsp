<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:useBean id="results" class="data.Results" scope="session"/>

<% if (results.isEmpty()) { %>
    <h4><span class="notification">Результаты отсутствуют</span></h4>
<% } else { %>
    <table id='outputTable'>
        <thead><tr> <th>x</th> <th>y</th> <th>r</th> <th>Точка входит в область</th> </tr></thead>
        <tbody><c:forEach var="point" items="${results.pointList}">
            <tr>
                <td>${point.format(point.x)}</td>
                <td>${point.format(point.y)}</td>
                <td>${point.format(point.r)}</td>
                <td>${point.result}</td>
            </tr>
        </c:forEach></tbody>
    </table>
<% } %>
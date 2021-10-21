<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:useBean id="results" class="data.Results" scope="session"/>

<% if (results.isEmpty()) { %>
    <h4><span class="notification">Результаты отсутствуют</span></h4>
<% } else { %>
    <table id='outputTable'>
        <thead><tr> <th>x</th> <th>y</th> <th>r</th> <th>Точка входит в область</th> </tr></thead>
        <tbody><c:forEach var="point" items="${results.pointList}">
            <tr>
                <td><fmt:formatNumber pattern="###.###" value="${point.x}"/></td>
                <td><fmt:formatNumber pattern="###.###" value="${point.y}"/></td>
                <td><fmt:formatNumber pattern="###.###" value="${point.r}"/></td>
                <td style='color: ${point.result ? "green" : "red"}'>${point.result ? "да" : "нет"}</td>
            </tr>
        </c:forEach></tbody>
    </table>
<% } %>
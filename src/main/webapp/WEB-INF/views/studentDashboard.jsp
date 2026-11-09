<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="topbar">
    <strong>Campus Maintenance Reporting System</strong>
    <div>
        Logged in as ${sessionScope.studentNumber} (Student)
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</div>
<div class="container">
    <h1>My Reported Faults</h1>

    <a href="${pageContext.request.contextPath}/student/report/new"><button type="button">Report a New Fault</button></a>

    <c:choose>
        <c:when test="${empty reports}">
            <p>You have not reported any faults yet.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Category</th>
                    <th>Location</th>
                    <th>Priority</th>
                    <th>Status</th>
                    <th></th>
                </tr>
                <c:forEach var="r" items="${reports}">
                    <tr>
                        <td>${r.id}</td>
                        <td>${r.category}</td>
                        <td>${r.location}</td>
                        <td>${r.priority}</td>
                        <td><span class="badge">${r.status.displayName}</span></td>
                        <td><a href="${pageContext.request.contextPath}/report?id=${r.id}">View</a></td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>

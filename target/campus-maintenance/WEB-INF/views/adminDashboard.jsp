<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Administrator Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="topbar">
    <strong>Campus Maintenance Reporting System</strong>
    <div>
        Logged in as admin (Administrator)
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</div>
<div class="container">
    <h1>All Reported Faults</h1>

    <form action="${pageContext.request.contextPath}/admin/dashboard" method="get">
        <label for="status">Filter by status</label>
        <select id="status" name="status" onchange="this.form.submit()">
            <option value="">-- All --</option>
            <c:forEach var="s" items="${statuses}">
                <option value="${s.displayName}" ${selectedStatus == s.displayName ? 'selected' : ''}>${s.displayName}</option>
            </c:forEach>
        </select>
    </form>

    <c:choose>
        <c:when test="${empty reports}">
            <p>No reports match this filter.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Student</th>
                    <th>Category</th>
                    <th>Location</th>
                    <th>Priority</th>
                    <th>Status</th>
                    <th>Assigned To</th>
                    <th></th>
                </tr>
                <c:forEach var="r" items="${reports}">
                    <tr>
                        <td>${r.id}</td>
                        <td>${r.studentNumber}</td>
                        <td>${r.category}</td>
                        <td>${r.location}</td>
                        <td>${r.priority}</td>
                        <td><span class="badge">${r.status.displayName}</span></td>
                        <td>${r.assignedTo}</td>
                        <td><a href="${pageContext.request.contextPath}/report?id=${r.id}">View / Update</a></td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>

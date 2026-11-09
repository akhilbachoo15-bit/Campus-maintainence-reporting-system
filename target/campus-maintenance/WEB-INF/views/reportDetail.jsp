<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Report Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="topbar">
    <strong>Campus Maintenance Reporting System</strong>
    <div>
        <c:choose>
            <c:when test="${sessionScope.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/student/dashboard">Dashboard</a>
            </c:otherwise>
        </c:choose>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</div>
<div class="container">
    <h1>Report #${report.id}</h1>

    <c:if test="${not empty errorMessage}">
        <div class="error">${errorMessage}</div>
    </c:if>

    <p><strong>Student:</strong> ${report.studentNumber}</p>
    <p><strong>Category:</strong> ${report.category}</p>
    <p><strong>Location:</strong> ${report.location}</p>
    <p><strong>Description:</strong> ${report.description}</p>
    <p><strong>Priority:</strong> ${report.priority}</p>
    <p><strong>Status:</strong> <span class="badge">${report.status.displayName}</span></p>
    <p><strong>Assigned To:</strong> ${empty report.assignedTo ? 'Not yet assigned' : report.assignedTo}</p>

    <c:if test="${report.hasEvidence()}">
        <p><strong>Evidence:</strong>
            <a href="${pageContext.request.contextPath}/evidence?id=${report.id}" target="_blank">${report.evidenceOriginalName}</a>
        </p>
    </c:if>

    <h2>Feedback History</h2>
    <c:choose>
        <c:when test="${empty feedbackList}">
            <p>No feedback yet.</p>
        </c:when>
        <c:otherwise>
            <ul>
                <c:forEach var="f" items="${feedbackList}">
                    <li>${f.date} - ${f.author}: ${f.message}</li>
                </c:forEach>
            </ul>
        </c:otherwise>
    </c:choose>

    <c:if test="${sessionScope.role == 'ADMIN'}">
        <h2>Update This Report</h2>
        <form action="${pageContext.request.contextPath}/admin/report/update" method="post">
            <input type="hidden" name="id" value="${report.id}">

            <label for="status">New Status</label>
            <select id="status" name="status">
                <option value="Reported">Reported</option>
                <option value="Assigned">Assigned</option>
                <option value="In Progress">In Progress</option>
                <option value="Resolved">Resolved</option>
                <option value="Closed">Closed</option>
            </select>

            <label for="assignedTo">Assign To</label>
            <input type="text" id="assignedTo" name="assignedTo" value="${report.assignedTo}">

            <label for="message">Feedback Message</label>
            <textarea id="message" name="message" rows="3"></textarea>

            <input type="submit" value="Update Report">
        </form>
    </c:if>
</div>
</body>
</html>

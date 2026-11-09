<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Report a Fault</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="topbar">
    <strong>Campus Maintenance Reporting System</strong>
    <div>
        ${sessionScope.studentNumber}
        <a href="${pageContext.request.contextPath}/student/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</div>
<div class="container">
    <h1>Report a New Fault</h1>

    <c:if test="${not empty errorMessage}">
        <div class="error">${errorMessage}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/student/report/new" method="post" enctype="multipart/form-data">
        <label for="category">Category</label>
        <select id="category" name="category" required>
            <option value="Electrical">Electrical</option>
            <option value="Plumbing">Plumbing</option>
            <option value="IT">IT</option>
            <option value="Furniture">Furniture</option>
            <option value="Other">Other</option>
        </select>

        <label for="location">Location</label>
        <input type="text" id="location" name="location" placeholder="e.g. Block C, Room 12" required>

        <label for="description">Description</label>
        <textarea id="description" name="description" rows="4" required></textarea>

        <label for="priority">Priority</label>
        <select id="priority" name="priority" required>
            <option value="Low">Low</option>
            <option value="Medium">Medium</option>
            <option value="High">High</option>
        </select>

        <label for="evidence">Evidence (optional, JPG/PNG/PDF, max 2 MB)</label>
        <input type="file" id="evidence" name="evidence" accept=".jpg,.jpeg,.png,.pdf">

        <input type="submit" value="Submit Report">
    </form>
</div>
</body>
</html>

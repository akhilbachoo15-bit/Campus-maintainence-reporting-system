<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - Campus Maintenance Reporting System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="topbar">
    <strong>Campus Maintenance Reporting System</strong>
    <div><a href="${pageContext.request.contextPath}/login.jsp">Login</a></div>
</div>
<div class="container">
    <h1>Student Registration</h1>

    <c:if test="${not empty errorMessage}">
        <div class="error">${errorMessage}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <label for="studentNumber">Student Number (6-12 digits)</label>
        <input type="text" id="studentNumber" name="studentNumber" required>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" required>

        <label for="password">Password (at least 8 characters)</label>
        <input type="password" id="password" name="password" required minlength="8">

        <input type="submit" value="Register">
    </form>

    <p>Already have an account? <a href="${pageContext.request.contextPath}/login.jsp">Login here</a></p>
</div>
</body>
</html>

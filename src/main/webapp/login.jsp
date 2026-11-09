<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Campus Maintenance Reporting System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="topbar">
    <strong>Campus Maintenance Reporting System</strong>
    <div><a href="${pageContext.request.contextPath}/register.jsp">Register</a></div>
</div>
<div class="container">
    <h1>Login</h1>

    <c:if test="${param.registered == 'true'}">
        <div class="success">Registration successful. You can now log in.</div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="error">${errorMessage}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="studentNumber">Student Number (or "admin")</label>
        <input type="text" id="studentNumber" name="studentNumber" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>

        <input type="submit" value="Login">
    </form>

    <p>New student? <a href="${pageContext.request.contextPath}/register.jsp">Register here</a></p>
</div>
</body>
</html>

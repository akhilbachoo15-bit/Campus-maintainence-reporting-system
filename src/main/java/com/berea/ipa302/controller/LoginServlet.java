package com.berea.ipa302.controller;

import com.berea.ipa302.AppInitListener;
import com.berea.ipa302.model.Role;
import com.berea.ipa302.model.User;
import com.berea.ipa302.repository.UserRepository;
import com.berea.ipa302.service.PasswordUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles logging in for both students and the fixed admin account.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentNumber = request.getParameter("studentNumber");
        String password = request.getParameter("password");

        UserRepository userRepository =
                (UserRepository) getServletContext().getAttribute(AppInitListener.USER_REPO_KEY);

        // Null-safe credential check - never call .equals() on something
        // that might be null.
        User user = (studentNumber == null) ? null : userRepository.findByStudentNumber(studentNumber.trim());

        boolean credentialsOk = user != null && password != null
                && PasswordUtil.matches(password, user.getPasswordHash());

        if (!credentialsOk) {
            request.setAttribute("errorMessage", "Incorrect student number or password.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Reuse the existing session if there is one, otherwise create one.
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionUtil.SESSION_STUDENT_NUMBER, user.getStudentNumber());
        session.setAttribute(SessionUtil.SESSION_ROLE, user.getRole());

        // Non-sensitive preference cookie: how many reports to show per
        // page on the dashboard. Only set it the first time so we do not
        // overwrite a preference the user already changed.
        boolean hasPreferenceCookie = false;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("listSize".equals(cookie.getName())) {
                    hasPreferenceCookie = true;
                }
            }
        }
        if (!hasPreferenceCookie) {
            Cookie listSizeCookie = new Cookie("listSize", "10");
            listSizeCookie.setPath(request.getContextPath() + "/");
            listSizeCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
            response.addCookie(listSizeCookie);
        }

        if (user.getRole() == Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
        }
    }
}

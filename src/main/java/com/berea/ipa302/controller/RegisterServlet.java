package com.berea.ipa302.controller;

import com.berea.ipa302.AppInitListener;
import com.berea.ipa302.model.Role;
import com.berea.ipa302.model.User;
import com.berea.ipa302.repository.UserRepository;
import com.berea.ipa302.service.PasswordUtil;
import com.berea.ipa302.service.ValidationService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles new student registrations.
 * GET  -> just shows the registration form (register.jsp is a public page)
 * POST -> validates the form and creates the new student account
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentNumber = request.getParameter("studentNumber");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserRepository userRepository =
                (UserRepository) getServletContext().getAttribute(AppInitListener.USER_REPO_KEY);

        // Server-side validation - never trust the browser alone.
        if (!ValidationService.isValidStudentNumber(studentNumber)) {
            showError(request, response, "Student number must be 6 to 12 digits.");
            return;
        }
        if (!ValidationService.isValidEmail(email)) {
            showError(request, response, "Please enter a valid email address.");
            return;
        }
        if (!ValidationService.isValidPassword(password)) {
            showError(request, response, "Password must be at least 8 characters.");
            return;
        }
        if (userRepository.existsByStudentNumber(studentNumber)) {
            showError(request, response, "That student number is already registered.");
            return;
        }
        if (userRepository.existsByEmail(email)) {
            showError(request, response, "That email address is already registered.");
            return;
        }

        User user = new User();
        user.setStudentNumber(studentNumber.trim());
        user.setEmail(email.trim());
        user.setPasswordHash(PasswordUtil.hash(password));
        user.setRole(Role.STUDENT);
        userRepository.save(user);

        // Post/Redirect/Get: after a successful POST we redirect so a page
        // refresh does not create the account twice.
        response.sendRedirect(request.getContextPath() + "/login.jsp?registered=true");
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
        dispatcher.forward(request, response);
    }
}

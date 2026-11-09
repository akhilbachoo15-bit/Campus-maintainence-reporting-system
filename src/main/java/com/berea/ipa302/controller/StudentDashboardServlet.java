package com.berea.ipa302.controller;

import com.berea.ipa302.AppInitListener;
import com.berea.ipa302.model.MaintenanceReport;
import com.berea.ipa302.repository.ReportRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Shows a student their own reports and the current status/feedback
 * on each one.
 */
@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtil.isLoggedIn(request) || !SessionUtil.isStudent(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String studentNumber = SessionUtil.getStudentNumber(request);

        ReportRepository reportRepository =
                (ReportRepository) getServletContext().getAttribute(AppInitListener.REPORT_REPO_KEY);

        List<MaintenanceReport> myReports = reportRepository.findByStudentNumber(studentNumber);

        request.setAttribute("reports", myReports);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/studentDashboard.jsp");
        dispatcher.forward(request, response);
    }
}

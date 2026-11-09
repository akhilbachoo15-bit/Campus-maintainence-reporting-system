package com.berea.ipa302.controller;

import com.berea.ipa302.AppInitListener;
import com.berea.ipa302.model.Feedback;
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
 * Shows the full detail of one report, including all admin feedback.
 * A student may only open their own report. An admin may open any report.
 */
@WebServlet("/report")
public class ReportDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int reportId;
        try {
            reportId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid report id.");
            return;
        }

        ReportRepository reportRepository =
                (ReportRepository) getServletContext().getAttribute(AppInitListener.REPORT_REPO_KEY);
        MaintenanceReport report = reportRepository.findById(reportId);

        if (report == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Report not found.");
            return;
        }

        // Ownership rule: a student may only see their own report.
        boolean isOwner = report.getStudentNumber().equals(SessionUtil.getStudentNumber(request));
        if (!SessionUtil.isAdmin(request) && !isOwner) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You may not view this report.");
            return;
        }

        List<Feedback> feedback = reportRepository.getFeedback(reportId);

        request.setAttribute("report", report);
        request.setAttribute("feedbackList", feedback);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/reportDetail.jsp");
        dispatcher.forward(request, response);
    }
}

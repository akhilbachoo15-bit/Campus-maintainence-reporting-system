package com.berea.ipa302.controller;

import com.berea.ipa302.AppInitListener;
import com.berea.ipa302.model.MaintenanceReport;
import com.berea.ipa302.model.ReportStatus;
import com.berea.ipa302.repository.ReportRepository;
import com.berea.ipa302.service.ReportService;
import com.berea.ipa302.service.ValidationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Lets the administrator assign a report, add a feedback message and
 * move it to its next status. Only reachable by an admin.
 */
@WebServlet("/admin/report/update")
public class UpdateReportServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtil.isLoggedIn(request) || !SessionUtil.isAdmin(request)) {
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

        String assignedTo = request.getParameter("assignedTo");
        String newStatusText = request.getParameter("status");
        String message = request.getParameter("message");

        ReportStatus newStatus = ReportStatus.fromText(newStatusText);

        if (newStatus == null || !ReportService.isValidTransition(report.getStatus(), newStatus)) {
            request.setAttribute("errorMessage", "That status change is not allowed.");
            request.setAttribute("report", report);
            request.setAttribute("feedbackList", reportRepository.getFeedback(reportId));
            request.getRequestDispatcher("/WEB-INF/views/reportDetail.jsp").forward(request, response);
            return;
        }

        if (ValidationService.isNotBlank(assignedTo)) {
            report.setAssignedTo(assignedTo.trim());
        }
        report.setStatus(newStatus);
        reportRepository.save(report);

        if (ValidationService.isNotBlank(message)) {
            reportRepository.addFeedback(reportId, message.trim(), "admin");
        }

        // Post/Redirect/Get after the update.
        response.sendRedirect(request.getContextPath() + "/report?id=" + reportId);
    }
}

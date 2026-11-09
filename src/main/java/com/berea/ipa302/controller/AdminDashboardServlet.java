package com.berea.ipa302.controller;

import com.berea.ipa302.AppInitListener;
import com.berea.ipa302.model.MaintenanceReport;
import com.berea.ipa302.model.ReportStatus;
import com.berea.ipa302.repository.ReportRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows the administrator every report, with an optional filter by
 * status (?status=Assigned for example) so a busy admin can focus on
 * one stage of the workflow at a time.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtil.isLoggedIn(request) || !SessionUtil.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        ReportRepository reportRepository =
                (ReportRepository) getServletContext().getAttribute(AppInitListener.REPORT_REPO_KEY);

        String statusFilter = request.getParameter("status");
        List<MaintenanceReport> reports = new ArrayList<>(reportRepository.findAll());

        if (statusFilter != null && !statusFilter.isBlank()) {
            ReportStatus wanted = ReportStatus.fromText(statusFilter);
            if (wanted != null) {
                reports.removeIf(r -> r.getStatus() != wanted);
            }
        }

        request.setAttribute("reports", reports);
        request.setAttribute("statuses", ReportStatus.values());
        request.setAttribute("selectedStatus", statusFilter);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp");
        dispatcher.forward(request, response);
    }
}

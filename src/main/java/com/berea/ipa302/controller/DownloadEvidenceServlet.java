package com.berea.ipa302.controller;

import com.berea.ipa302.AppInitListener;
import com.berea.ipa302.model.MaintenanceReport;
import com.berea.ipa302.repository.ReportRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Streams back an uploaded evidence file so the browser can show/download it.
 * The file lives outside the web app (see AppInitListener) so it can only
 * ever be reached through this servlet, which checks permissions first.
 */
@WebServlet("/evidence")
public class DownloadEvidenceServlet extends HttpServlet {

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

        if (report == null || !report.hasEvidence()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No evidence file for this report.");
            return;
        }

        boolean isOwner = report.getStudentNumber().equals(SessionUtil.getStudentNumber(request));
        if (!SessionUtil.isAdmin(request) && !isOwner) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You may not view this file.");
            return;
        }

        String uploadDir = (String) getServletContext().getAttribute(AppInitListener.UPLOAD_DIR_KEY);
        File file = new File(uploadDir, report.getEvidenceStoredName());

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File is missing on the server.");
            return;
        }

        response.setContentType(Files.probeContentType(file.toPath()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + report.getEvidenceOriginalName() + "\"");

        try (InputStream in = Files.newInputStream(file.toPath());
             OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
        }
    }
}

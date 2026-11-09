package com.berea.ipa302.controller;

import com.berea.ipa302.AppInitListener;
import com.berea.ipa302.model.MaintenanceReport;
import com.berea.ipa302.repository.ReportRepository;
import com.berea.ipa302.service.ReportService;
import com.berea.ipa302.service.ValidationService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.UUID;

/**
 * Lets a logged-in student report a new fault, with an optional
 * evidence photo/PDF attached.
 */
@WebServlet("/student/report/new")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 2,      // 2 MB - matches the rule in the brief
        maxRequestSize = 1024 * 1024 * 3
)
public class CreateReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtil.isLoggedIn(request) || !SessionUtil.isStudent(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/createReport.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtil.isLoggedIn(request) || !SessionUtil.isStudent(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String studentNumber = SessionUtil.getStudentNumber(request);
        String category = request.getParameter("category");
        String location = request.getParameter("location");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");

        if (!ValidationService.isNotBlank(category)
                || !ValidationService.isNotBlank(location)
                || !ValidationService.isNotBlank(description)
                || !ValidationService.isValidPriority(priority)) {
            request.setAttribute("errorMessage", "Please fill in category, location, description and a valid priority (Low/Medium/High).");
            request.getRequestDispatcher("/WEB-INF/views/createReport.jsp").forward(request, response);
            return;
        }

        MaintenanceReport report = ReportService.buildNewReport(studentNumber, category.trim(),
                location.trim(), description.trim(), priority.trim());

        // Evidence file is optional.
        Part filePart = request.getPart("evidence");
        if (filePart != null && filePart.getSize() > 0) {
            String originalName = filePart.getSubmittedFileName();

            if (!ValidationService.isValidEvidenceFileName(originalName)) {
                request.setAttribute("errorMessage", "Evidence must be a JPG, PNG or PDF file.");
                request.getRequestDispatcher("/WEB-INF/views/createReport.jsp").forward(request, response);
                return;
            }
            if (filePart.getSize() > ValidationService.MAX_EVIDENCE_FILE_SIZE) {
                request.setAttribute("errorMessage", "Evidence file must be 2 MB or smaller.");
                request.getRequestDispatcher("/WEB-INF/views/createReport.jsp").forward(request, response);
                return;
            }

            String extension = originalName.substring(originalName.lastIndexOf('.'));
            String storedName = UUID.randomUUID().toString() + extension;

            String uploadDir = (String) getServletContext().getAttribute(AppInitListener.UPLOAD_DIR_KEY);
            File destination = new File(uploadDir, storedName);

            try (InputStream in = filePart.getInputStream();
                 OutputStream out = Files.newOutputStream(destination.toPath())) {
                in.transferTo(out);
            }

            report.setEvidenceStoredName(storedName);
            report.setEvidenceOriginalName(originalName);
        }

        ReportRepository reportRepository =
                (ReportRepository) getServletContext().getAttribute(AppInitListener.REPORT_REPO_KEY);
        reportRepository.save(report);

        // Post/Redirect/Get so refreshing the page does not submit twice.
        response.sendRedirect(request.getContextPath() + "/student/dashboard");
    }
}

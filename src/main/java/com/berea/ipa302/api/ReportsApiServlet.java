package com.berea.ipa302.api;

import com.berea.ipa302.AppInitListener;
import com.berea.ipa302.controller.SessionUtil;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

/**
 * REST-style JSON endpoint for maintenance reports.
 *
 * GET    /api/reports            -> list reports (own reports for a student, all for admin)
 * GET    /api/reports?id=5       -> one report
 * POST   /api/reports            -> create a report (student only), body is JSON
 * PUT    /api/reports?id=5       -> update status/feedback (admin only), body is JSON
 * anything else                  -> 405
 *
 * The API checks the same session used by the normal web pages, so a
 * client (browser, Postman, cURL) must log in through /login first and
 * keep sending the JSESSIONID cookie with each request.
 */
@WebServlet("/api/reports")
public class ReportsApiServlet extends HttpServlet {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        if (!SessionUtil.isLoggedIn(request)) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "You must be logged in.");
            return;
        }

        ReportRepository reportRepository =
                (ReportRepository) getServletContext().getAttribute(AppInitListener.REPORT_REPO_KEY);

        String idParam = request.getParameter("id");

        if (idParam == null) {
            // list endpoint
            Collection<MaintenanceReport> all;
            if (SessionUtil.isAdmin(request)) {
                all = reportRepository.findAll();
            } else {
                all = reportRepository.findByStudentNumber(SessionUtil.getStudentNumber(request));
            }
            JSONArray array = new JSONArray();
            for (MaintenanceReport report : all) {
                array.put(toJson(report));
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(array.toString());
            return;
        }

        // single record endpoint
        MaintenanceReport report = findReportOrNull(idParam, reportRepository);
        if (report == null) {
            writeError(response, HttpServletResponse.SC_NOT_FOUND, "Report not found.");
            return;
        }
        if (!SessionUtil.isAdmin(request) && !report.getStudentNumber().equals(SessionUtil.getStudentNumber(request))) {
            // Do not reveal another student's private report.
            writeError(response, HttpServletResponse.SC_NOT_FOUND, "Report not found.");
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(toJson(report).toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        if (!SessionUtil.isLoggedIn(request) || !SessionUtil.isStudent(request)) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Only a logged-in student can create a report.");
            return;
        }

        JSONObject body;
        try {
            body = readJsonBody(request);
        } catch (Exception e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Request body must be valid JSON.");
            return;
        }

        String category = body.optString("category", null);
        String location = body.optString("location", null);
        String description = body.optString("description", null);
        String priority = body.optString("priority", null);

        if (!ValidationService.isNotBlank(category) || !ValidationService.isNotBlank(location)
                || !ValidationService.isNotBlank(description) || !ValidationService.isValidPriority(priority)) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "category, location, description and a valid priority (Low/Medium/High) are required.");
            return;
        }

        MaintenanceReport report = ReportService.buildNewReport(
                SessionUtil.getStudentNumber(request), category.trim(), location.trim(),
                description.trim(), priority.trim());

        ReportRepository reportRepository =
                (ReportRepository) getServletContext().getAttribute(AppInitListener.REPORT_REPO_KEY);
        reportRepository.save(report);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write(toJson(report).toString());
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        if (!SessionUtil.isLoggedIn(request) || !SessionUtil.isAdmin(request)) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Only the administrator can update a report.");
            return;
        }

        String idParam = request.getParameter("id");
        ReportRepository reportRepository =
                (ReportRepository) getServletContext().getAttribute(AppInitListener.REPORT_REPO_KEY);
        MaintenanceReport report = findReportOrNull(idParam, reportRepository);

        if (report == null) {
            writeError(response, HttpServletResponse.SC_NOT_FOUND, "Report not found.");
            return;
        }

        JSONObject body;
        try {
            body = readJsonBody(request);
        } catch (Exception e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Request body must be valid JSON.");
            return;
        }

        String statusText = body.optString("status", null);
        String message = body.optString("feedback", null);
        String assignedTo = body.optString("assignedTo", null);

        ReportStatus newStatus = ReportStatus.fromText(statusText);
        if (newStatus == null || !ReportService.isValidTransition(report.getStatus(), newStatus)) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "status must be a valid, forward-moving status.");
            return;
        }

        if (ValidationService.isNotBlank(assignedTo)) {
            report.setAssignedTo(assignedTo.trim());
        }
        report.setStatus(newStatus);
        reportRepository.save(report);

        if (ValidationService.isNotBlank(message)) {
            reportRepository.addFeedback(report.getId(), message.trim(), "admin");
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(toJson(report).toString());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String method = request.getMethod();
        if ("GET".equals(method) || "POST".equals(method) || "PUT".equals(method) || "HEAD".equals(method)) {
            super.service(request, response);
        } else {
            response.setHeader("Allow", "GET, POST, PUT");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.getWriter().write(new JSONObject().put("error", "Method not allowed.").toString());
        }
    }

    // ---- helpers ----

    private MaintenanceReport findReportOrNull(String idParam, ReportRepository reportRepository) {
        if (idParam == null) {
            return null;
        }
        try {
            return reportRepository.findById(Integer.parseInt(idParam));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private JSONObject readJsonBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return new JSONObject(sb.toString());
    }

    private JSONObject toJson(MaintenanceReport report) {
        JSONObject json = new JSONObject();
        json.put("id", report.getId());
        json.put("studentNumber", report.getStudentNumber());
        json.put("category", report.getCategory());
        json.put("location", report.getLocation());
        json.put("description", report.getDescription());
        json.put("priority", report.getPriority());
        json.put("status", report.getStatus().getDisplayName());
        json.put("assignedTo", report.getAssignedTo() == null ? "" : report.getAssignedTo());
        json.put("hasEvidence", report.hasEvidence());
        json.put("createdDate", report.getCreatedDate() == null ? "" : report.getCreatedDate().format(DATE_FORMAT));
        return json;
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.getWriter().write(new JSONObject().put("error", message).toString());
    }
}

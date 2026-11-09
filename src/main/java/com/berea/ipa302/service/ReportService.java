package com.berea.ipa302.service;

import com.berea.ipa302.model.MaintenanceReport;
import com.berea.ipa302.model.ReportStatus;

import java.time.LocalDateTime;

/**
 * Small business-logic helper class for maintenance reports.
 * Kept separate from the servlets so the rules can be unit tested
 * without needing a running Tomcat server.
 */
public class ReportService {

    private ReportService() {
    }

    /**
     * Builds a new report in the "Reported" status from already-validated
     * values. Validation itself happens in ValidationService before this
     * method is called.
     */
    public static MaintenanceReport buildNewReport(String studentNumber, String category,
                                                     String location, String description,
                                                     String priority) {
        MaintenanceReport report = new MaintenanceReport();
        report.setStudentNumber(studentNumber);
        report.setCategory(category);
        report.setLocation(location);
        report.setDescription(description);
        report.setPriority(priority);
        report.setStatus(ReportStatus.REPORTED);
        report.setCreatedDate(LocalDateTime.now());
        return report;
    }

    /**
     * The status of a report is only allowed to move forward through the
     * fixed workflow (Reported -> Assigned -> In Progress -> Resolved ->
     * Closed), or stay the same. Moving backwards, e.g. from Resolved back
     * to Reported, is rejected because it does not make sense for a fault
     * that has already been fixed.
     */
    public static boolean isValidTransition(ReportStatus from, ReportStatus to) {
        if (from == null || to == null) {
            return false;
        }
        return to.ordinal() >= from.ordinal();
    }
}

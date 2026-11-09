package com.berea.ipa302.service;

import com.berea.ipa302.model.MaintenanceReport;
import com.berea.ipa302.model.ReportStatus;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the report business rules: building a new report and
 * checking which status changes are allowed.
 */
public class ReportServiceTest {

    @Test
    public void buildNewReport_startsAsReportedStatus() {
        MaintenanceReport report = ReportService.buildNewReport(
                "123456789", "Electrical", "Block C, Room 12", "Light not working", "Medium");

        assertEquals(ReportStatus.REPORTED, report.getStatus());
        assertEquals("123456789", report.getStudentNumber());
        assertNotNull(report.getCreatedDate());
    }

    @Test
    public void transition_forwardOneStep_isAllowed() {
        assertTrue(ReportService.isValidTransition(ReportStatus.REPORTED, ReportStatus.ASSIGNED));
    }

    @Test
    public void transition_skippingSteps_isAllowed() {
        // The rule only blocks going backwards, skipping ahead is fine.
        assertTrue(ReportService.isValidTransition(ReportStatus.REPORTED, ReportStatus.CLOSED));
    }

    @Test
    public void transition_stayingOnSameStatus_isAllowed_boundaryCase() {
        assertTrue(ReportService.isValidTransition(ReportStatus.IN_PROGRESS, ReportStatus.IN_PROGRESS));
    }

    @Test
    public void transition_backwards_isRejected() {
        assertFalse(ReportService.isValidTransition(ReportStatus.RESOLVED, ReportStatus.REPORTED));
    }

    @Test
    public void transition_nullValues_areRejected() {
        assertFalse(ReportService.isValidTransition(null, ReportStatus.REPORTED));
        assertFalse(ReportService.isValidTransition(ReportStatus.REPORTED, null));
    }
}

package com.berea.ipa302.model;

import java.time.LocalDateTime;

/**
 * A fault reported by a student, for example a broken tap or a
 * flickering light in a lecture hall.
 */
public class MaintenanceReport {

    private int id;
    private String studentNumber;   // owner of this report
    private String category;        // e.g. Electrical, Plumbing, IT, Furniture
    private String location;        // e.g. "Block C, Room 12"
    private String description;
    private String priority;        // Low, Medium, High
    private ReportStatus status;
    private String assignedTo;      // person/team the admin assigned, can be blank

    // File attachment details. The file itself is saved on disk with a
    // generated name (see FileStorage in CreateReportServlet) so the
    // original name below is only used to show something readable to the user.
    private String evidenceStoredName;
    private String evidenceOriginalName;

    private LocalDateTime createdDate;

    public MaintenanceReport() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getEvidenceStoredName() {
        return evidenceStoredName;
    }

    public void setEvidenceStoredName(String evidenceStoredName) {
        this.evidenceStoredName = evidenceStoredName;
    }

    public String getEvidenceOriginalName() {
        return evidenceOriginalName;
    }

    public void setEvidenceOriginalName(String evidenceOriginalName) {
        this.evidenceOriginalName = evidenceOriginalName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean hasEvidence() {
        return evidenceStoredName != null && !evidenceStoredName.isBlank();
    }
}

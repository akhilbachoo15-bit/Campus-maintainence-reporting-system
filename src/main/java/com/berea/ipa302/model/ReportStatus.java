package com.berea.ipa302.model;

/**
 * The status a maintenance report can move through.
 * Order matches the assignment brief:
 * Reported -> Assigned -> In Progress -> Resolved -> Closed
 */
public enum ReportStatus {
    REPORTED("Reported"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved"),
    CLOSED("Closed");

    private final String displayName;

    ReportStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Turns text like "In Progress" or "in_progress" back into an enum value.
     * Returns null instead of throwing so callers can show a nice error message.
     */
    public static ReportStatus fromText(String text) {
        if (text == null) {
            return null;
        }
        String cleaned = text.trim().toUpperCase().replace(" ", "_");
        for (ReportStatus status : values()) {
            if (status.name().equals(cleaned)) {
                return status;
            }
        }
        return null;
    }
}

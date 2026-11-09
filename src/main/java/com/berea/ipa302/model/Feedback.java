package com.berea.ipa302.model;

import java.time.LocalDateTime;

/**
 * One comment/update the admin leaves on a report.
 * A report can have many Feedback entries, newest last.
 */
public class Feedback {

    private int id;
    private int reportId;
    private String message;
    private String author;   // who wrote it, e.g. "admin"
    private LocalDateTime date;

    public Feedback() {
    }

    public Feedback(int id, int reportId, String message, String author, LocalDateTime date) {
        this.id = id;
        this.reportId = reportId;
        this.message = message;
        this.author = author;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}

package com.berea.ipa302.repository;

import com.berea.ipa302.model.Feedback;
import com.berea.ipa302.model.MaintenanceReport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory storage for maintenance reports and their feedback.
 * Same idea as UserRepository - simple shared collections protected
 * with thread-safe types and synchronized write methods, because
 * several requests can arrive at the same time.
 */
public class ReportRepository {

    private final Map<Integer, MaintenanceReport> reports = new ConcurrentHashMap<>();
    private final Map<Integer, List<Feedback>> feedbackByReportId = new ConcurrentHashMap<>();

    private final AtomicInteger reportIdGenerator = new AtomicInteger(1);
    private final AtomicInteger feedbackIdGenerator = new AtomicInteger(1);

    public synchronized MaintenanceReport save(MaintenanceReport report) {
        if (report.getId() == 0) {
            report.setId(reportIdGenerator.getAndIncrement());
            feedbackByReportId.put(report.getId(), new CopyOnWriteArrayList<>());
        }
        reports.put(report.getId(), report);
        return report;
    }

    public MaintenanceReport findById(int id) {
        return reports.get(id);
    }

    public Collection<MaintenanceReport> findAll() {
        return reports.values();
    }

    public List<MaintenanceReport> findByStudentNumber(String studentNumber) {
        List<MaintenanceReport> result = new ArrayList<>();
        for (MaintenanceReport report : reports.values()) {
            if (report.getStudentNumber() != null && report.getStudentNumber().equals(studentNumber)) {
                result.add(report);
            }
        }
        return result;
    }

    public synchronized Feedback addFeedback(int reportId, String message, String author) {
        Feedback feedback = new Feedback();
        feedback.setId(feedbackIdGenerator.getAndIncrement());
        feedback.setReportId(reportId);
        feedback.setMessage(message);
        feedback.setAuthor(author);
        feedback.setDate(java.time.LocalDateTime.now());

        List<Feedback> list = feedbackByReportId.computeIfAbsent(reportId, k -> new CopyOnWriteArrayList<>());
        list.add(feedback);
        return feedback;
    }

    public List<Feedback> getFeedback(int reportId) {
        return feedbackByReportId.getOrDefault(reportId, new ArrayList<>());
    }
}

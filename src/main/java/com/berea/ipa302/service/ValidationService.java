package com.berea.ipa302.service;

import java.util.regex.Pattern;

/**
 * Holds all the server-side validation rules used by the app.
 * We never trust the browser (HTML "required" attributes can be
 * removed by anyone using the developer tools), so every field is
 * checked again here before anything is saved.
 */
public class ValidationService {

    private static final Pattern STUDENT_NUMBER_PATTERN = Pattern.compile("^\\d{6,12}$");

    // A "reasonable" email check, not a full RFC 5322 implementation.
    // Allows domains with more than one dot, e.g. berea.ac.za
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private static final String[] VALID_PRIORITIES = {"Low", "Medium", "High"};

    private ValidationService() {
    }

    public static boolean isValidStudentNumber(String studentNumber) {
        return studentNumber != null && STUDENT_NUMBER_PATTERN.matcher(studentNumber.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidPriority(String priority) {
        if (priority == null) {
            return false;
        }
        for (String valid : VALID_PRIORITIES) {
            if (valid.equalsIgnoreCase(priority.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the file extension of an uploaded evidence file.
     * Only jpg, jpeg, png and pdf are accepted, and the file cannot be
     * bigger than 2 MB (checked separately using the part size).
     */
    public static boolean isValidEvidenceFileName(String fileName) {
        if (fileName == null) {
            return false;
        }
        String lower = fileName.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".png") || lower.endsWith(".pdf");
    }

    public static final long MAX_EVIDENCE_FILE_SIZE = 2L * 1024 * 1024; // 2 MB
}
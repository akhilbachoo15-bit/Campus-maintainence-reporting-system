package com.berea.ipa302.controller;

import com.berea.ipa302.model.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Small helper used by every protected servlet to check who is logged
 * in. We only ever store the student number and the role in the
 * session - never the password and never a whole User object with
 * sensitive fields in it.
 */
public class SessionUtil {

    public static final String SESSION_STUDENT_NUMBER = "studentNumber";
    public static final String SESSION_ROLE = "role";

    private SessionUtil() {
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(SESSION_STUDENT_NUMBER) != null;
    }

    public static String getStudentNumber(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute(SESSION_STUDENT_NUMBER);
    }

    public static Role getRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Role) session.getAttribute(SESSION_ROLE);
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return getRole(request) == Role.ADMIN;
    }

    public static boolean isStudent(HttpServletRequest request) {
        return getRole(request) == Role.STUDENT;
    }
}

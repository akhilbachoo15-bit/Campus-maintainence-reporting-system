package com.berea.ipa302.repository;

import com.berea.ipa302.model.Role;
import com.berea.ipa302.model.User;
import com.berea.ipa302.service.PasswordUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Keeps all registered users in memory. This is only for the classroom
 * prototype - a real application would use a database table instead so
 * that data survives a server restart.
 *
 * ConcurrentHashMap is used because several users could hit the server
 * (different browser tabs/students) at the same time.
 */
public class UserRepository {

    // studentNumber (or "admin") -> User
    private final Map<String, User> usersByStudentNumber = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public UserRepository() {
        // Fixed classroom administrator account, as required by the brief.
        User admin = new User();
        admin.setId(idGenerator.getAndIncrement());
        admin.setStudentNumber("admin");
        admin.setEmail("admin@berea.local");
        admin.setPasswordHash(PasswordUtil.hash("Demo1234"));
        admin.setRole(Role.ADMIN);
        usersByStudentNumber.put("admin", admin);
    }

    public synchronized User save(User user) {
        if (user.getId() == 0) {
            user.setId(idGenerator.getAndIncrement());
        }
        usersByStudentNumber.put(user.getStudentNumber(), user);
        return user;
    }

    public User findByStudentNumber(String studentNumber) {
        if (studentNumber == null) {
            return null;
        }
        return usersByStudentNumber.get(studentNumber.trim());
    }

    public boolean existsByStudentNumber(String studentNumber) {
        return findByStudentNumber(studentNumber) != null;
    }

    public boolean existsByEmail(String email) {
        if (email == null) {
            return false;
        }
        for (User user : usersByStudentNumber.values()) {
            if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(email.trim())) {
                return true;
            }
        }
        return false;
    }

    public Collection<User> findAll() {
        return usersByStudentNumber.values();
    }
}

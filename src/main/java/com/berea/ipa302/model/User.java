package com.berea.ipa302.model;

/**
 * A registered user. Students register themselves.
 * The single admin account is created once when the app starts
 * (see UserRepository) and is not editable through the UI.
 *
 * NOTE: this is a classroom prototype. Users are kept in memory only
 * (see repository package) and are lost when the server restarts.
 * A real system would use a database with a properly salted and
 * hashed password column, not a plain in-memory map.
 */
public class User {

    private int id;
    private String studentNumber; // also used as the login username
    private String email;
    private String passwordHash;  // never store the plain password, see PasswordUtil
    private Role role;

    public User() {
    }

    public User(int id, String studentNumber, String email, String passwordHash, Role role) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

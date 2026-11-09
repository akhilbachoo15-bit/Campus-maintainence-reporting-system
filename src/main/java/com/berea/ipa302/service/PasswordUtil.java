package com.berea.ipa302.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Very small helper that turns a password into a SHA-256 hash so that
 * the plain password is never kept anywhere (not in the user object,
 * not in the session, not in a log message).
 *
 * IMPORTANT: this is still not how a real production system should do
 * it. A real system needs a per-user random salt and a slow algorithm
 * made for passwords, such as bcrypt or PBKDF2, together with a proper
 * database instead of an in-memory map. Plain SHA-256 is only used
 * here to keep the classroom prototype simple.
 */
public class PasswordUtil {

    private PasswordUtil() {
        // utility class, no instances
    }

    public static String hash(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is always available on the JVM, but we still have to
            // handle the checked exception somehow.
            throw new RuntimeException("Could not hash password", e);
        }
    }

    public static boolean matches(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        return hash(plainPassword).equals(storedHash);
    }
}

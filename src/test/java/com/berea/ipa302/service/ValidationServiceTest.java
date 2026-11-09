package com.berea.ipa302.service;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the server-side validation rules.
 * Covers valid cases, invalid cases and a few boundary cases
 * (shortest/longest allowed student number, shortest allowed password).
 */
public class ValidationServiceTest {

    @Test
    public void studentNumber_acceptsSixDigits_boundaryCase() {
        assertTrue(ValidationService.isValidStudentNumber("123456"));
    }

    @Test
    public void studentNumber_acceptsTwelveDigits_boundaryCase() {
        assertTrue(ValidationService.isValidStudentNumber("123456789012"));
    }

    @Test
    public void studentNumber_rejectsFiveDigits_tooShort() {
        assertFalse(ValidationService.isValidStudentNumber("12345"));
    }

    @Test
    public void studentNumber_rejectsLettersMixedIn() {
        assertFalse(ValidationService.isValidStudentNumber("12A456"));
    }

    @Test
    public void email_acceptsNormalAddress() {
        assertTrue(ValidationService.isValidEmail("student@berea.ac.za"));
    }

    @Test
    public void email_rejectsMissingAtSymbol() {
        assertFalse(ValidationService.isValidEmail("studentberea.ac.za"));
    }

    @Test
    public void password_acceptsExactlyEightCharacters_boundaryCase() {
        assertTrue(ValidationService.isValidPassword("abcd1234"));
    }

    @Test
    public void password_rejectsSevenCharacters_tooShort() {
        assertFalse(ValidationService.isValidPassword("abcd123"));
    }

    @Test
    public void priority_acceptsKnownValues() {
        assertTrue(ValidationService.isValidPriority("Low"));
        assertTrue(ValidationService.isValidPriority("medium"));
        assertTrue(ValidationService.isValidPriority("HIGH"));
    }

    @Test
    public void priority_rejectsUnknownValue() {
        assertFalse(ValidationService.isValidPriority("Urgent"));
    }

    @Test
    public void evidenceFileName_acceptsAllowedExtensions() {
        assertTrue(ValidationService.isValidEvidenceFileName("photo.jpg"));
        assertTrue(ValidationService.isValidEvidenceFileName("photo.PNG"));
        assertTrue(ValidationService.isValidEvidenceFileName("report.pdf"));
    }

    @Test
    public void evidenceFileName_rejectsDisallowedExtension() {
        assertFalse(ValidationService.isValidEvidenceFileName("virus.exe"));
    }
}

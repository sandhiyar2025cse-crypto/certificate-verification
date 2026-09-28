package com.example.certificate.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "verification_logs")
public class VerificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certificateId;

    private LocalDateTime verificationTime;

    private String result;

    @Column(length = 500)
    private String message;

    public VerificationLog() {
    }

    public VerificationLog(String certificateId, String result, String message) {
        this.certificateId = certificateId;
        this.result = result;
        this.message = message;
        this.verificationTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public LocalDateTime getVerificationTime() {
        return verificationTime;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
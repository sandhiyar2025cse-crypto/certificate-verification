package com.example.certificate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.certificate.model.VerificationLog;

public interface VerificationLogRepository extends JpaRepository<VerificationLog, Long> {

    List<VerificationLog> findAllByOrderByVerificationTimeDesc();
}
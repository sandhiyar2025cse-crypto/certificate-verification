package com.example.certificate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.certificate.model.Certificate;
import com.example.certificate.model.CertificateStatus;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Optional<Certificate> findByCertificateId(String certificateId);

    long countByStatus(CertificateStatus status);
}
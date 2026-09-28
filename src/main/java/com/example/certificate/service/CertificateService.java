package com.example.certificate.service;

import com.example.certificate.model.Certificate;
import com.example.certificate.model.CertificateStatus;
import com.example.certificate.repository.CertificateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final HashService hashService;
    private final BlockchainService blockchainService;

    public CertificateService(
            CertificateRepository certificateRepository,
            HashService hashService,
            BlockchainService blockchainService) {

        this.certificateRepository = certificateRepository;
        this.hashService = hashService;
        this.blockchainService = blockchainService;
    }

    public List<Certificate> findAll() {
        return certificateRepository.findAll();
    }

    public Certificate findByCertificateId(String certificateId) {
        return certificateRepository
                .findByCertificateId(certificateId)
                .orElse(null);
    }

    public Certificate findById(Long id) {
        return certificateRepository
                .findById(id)
                .orElse(null);
    }

    public Certificate issueCertificate(Certificate certificate) {

        if (certificateRepository
                .findByCertificateId(certificate.getCertificateId())
                .isPresent()) {

            throw new IllegalArgumentException(
                    "Certificate ID already exists"
            );
        }

        certificate.setStatus(CertificateStatus.ACTIVE);

        String hash = hashService.generateCertificateHash(
                certificate.getCertificateId(),
                certificate.getStudentName(),
                certificate.getCourseName(),
                certificate.getInstitution(),
                certificate.getIssueDate().toString()
        );

        certificate.setCertificateHash(hash);

        Certificate savedCertificate =
                certificateRepository.save(certificate);

        blockchainService.addBlock(
                savedCertificate.getCertificateId(),
                savedCertificate.getCertificateHash()
        );

        return savedCertificate;
    }

    public Certificate updateCertificate(Certificate certificate) {

        Certificate existing =
                certificateRepository
                        .findById(certificate.getId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Certificate not found"
                                ));

        existing.setStudentName(certificate.getStudentName());
        existing.setCourseName(certificate.getCourseName());
        existing.setInstitution(certificate.getInstitution());
        existing.setIssueDate(certificate.getIssueDate());

        String hash = hashService.generateCertificateHash(
                existing.getCertificateId(),
                existing.getStudentName(),
                existing.getCourseName(),
                existing.getInstitution(),
                existing.getIssueDate().toString()
        );

        existing.setCertificateHash(hash);

        Certificate updatedCertificate =
                certificateRepository.save(existing);

        blockchainService.addBlock(
                updatedCertificate.getCertificateId(),
                updatedCertificate.getCertificateHash()
        );

        return updatedCertificate;
    }

    public void revokeCertificate(Long id) {

        Certificate certificate =
                certificateRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Certificate not found"
                                ));

        certificate.setStatus(CertificateStatus.REVOKED);

        certificateRepository.save(certificate);
    }

    public long getTotalCertificates() {
        return certificateRepository.count();
    }

    public long getActiveCertificates() {
        return certificateRepository
                .countByStatus(CertificateStatus.ACTIVE);
    }

    public long getRevokedCertificates() {
        return certificateRepository
                .countByStatus(CertificateStatus.REVOKED);
    }
}
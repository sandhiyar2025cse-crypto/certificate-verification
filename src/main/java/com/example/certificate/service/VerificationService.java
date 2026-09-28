package com.example.certificate.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.certificate.model.BlockchainRecord;
import com.example.certificate.model.Certificate;
import com.example.certificate.model.VerificationLog;
import com.example.certificate.repository.VerificationLogRepository;

@Service
public class VerificationService {

    private final CertificateService certificateService;
    private final HashService hashService;
    private final BlockchainService blockchainService;
    private final VerificationLogRepository verificationLogRepository;

    public VerificationService(
            CertificateService certificateService,
            HashService hashService,
            BlockchainService blockchainService,
            VerificationLogRepository verificationLogRepository) {

        this.certificateService = certificateService;
        this.hashService = hashService;
        this.blockchainService = blockchainService;
        this.verificationLogRepository = verificationLogRepository;
    }

    public VerificationResult verifyCertificate(String certificateId) {

        Certificate certificate =
                certificateService.findByCertificateId(certificateId);

        if (certificate == null) {

            VerificationLog log = new VerificationLog(
                    certificateId,
                    "NOT FOUND",
                    "Certificate does not exist"
            );

            verificationLogRepository.save(log);

            return new VerificationResult(
                    certificateId,
                    false,
                    "Certificate not found",
                    null,
                    null
            );
        }

        if (certificate.getStatus().name().equals("REVOKED")) {

            VerificationLog log = new VerificationLog(
                    certificateId,
                    "REVOKED",
                    "Certificate has been revoked"
            );

            verificationLogRepository.save(log);

            return new VerificationResult(
                    certificateId,
                    false,
                    "Certificate has been revoked",
                    certificate,
                    null
            );
        }

        String currentHash = hashService.generateCertificateHash(
                certificate.getCertificateId(),
                certificate.getStudentName(),
                certificate.getCourseName(),
                certificate.getInstitution(),
                certificate.getIssueDate().toString()
        );

        Optional<BlockchainRecord> latestBlock =
                blockchainService.latestFor(certificateId);

        if (latestBlock.isEmpty()) {

            VerificationLog log = new VerificationLog(
                    certificateId,
                    "INVALID",
                    "Blockchain record not found"
            );

            verificationLogRepository.save(log);

            return new VerificationResult(
                    certificateId,
                    false,
                    "Blockchain record not found",
                    certificate,
                    null
            );
        }

        BlockchainRecord block = latestBlock.get();

        boolean hashMatches =
                currentHash.equals(block.getCertificateHash());

        if (hashMatches) {

            VerificationLog log = new VerificationLog(
                    certificateId,
                    "VERIFIED",
                    "Certificate hash matches blockchain record"
            );

            verificationLogRepository.save(log);

            return new VerificationResult(
                    certificateId,
                    true,
                    "Certificate verified successfully",
                    certificate,
                    block
            );

        } else {

            VerificationLog log = new VerificationLog(
                    certificateId,
                    "INVALID",
                    "Certificate hash does not match blockchain record"
            );

            verificationLogRepository.save(log);

            return new VerificationResult(
                    certificateId,
                    false,
                    "Certificate information may have been modified",
                    certificate,
                    block
            );
        }
    }

    public record VerificationResult(
            String certificateId,
            boolean verified,
            String message,
            Certificate certificate,
            BlockchainRecord block) {
    }
}
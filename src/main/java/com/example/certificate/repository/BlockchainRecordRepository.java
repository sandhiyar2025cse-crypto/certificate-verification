package com.example.certificate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.certificate.model.BlockchainRecord;

public interface BlockchainRecordRepository extends JpaRepository<BlockchainRecord, Long> {

    Optional<BlockchainRecord> findTopByOrderByBlockIndexDesc();

    Optional<BlockchainRecord> findTopByCertificateIdOrderByBlockIndexDesc(String certificateId);

    List<BlockchainRecord> findAllByOrderByBlockIndexAsc();
}
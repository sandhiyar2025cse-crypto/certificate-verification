package com.example.certificate.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.certificate.model.BlockchainRecord;
import com.example.certificate.repository.BlockchainRecordRepository;

@Service
public class BlockchainService {

    private final BlockchainRecordRepository blockchainRecordRepository;
    private final HashService hashService;

    public BlockchainService(
            BlockchainRecordRepository blockchainRecordRepository,
            HashService hashService) {

        this.blockchainRecordRepository = blockchainRecordRepository;
        this.hashService = hashService;
    }

    public BlockchainRecord addBlock(
            String certificateId,
            String certificateHash) {

        Optional<BlockchainRecord> lastBlock =
                blockchainRecordRepository.findTopByOrderByBlockIndexDesc();

        long blockIndex = lastBlock
                .map(block -> block.getBlockIndex() + 1)
                .orElse(1L);

        String previousHash = lastBlock
                .map(BlockchainRecord::getBlockHash)
                .orElse("0");

        LocalDateTime timestamp = LocalDateTime.now();

        String blockData = blockIndex + "|" +
                certificateId + "|" +
                certificateHash + "|" +
                previousHash + "|" +
                timestamp;

        String blockHash = hashService.sha256(blockData);

        BlockchainRecord record = new BlockchainRecord();

        record.setBlockIndex(blockIndex);
        record.setCertificateId(certificateId);
        record.setCertificateHash(certificateHash);
        record.setPreviousHash(previousHash);
        record.setBlockHash(blockHash);
        record.setTimestamp(timestamp);

        return blockchainRecordRepository.save(record);
    }

    public Optional<BlockchainRecord> latestFor(String certificateId) {
        return blockchainRecordRepository
                .findTopByCertificateIdOrderByBlockIndexDesc(certificateId);
    }

    public List<BlockchainRecord> findAll() {
        return blockchainRecordRepository
                .findAllByOrderByBlockIndexAsc();
    }
}
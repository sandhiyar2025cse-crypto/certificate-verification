package com.example.certificate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.certificate.service.BlockchainService;

@Controller
@RequestMapping("/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping
    public String blockchainRecords(Model model) {

        model.addAttribute(
                "blocks",
                blockchainService.findAll()
        );

        return "blockchain";
    }
}
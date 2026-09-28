package com.example.certificate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.certificate.service.VerificationService;

@Controller
@RequestMapping("/verify")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping
    public String verifyPage(
            @RequestParam(required = false) String certificateId,
            Model model) {

        // If certificate ID comes from QR code
        if (certificateId != null && !certificateId.isBlank()) {

            VerificationService.VerificationResult result =
                    verificationService.verifyCertificate(certificateId);

            model.addAttribute("result", result);

            return "verify-result";
        }

        return "verify";
    }

    @PostMapping
    public String verifyCertificate(
            @RequestParam String certificateId,
            Model model) {

        VerificationService.VerificationResult result =
                verificationService.verifyCertificate(certificateId);

        model.addAttribute("result", result);

        return "verify-result";
    }
}
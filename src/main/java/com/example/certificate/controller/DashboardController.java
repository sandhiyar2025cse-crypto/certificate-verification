package com.example.certificate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.certificate.service.CertificateService;

@Controller
public class DashboardController {

    private final CertificateService certificateService;

    public DashboardController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
                "totalCertificates",
                certificateService.getTotalCertificates()
        );

        model.addAttribute(
                "activeCertificates",
                certificateService.getActiveCertificates()
        );

        model.addAttribute(
                "revokedCertificates",
                certificateService.getRevokedCertificates()
        );

        return "dashboard";
    }
}
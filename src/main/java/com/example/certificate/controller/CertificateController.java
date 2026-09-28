package com.example.certificate.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.certificate.model.Certificate;
import com.example.certificate.service.CertificateService;

@Controller
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public String listCertificates(Model model) {

        model.addAttribute(
                "certificates",
                certificateService.findAll()
        );

        return "certificates";
    }

    @GetMapping("/new")
    public String newCertificate(Model model) {

        Certificate certificate = new Certificate();
        certificate.setIssueDate(LocalDate.now());

        model.addAttribute("certificate", certificate);

        return "certificate-form";
    }

    @PostMapping("/save")
    public String saveCertificate(
            @ModelAttribute Certificate certificate) {

        certificateService.issueCertificate(certificate);

        return "redirect:/certificates";
    }

    @GetMapping("/edit/{id}")
    public String editCertificate(
            @PathVariable Long id,
            Model model) {

        Certificate certificate =
                certificateService.findById(id);

        if (certificate == null) {
            return "redirect:/certificates";
        }

        model.addAttribute("certificate", certificate);

        return "certificate-form";
    }

    @PostMapping("/update")
    public String updateCertificate(
            @ModelAttribute Certificate certificate) {

        certificateService.updateCertificate(certificate);

        return "redirect:/certificates";
    }

    @PostMapping("/revoke/{id}")
    public String revokeCertificate(
            @PathVariable Long id) {

        certificateService.revokeCertificate(id);

        return "redirect:/certificates";
    }
}
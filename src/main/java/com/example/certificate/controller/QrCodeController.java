package com.example.certificate.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.certificate.model.Certificate;
import com.example.certificate.service.CertificateService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Controller
@RequestMapping("/qr")
public class QrCodeController {

    private final CertificateService certificateService;

    public QrCodeController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/{certificateId}")
    public ResponseEntity<byte[]> generateQrCode(
            @PathVariable String certificateId) {

        Certificate certificate =
                certificateService.findByCertificateId(certificateId);

        if (certificate == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            String verificationUrl =
        "http://172.17.31.20:8080/verify?certificateId="
                + certificate.getCertificateId();
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    verificationUrl,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            MatrixToImageWriter.writeToStream(
                    bitMatrix,
                    "PNG",
                    outputStream
            );

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(outputStream.toByteArray());

        } catch (WriterException | IOException e) {

            return ResponseEntity.internalServerError().build();
        }
    }
}
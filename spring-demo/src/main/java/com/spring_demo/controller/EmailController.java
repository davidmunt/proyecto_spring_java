package com.spring_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.spring_demo.services.EmailService;

/**
 * Controlador REST para el envio de correos electronicos.
 * Proporciona endpoints para enviar correos simples y con adjuntos.
 */

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        emailService.sendEmail(to, subject, text);
        return ResponseEntity.ok("Correo enviado");
    }

    @PostMapping("/sendWithAttachment")
    public ResponseEntity<String> sendEmailWithAttachment(@RequestParam String to, @RequestParam String subject, @RequestParam String text, @RequestParam String pathToAttachment) {
        emailService.sendEmailWithAttachment(to, subject, text, pathToAttachment);
        return ResponseEntity.ok("Correo con adjunto enviado");
    }
}
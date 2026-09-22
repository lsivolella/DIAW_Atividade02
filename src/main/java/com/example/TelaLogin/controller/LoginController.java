package com.example.TelaLogin.controller;

import com.example.TelaLogin.service.PasswordRecoveryService;
import com.example.TelaLogin.service.SendEmailService;
import com.example.TelaLogin.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class LoginController {

    private final SendEmailService sendEmailService;
    private final UserService userService;
    private final PasswordRecoveryService passwordRecoveryService;
    
    public LoginController(SendEmailService sendEmailService, UserService userService, PasswordRecoveryService passwordRecoveryService) {
        this.sendEmailService = sendEmailService;
        this.userService = userService;
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

     // =========================================================
        // RECUPERAÇÃO DE SENHA
        // =========================================================

        @GetMapping("/recoverpassword")
        public String recoverpassword() {
                return "recoverpassword";
        }

        @PostMapping("/recoverpassword")
        public String handleRecoverPassword(
                        @RequestParam("email") String email) {

                if (!userService.exists(email)) {

                        System.out.println(
                                        "E-mail não encontrado: " + email);

                        return "redirect:/recoverpassword?erro=email";
                }

                String nome = userService.getName(email);

                if (nome == null || nome.isBlank()) {
                        nome = email;
                }

                String token = passwordRecoveryService.generateToken(email);

                String link = "http://localhost:8080/resetpassword?token="
                                + token;

                sendEmailService.sendEmail(
                                email,
                                "Recuperação de Senha - PUC Minas",
                                "<!DOCTYPE html><html lang='pt-BR'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'></head><body style='margin:0;padding:0;background-color:#f8fafc;font-family:Arial,Helvetica,sans-serif;color:#171717;'><div style='width:100%;padding:40px 20px;box-sizing:border-box;'><div style='max-width:600px;margin:0 auto;background:#ffffff;border:1px solid rgba(0,51,79,0.09);border-radius:20px;overflow:hidden;box-shadow:0 2px 4px rgba(0,51,79,0.08),0 24px 56px -12px rgba(0,51,79,0.20);'><div style='padding:40px 30px;text-align:center;background:linear-gradient(145deg,#00334f,#005380);'><div style='color:#ffffff;font-size:13px;font-weight:500;letter-spacing:2px;margin-bottom:12px;'>PUC MINAS</div><div style='color:#ffffff;font-size:28px;font-weight:500;line-height:1.2;'>Recuperação de Senha</div></div><div style='padding:40px 45px;text-align:center;'><p style='margin:0 0 18px 0;color:#005380;font-size:18px;font-weight:500;'>Olá, "
                                                + nome
                                                + "!</p><div style='width:60px;height:3px;margin:0 auto 25px auto;background-color:#005380;border-radius:999px;'></div><p style='margin:0 0 18px 0;color:#64748b;font-size:15px;line-height:1.7;'>Recebemos uma solicitação para redefinir a senha da sua conta no sistema.</p><p style='margin:0 0 30px 0;color:#64748b;font-size:15px;line-height:1.7;'>Clique no botão abaixo para criar uma nova senha.</p><a href='"
                                                + link
                                                + "' style='display:inline-block;padding:14px 28px;background-color:#005380;color:#ffffff;text-decoration:none;border-radius:8px;font-size:15px;font-weight:500;'>Redefinir minha senha</a><p style='margin:30px 0 0 0;padding:15px;background-color:#f8fafc;border:1px solid #e2e8f0;border-radius:8px;color:#64748b;font-size:13px;line-height:1.6;'>Este link é válido por <strong style='color:#171717;'>15 minutos</strong>.</p><p style='margin:25px 0 0 0;color:#64748b;font-size:13px;line-height:1.6;'>Se você não solicitou a recuperação da senha, ignore este e-mail.</p></div><div style='padding:20px 30px;background-color:#f8fafc;border-top:1px solid #e2e8f0;text-align:center;'><p style='margin:0;color:#64748b;font-size:12px;line-height:1.5;'>PUC Minas<br>Sistema de Autenticação</p></div></div></div></body></html>");

                System.out.println(
                                "Link de recuperação enviado para: " + email);

                return "redirect:/recoverpassword?sucesso=email";
        }
    
}

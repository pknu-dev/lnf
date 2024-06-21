package org.pknudev.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.pknudev.common.Config;
import org.pknudev.model.EmailVerification;
import org.pknudev.repository.EmailVerificationRepository;
import org.pknudev.service.MailService;

@WebServlet(name = "emailVerify", value = "/verifyEmail")
public class EmailVerifyServlet extends HttpServlet {
    static boolean isValidEmail(String email) {
        if (!email.contains("@")) {
            return false;
        }
        return email.endsWith("@" + Config.getProperty("EMAIL_DOMAIN"));
    }

    static String generateVerificationCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(1000000); // 0~999999
        return String.format("%06d", number);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = Helper.getStringParameter(request, "email", true);
        if (!isValidEmail(email)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid email");
            return;
        }

        String verificationCode = generateVerificationCode();

        EmailVerification emailVerification = EmailVerification.builder()
            .code(verificationCode)
            .email(email)
            .build();
        try {
            EmailVerificationRepository.createEmailVerification(emailVerification);
        } catch (SQLException e) {
            throw new RuntimeException("failed to create email verification", e);
        }

        String title = "회원가입 인증 번호입니다.";
        String content = "[부경대학교 분실물 게시판]\n" +
            "회원가입 인증 번호는 [ " + verificationCode + " ] 입니다.";
        MailService.sendMail(email, title, content);

        try {
            EmailVerificationRepository.deleteExpiredEmailVerifications();
        } catch (SQLException e) {
            System.err.println("failed to delete expired email verifications: " + e);
        }
    }
}
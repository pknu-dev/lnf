package org.pknudev.servlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.pknudev.common.Config;
import org.pknudev.common.Utility;
import org.pknudev.model.EmailVerification;
import org.pknudev.model.User;
import org.pknudev.repository.EmailVerificationRepository;
import org.pknudev.repository.UserRepository;

@WebServlet(name = "register", value = "/register")
public class RegisterServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("emailDomain", Config.getProperty("EMAIL_DOMAIN"));
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = Helper.getStringParameter(request, "email", true);
        String password = Helper.getStringParameter(request, "password", true);
        String nickname = Helper.getStringParameter(request, "nickname", true);
        String verificationCode = Helper.getStringParameter(request, "verificationCode", true);

        try {
            User user = UserRepository.getUser(email);
            if (user != null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "user already exists");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to get user", e);
        }

        try {
            User user = UserRepository.getUserByNickname(nickname);
            if (user != null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "nickname already taken");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to get user by nickname", e);
        }

        EmailVerification verification;
        try {
            verification = EmailVerificationRepository.getEmailVerification(verificationCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (verification == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid verification code");
            return;
        }

        if (!verification.getEmail().equals(email)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid verification code");
            return;
        }

        String passwordHash = Utility.sha256(password);

        User user = User.builder()
            .email(email)
            .passwordHash(passwordHash)
            .nickname(nickname)
            .build();
        try {
            UserRepository.createUser(user);
        } catch (SQLException e) {
            throw new RuntimeException("failed to create user", e);
        }

        response.sendRedirect("/");
    }
}
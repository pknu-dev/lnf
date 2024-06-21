package org.pknudev.servlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.pknudev.model.User;
import org.pknudev.repository.UserRepository;

@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = Helper.getStringParameter(request, "email", true);
        String password = Helper.getStringParameter(request, "password", true);

        User user;
        try {
            user = UserRepository.getUser(email);
        } catch (SQLException e) {
            throw new RuntimeException("failed to get user", e);
        }
        if (user == null || !user.checkPassword(password)) {
            response.sendRedirect("/login");
            return;
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid email or password");
//            return;
        }

        request.getSession().setAttribute("user", user);

        response.sendRedirect("/");
    }
}
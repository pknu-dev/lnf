package org.pknudev.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.pknudev.model.Post;
import org.pknudev.repository.PostRepository;

@WebServlet(name = "home", value = "/")
public class HomeServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Post> posts;
        try {
            posts = PostRepository.getPosts();
        } catch (SQLException e) {
            throw new RuntimeException("failed to get posts", e);
        }

        request.setAttribute("posts", posts);
        request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
    }
}
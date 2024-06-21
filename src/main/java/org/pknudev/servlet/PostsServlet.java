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

@WebServlet(name = "posts", value = "/posts")
public class PostsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int type = Helper.getIntegerParameter(request, "type", true);
        if (!Post.isValidType(type)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid post type");
            return;
        }

        List<Post> posts;
        try {
            posts = PostRepository.getPostsByType(type, Integer.MAX_VALUE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("type", type);
        request.setAttribute("posts", posts);

        request.getRequestDispatcher("/WEB-INF/jsp/posts.jsp").forward(request, response);
    }
}
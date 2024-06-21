package org.pknudev.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.pknudev.model.Post;
import org.pknudev.repository.PostRepository;

@WebServlet(name = "search", value = "/search")
public class SearchServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("type", 0);
        request.setAttribute("query", "");
        request.setAttribute("posts", new ArrayList<Post>());
        request.getRequestDispatcher("/WEB-INF/jsp/search.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int postType = Helper.getIntegerParameter(request, "type", true);
        String query = Helper.getStringParameter(request, "query", true);

        List<Post> posts;
        try {
            posts = PostRepository.searchPosts(query, postType);
        } catch (SQLException e) {
            throw new RuntimeException("failed to search posts", e);
        }

        request.setAttribute("type", postType);
        request.setAttribute("query", query);
        request.setAttribute("posts", posts);
        request.getRequestDispatcher("/WEB-INF/jsp/search.jsp").forward(request, response);
    }
}
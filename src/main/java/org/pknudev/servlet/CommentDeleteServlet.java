package org.pknudev.servlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.pknudev.model.Comment;
import org.pknudev.model.User;
import org.pknudev.repository.CommentRepository;

@WebServlet(name = "commentDelete", value = "/deleteComment")
public class CommentDeleteServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = Helper.getLoginUser(request, response);
        if (user == null) {
            return;
        }

        int id = Helper.getIntegerParameter(request, "id", true);

        Comment comment;
        try {
            comment = CommentRepository.getComment(id);
        } catch (SQLException e) {
            throw new RuntimeException("failed to get comment", e);
        }
        if (comment == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "comment not found");
            return;
        }

        if (!comment.getAuthorEmail().equals(user.getEmail())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "comment is not owned by the current user");
            return;
        }

        try {
            CommentRepository.deleteComment(id);
        } catch (SQLException e) {
            throw new RuntimeException("failed to delete comment", e);
        }
    }
}
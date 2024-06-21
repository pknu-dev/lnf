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

@WebServlet(name = "commentWrite", value = "/writeComment")
public class CommentWriteServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = Helper.getLoginUser(request, response);
        if (user == null) {
            return;
        }

        int postId = Helper.getIntegerParameter(request, "post_id", true);

        Integer parentId = Helper.getIntegerParameter(request, "parent_id", false);
        if (parentId != null) {
            try {
                Comment parent = CommentRepository.getComment(parentId);
                if (parent == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "parent comment not found");
                    return;
                }

                if (parent.getPostId() != postId) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid parent comment id");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException("failed to check parent comment", e);
            }
        }

        String content = Helper.getStringParameter(request, "content", true);

        Comment comment = Comment.builder()
            .postId(postId)
            .authorEmail(user.getEmail())
            .content(content)
            .parentId(parentId)
            .build();

        try {
            CommentRepository.createComment(comment);
        } catch (SQLException e) {
            throw new RuntimeException("failed to create comment", e);
        }

        response.sendRedirect("post?id=" + postId);
    }
}
package org.pknudev.servlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.pknudev.model.Post;
import org.pknudev.model.User;
import org.pknudev.repository.AttachmentRepository;
import org.pknudev.repository.CommentRepository;
import org.pknudev.repository.PostRepository;

@WebServlet(name = "postDelete", value = "/deletePost")
public class PostDeleteServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = Helper.getLoginUser(request, response);
        if (user == null) {
            return;
        }

        int postId = Helper.getIntegerParameter(request, "id", true);

        Post post;
        try {
            post = PostRepository.getPost(postId);
        } catch (SQLException e) {
            throw new RuntimeException("failed to get post", e);
        }
        if (post == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "post not found");
            return;
        }
        if (!post.getAuthorEmail().equals(user.getEmail())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "post is not owned by the current user");
            return;
        }

        try {
            PostRepository.deletePost(postId);
            AttachmentRepository.deletePostAttachments(postId);
            CommentRepository.deletePostComments(postId);
        } catch (SQLException e) {
            throw new RuntimeException("failed to delete post", e);
        }

        response.sendRedirect("/posts?type=" + post.getType());
    }
}
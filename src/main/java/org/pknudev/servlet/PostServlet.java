package org.pknudev.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.pknudev.model.Attachment;
import org.pknudev.model.Comment;
import org.pknudev.model.Post;
import org.pknudev.repository.AttachmentRepository;
import org.pknudev.repository.CommentRepository;
import org.pknudev.repository.PostRepository;

@WebServlet(name = "post", value = "/post")
public class PostServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int postId = Helper.getIntegerParameter(request, "id", true);

        Post post;
        try {
            post = PostRepository.getPost(postId);
        } catch (SQLException e) {
            throw new RuntimeException("failed to get post", e);
        }
        if (post == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        List<Attachment> attachments;
        try {
            attachments = AttachmentRepository.getPostAttachments(postId);
        } catch (SQLException e) {
            throw new RuntimeException("failed to get post attachments", e);
        }

        List<Comment> comments;
        try {
            comments = CommentRepository.getPostComments(postId);
        } catch (SQLException e) {
            throw new RuntimeException("failed to get post comments", e);
        }

        boolean showComments = comments.stream().anyMatch(Comment::visible);

        request.setAttribute("post", post);
        request.setAttribute("attachments", attachments);
        request.setAttribute("comments", comments);
        request.setAttribute("showComments", showComments);
        request.getRequestDispatcher("/WEB-INF/jsp/post.jsp").forward(request, response);
    }
}
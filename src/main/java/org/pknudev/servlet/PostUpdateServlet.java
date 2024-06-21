package org.pknudev.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.pknudev.model.Attachment;
import org.pknudev.model.Category;
import org.pknudev.model.Post;
import org.pknudev.model.User;
import org.pknudev.repository.AttachmentRepository;
import org.pknudev.repository.CategoryRepository;
import org.pknudev.repository.PostRepository;

@WebServlet(name = "postUpdate", value = "/updatePost")
@MultipartConfig(
    maxFileSize = 2 * 1024 * 1024 // 2MiB
)
public class PostUpdateServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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

        List<Attachment> attachments;
        try {
            attachments = AttachmentRepository.getPostAttachments(postId);
        } catch (SQLException e) {
            throw new RuntimeException("failed to get post attachments", e);
        }

        List<Category> categories;
        try {
            categories = CategoryRepository.getCategories();
        } catch (SQLException e) {
            throw new RuntimeException("failed to get categories", e);
        }

        request.setAttribute("post", post);
        request.setAttribute("attachments", attachments);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/jsp/post_update.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = Helper.getLoginUser(request, response);
        if (user == null) {
            return;
        }

        int postId = Helper.getIntegerParameter(request, "id", true);
        Integer itemCategoryId = Helper.getIntegerParameter(request, "item_category_id", false);
        String itemName = Helper.getStringParameter(request, "item_name", false);
        String itemLocation = Helper.getStringParameter(request, "item_location", false);

        Date itemDate;
        try {
            // TODO: check frontend code
            itemDate = Helper.getDateParameter(request, "item_date", "yyyy-MM-dd'T'HH:mm", false);
        } catch (ParseException e) {
            throw new ServletException("failed to parse item_date", e);
        }
        Timestamp itemDateTimestamp = null;
        if (itemDate != null) {
            itemDateTimestamp = new Timestamp(itemDate.getTime());
        }

        Part filePart = request.getPart("file");
        if (filePart != null && !filePart.getSubmittedFileName().isEmpty()) {
            if (!filePart.getContentType().startsWith("image/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "only image files are allowed");
                return;
            }
        } else {
            filePart = null;
        }

        String content = Helper.getStringParameter(request, "content", true);

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

        post.setItemCategoryId(itemCategoryId);
        post.setItemName(itemName);
        post.setItemLocation(itemLocation);
        post.setItemDate(itemDateTimestamp);
        post.setContent(content);
        try {
            PostRepository.updatePost(post);
        } catch (SQLException e) {
            throw new RuntimeException("failed to update post", e);
        }

//        List<Attachment> attachments;
//        try {
//            attachments = AttachmentRepository.getPostAttachments(postId);
//        } catch (SQLException e) {
//            throw new RuntimeException("failed to get post attachments", e);
//        }
//
//        if (attachments.isEmpty() && filePart != null) { // new upload
//            try (InputStream inputStream = filePart.getInputStream()) {
//                Attachment attachment = Attachment.builder()
//                    .id(UUID.randomUUID().toString())
//                    .postId(postId)
//                    .build();
//                try {
//                    AttachmentRepository.createAttachment(attachment, inputStream);
//                } catch (SQLException e) {
//                    throw new RuntimeException("failed to create attachment", e);
//                }
//            }
//        } else if (!attachments.isEmpty() && filePart == null) { // delete file
//            try {
//                AttachmentRepository.deleteAttachment(attachments.get(0).getId());
//            } catch (SQLException e) {
//                throw new RuntimeException("failed to delete attachment", e);
//            }
//        }

        response.sendRedirect("/post?id=" + postId);
    }
}
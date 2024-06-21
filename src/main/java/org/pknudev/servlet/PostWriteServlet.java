package org.pknudev.servlet;

import java.io.IOException;
import java.nio.file.Paths;
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

import org.pknudev.common.Config;
import org.pknudev.model.Attachment;
import org.pknudev.model.Category;
import org.pknudev.model.Post;
import org.pknudev.model.User;
import org.pknudev.repository.AttachmentRepository;
import org.pknudev.repository.CategoryRepository;
import org.pknudev.repository.PostRepository;

@WebServlet(name = "postWrite", value = "/writePost")
@MultipartConfig(
    maxFileSize = 2 * 1024 * 1024 // 2MiB
)
public class PostWriteServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = Helper.getLoginUser(request, response);
        if (user == null) {
            return;
        }

        List<Category> categories;
        try {
            categories = CategoryRepository.getCategories();
        } catch (SQLException e) {
            throw new RuntimeException("failed to get categories", e);
        }

        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/jsp/post_write.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = Helper.getLoginUser(request, response);
        if (user == null) {
            return;
        }

        int type = Helper.getIntegerParameter(request, "type", true);
        if (!Post.isValidType(type)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid post type");
            return;
        }
        Integer itemCategoryId = Helper.getIntegerParameter(request, "item_category_id", false);
        String itemName = Helper.getStringParameter(request, "item_name", true);
        String itemLocation = Helper.getStringParameter(request, "item_location", true);

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

        Post post = Post.builder()
            .type(type)
            .authorEmail(user.getEmail())
            .itemCategoryId(itemCategoryId)
            .itemName(itemName)
            .itemLocation(itemLocation)
            .itemDate(itemDateTimestamp)
            .content(content)
            .build();

        int postId;
        try {
            postId = PostRepository.createPost(post);
        } catch (SQLException e) {
            throw new RuntimeException("failed to create post", e);
        }

        if (filePart != null) {
            String fileId = UUID.randomUUID().toString();
            filePart.write(Paths.get(Config.getProperty("FILE_UPLOAD_DIR"), fileId).toString());
            Attachment attachment = Attachment.builder()
                .id(fileId)
                .postId(postId)
                .build();
            try {
                AttachmentRepository.createAttachment(attachment);
            } catch (SQLException e) {
                throw new RuntimeException("failed to create attachment", e);
            }
        }

        response.sendRedirect("/post?id=" + postId);
    }
}
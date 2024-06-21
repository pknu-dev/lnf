package org.pknudev.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.pknudev.common.Config;
import org.pknudev.model.Attachment;
import org.pknudev.repository.AttachmentRepository;

@WebServlet(name = "attachment", value = "/attachment")
public class AttachmentServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = Helper.getStringParameter(request, "id", true);

        Attachment attachment;
        try {
            attachment = AttachmentRepository.getAttachment(id);
        } catch (SQLException e) {
            throw new RuntimeException("failed to get attachment", e);
        }
        if (attachment == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "attachment not found");
            return;
        }

        File file = new File(Paths.get(Config.getProperty("FILE_UPLOAD_DIR"), id).toString());
        response.setContentType(getServletContext().getMimeType(file.getName()));
        response.setContentLength((int) file.length());
        try (FileInputStream in = new FileInputStream(file)) {
            OutputStream out = response.getOutputStream();
            in.transferTo(out);
        }
    }
}
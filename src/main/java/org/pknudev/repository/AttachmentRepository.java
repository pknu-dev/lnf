package org.pknudev.repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.pknudev.model.Attachment;

public class AttachmentRepository extends BaseRepository {
    public static List<Attachment> getPostAttachments(int postId) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                "SELECT ID, POST_ID, CREATED_AT FROM ATTACHMENTS WHERE POST_ID = ?");
            stmt.setInt(1, postId);
            rs = stmt.executeQuery();
            List<Attachment> attachments = new ArrayList<>();
            while (rs.next()) {
                attachments.add(Attachment.builder()
                    .id(rs.getString("ID"))
                    .postId(rs.getInt("POST_ID"))
                    .createdAt(rs.getTimestamp("CREATED_AT"))
                    .build()
                );
            }
            return attachments;
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static Attachment getAttachment(String id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                "SELECT ID, POST_ID, CREATED_AT FROM ATTACHMENTS WHERE id = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return Attachment.builder()
                .id(rs.getString("ID"))
                .postId(rs.getInt("POST_ID"))
                .createdAt(rs.getTimestamp("CREATED_AT"))
                .build();
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static void createAttachment(Attachment attachment) throws SQLException, IOException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(
                "INSERT INTO ATTACHMENTS (ID, POST_ID) VALUES (?, ?)");
            stmt.setString(1, attachment.getId());
            stmt.setInt(2, attachment.getPostId());
            stmt.executeUpdate();
        } finally {
            cleanup(stmt, null);
        }
    }

    public static void deletePostAttachments(int postId) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("DELETE FROM ATTACHMENTS WHERE POST_ID = ?");
            stmt.setInt(1, postId);
            stmt.executeUpdate();
        } finally {
            cleanup(stmt, null);
        }
    }

    public static void deleteAttachment(String id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("DELETE FROM ATTACHMENTS WHERE ID = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();
        } finally {
            cleanup(stmt, null);
        }
    }
}

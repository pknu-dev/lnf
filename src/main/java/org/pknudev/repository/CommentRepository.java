package org.pknudev.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pknudev.model.Comment;
import org.pknudev.model.User;

public class CommentRepository extends BaseRepository {
    public static List<Comment> getPostComments(int postId) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                "SELECT id, post_id, author_email, content, parent_id, c.created_at, updated_at, deleted_at, " +
                    "u.nickname author_nickname " +
                    "FROM comments c " +
                    "LEFT JOIN users u ON u.email = author_email " +
                    "WHERE post_id = ? " +
                    "ORDER BY id");
            stmt.setInt(1, postId);
            rs = stmt.executeQuery();

            List<Comment> comments = new ArrayList<>();
            Map<Integer, Comment> commentMap = new HashMap<>();
            while (rs.next()) {
                User author = User.builder()
                    .nickname(rs.getString("author_nickname"))
                    .build();

                Integer parentId = rs.getInt("parent_id");
                if (rs.wasNull()) {
                    parentId = null;
                }

                Comment comment = Comment.builder()
                    .id(rs.getInt("id"))
                    .postId(rs.getInt("post_id"))
                    .authorEmail(rs.getString("author_email"))
                    .content(rs.getString("content"))
                    .parentId(parentId)
                    .createdAt(rs.getTimestamp("created_at"))
                    .updatedAt(rs.getTimestamp("updated_at"))
                    .deletedAt(rs.getTimestamp("deleted_at"))
                    .author(author)
                    .build();
                commentMap.put(comment.getId(), comment);

                if (parentId == null) {
                    comments.add(comment);
                }
            }
            commentMap.values().forEach(comment -> {
                if (comment.getParentId() != null) {
                    Comment parent = commentMap.get(comment.getParentId());
                    List<Comment> children = parent.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                    }
                    children.add(comment);
                    parent.setChildren(children);
                }
            });

            return comments;
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static Comment getComment(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                "SELECT id, post_id, author_email, content, parent_id, c.created_at, updated_at, deleted_at, " +
                    "u.nickname author_nickname " +
                    "FROM comments c " +
                    "LEFT JOIN users u ON u.email = author_email " +
                    "WHERE id = ?"
            );
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            User author = User.builder()
                .nickname(rs.getString("author_nickname"))
                .build();

            Integer parentId = rs.getInt("parent_id");
            if (rs.wasNull()) {
                parentId = null;
            }

            return Comment.builder()
                .id(rs.getInt("id"))
                .postId(rs.getInt("post_id"))
                .authorEmail(rs.getString("author_email"))
                .content(rs.getString("content"))
                .parentId(parentId)
                .createdAt(rs.getTimestamp("created_at"))
                .updatedAt(rs.getTimestamp("updated_at"))
                .deletedAt(rs.getTimestamp("deleted_at"))
                .author(author)
                .build();
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static void createComment(Comment comment) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                "INSERT INTO comments " +
                    "(post_id, author_email, content, parent_id) " +
                    "VALUES (?, ?, ?, ?)"
            );
            stmt.setInt(1, comment.getPostId());
            stmt.setString(2, comment.getAuthorEmail());
            stmt.setString(3, comment.getContent());
            if (comment.getParentId() != null) {
                stmt.setInt(4, comment.getParentId());
            } else {
                stmt.setNull(4, Types.NULL);
            }
            stmt.executeUpdate();
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static void deleteComment(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(
                "UPDATE comments SET deleted_at = SYSDATE WHERE id = ? AND deleted_at IS NULL");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } finally {
            cleanup(stmt, null);
        }
    }

    public static void deletePostComments(int postId) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(
                "DELETE FROM COMMENTS WHERE POST_ID = ?");
            stmt.setInt(1, postId);
            stmt.executeUpdate();
        } finally {
            cleanup(stmt, null);
        }
    }
}

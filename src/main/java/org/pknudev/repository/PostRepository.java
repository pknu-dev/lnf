package org.pknudev.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import org.pknudev.model.Attachment;
import org.pknudev.model.Category;
import org.pknudev.model.Post;
import org.pknudev.model.User;

public class PostRepository extends BaseRepository {
    public static List<Post> getPosts() throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                "SELECT p.id, type, done, title, author_email, item_category_id, " +
                    "item_name, item_date, item_location, content, p.created_at, updated_at, " +
                    "u.nickname author_nickname, c.name item_category_name, " +
                    "(SELECT COUNT(*) FROM COMMENTS WHERE POST_ID = p.ID AND DELETED_AT IS NULL) NUM_COMMENTS " +
                    "FROM posts p " +
                    "LEFT JOIN users u ON u.email = author_email " +
                    "LEFT JOIN categories c ON c.id = ITEM_CATEGORY_ID " +
                    "ORDER BY p.id DESC"
            );
            rs = stmt.executeQuery();

            List<Post> posts = new ArrayList<>();
            while (rs.next()) {
                User author = User.builder()
                    .email(rs.getString("author_email"))
                    .nickname(rs.getString("author_nickname"))
                    .build();

                Integer itemCategoryId = rs.getInt("item_category_id");
                if (rs.wasNull()) {
                    itemCategoryId = null;
                }

                Category itemCategory = null;
                if (itemCategoryId != null) {
                    itemCategory = Category.builder()
                        .name(rs.getString("item_category_name"))
                        .build();
                }

                int postId = rs.getInt("id");
                List<Attachment> attachments = AttachmentRepository.getPostAttachments(postId);

                posts.add(Post.builder()
                    .id(postId)
                    .type(rs.getInt("type"))
                    .done(rs.getBoolean("done"))
                    .title(rs.getString("title"))
                    .authorEmail(rs.getString("author_email"))
                    .itemCategoryId(itemCategoryId)
                    .itemName(rs.getString("item_name"))
                    .itemDate(rs.getTimestamp("item_date"))
                    .itemLocation(rs.getString("item_location"))
                    .content(rs.getString("content"))
                    .createdAt(rs.getTimestamp("created_at"))
                    .updatedAt(rs.getTimestamp("updated_at"))
                    .author(author)
                    .itemCategory(itemCategory)
                    .numComments(rs.getInt("NUM_COMMENTS"))
                    .attachments(attachments)
                    .build()
                );
            }

            return posts;
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static List<Post> getPostsByType(int type, int limit) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                "SELECT * FROM (" +
                    "SELECT p.id, type, done, title, author_email, item_category_id, " +
                    "item_name, item_date, item_location, content, p.created_at, updated_at, " +
                    "u.nickname author_nickname, c.name item_category_name, " +
                    "(SELECT COUNT(*) FROM COMMENTS WHERE POST_ID = p.ID AND DELETED_AT IS NULL) NUM_COMMENTS " +
                    "FROM posts p " +
                    "LEFT JOIN users u ON u.email = author_email " +
                    "LEFT JOIN categories c ON c.id = ITEM_CATEGORY_ID " +
                    "WHERE TYPE = ? " +
                    "ORDER BY p.id DESC" +
                    ") " +
                    "WHERE ROWNUM <= ?"); // TODO: need pagination
            stmt.setInt(1, type);
            stmt.setInt(2, limit);
            rs = stmt.executeQuery();

            List<Post> posts = new ArrayList<>();
            while (rs.next()) {
                User author = User.builder()
                    .email(rs.getString("author_email"))
                    .nickname(rs.getString("author_nickname"))
                    .build();

                Integer itemCategoryId = rs.getInt("item_category_id");
                if (rs.wasNull()) {
                    itemCategoryId = null;
                }

                Category itemCategory = null;
                if (itemCategoryId != null) {
                    itemCategory = Category.builder()
                        .name(rs.getString("item_category_name"))
                        .build();
                }

                int postId = rs.getInt("id");
                List<Attachment> attachments = AttachmentRepository.getPostAttachments(postId);

                posts.add(Post.builder()
                    .id(postId)
                    .type(rs.getInt("type"))
                    .done(rs.getBoolean("done"))
                    .title(rs.getString("title"))
                    .authorEmail(rs.getString("author_email"))
                    .itemCategoryId(itemCategoryId)
                    .itemName(rs.getString("item_name"))
                    .itemDate(rs.getTimestamp("item_date"))
                    .itemLocation(rs.getString("item_location"))
                    .content(rs.getString("content"))
                    .createdAt(rs.getTimestamp("created_at"))
                    .updatedAt(rs.getTimestamp("updated_at"))
                    .author(author)
                    .itemCategory(itemCategory)
                    .numComments(rs.getInt("NUM_COMMENTS"))
                    .attachments(attachments)
                    .build()
                );
            }

            return posts;
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static List<Post> searchPosts(String query, int type) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                "SELECT P.ID, TYPE, DONE, TITLE, AUTHOR_EMAIL, ITEM_CATEGORY_ID, " +
                    "ITEM_NAME, ITEM_DATE, ITEM_LOCATION, CONTENT, P.CREATED_AT, UPDATED_AT, " +
                    "U.NICKNAME AUTHOR_NICKNAME, C.NAME ITEM_CATEGORY_NAME, " +
                    "(SELECT COUNT(*) FROM COMMENTS WHERE POST_ID = P.ID AND DELETED_AT IS NULL) NUM_COMMENTS " +
                    "FROM POSTS P " +
                    "LEFT JOIN USERS U ON U.EMAIL = AUTHOR_EMAIL " +
                    "LEFT JOIN CATEGORIES C ON C.ID = ITEM_CATEGORY_ID " +
                    "WHERE (ITEM_NAME LIKE ? OR C.NAME LIKE ? OR ITEM_LOCATION LIKE ? OR CONTENT LIKE ?) " +
                    "AND (? = 0 OR TYPE = ?)"
            );
            String likeQuery = "%" + query + "%";
            stmt.setString(1, likeQuery);
            stmt.setString(2, likeQuery);
            stmt.setString(3, likeQuery);
            stmt.setString(4, likeQuery);
            stmt.setInt(5, type);
            stmt.setInt(6, type);
            rs = stmt.executeQuery();

            List<Post> posts = new ArrayList<>();
            while (rs.next()) {
                User author = User.builder()
                    .email(rs.getString("author_email"))
                    .nickname(rs.getString("author_nickname"))
                    .build();

                Integer itemCategoryId = rs.getInt("item_category_id");
                if (rs.wasNull()) {
                    itemCategoryId = null;
                }

                Category itemCategory = null;
                if (itemCategoryId != null) {
                    itemCategory = Category.builder()
                        .name(rs.getString("item_category_name"))
                        .build();
                }

                int postId = rs.getInt("id");
                List<Attachment> attachments = AttachmentRepository.getPostAttachments(postId);

                posts.add(Post.builder()
                    .id(postId)
                    .type(rs.getInt("type"))
                    .done(rs.getBoolean("done"))
                    .title(rs.getString("title"))
                    .authorEmail(rs.getString("author_email"))
                    .itemCategoryId(itemCategoryId)
                    .itemName(rs.getString("item_name"))
                    .itemDate(rs.getTimestamp("item_date"))
                    .itemLocation(rs.getString("item_location"))
                    .content(rs.getString("content"))
                    .createdAt(rs.getTimestamp("created_at"))
                    .updatedAt(rs.getTimestamp("updated_at"))
                    .author(author)
                    .itemCategory(itemCategory)
                    .numComments(rs.getInt("NUM_COMMENTS"))
                    .attachments(attachments)
                    .build()
                );
            }

            return posts;
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static Post getPost(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                "SELECT p.id, type, done, title, author_email, item_category_id, " +
                    "item_name, item_date, item_location, content, p.created_at, updated_at, " +
                    "u.nickname author_nickname, c.name item_category_name, " +
                    "(SELECT COUNT(*) FROM COMMENTS WHERE POST_ID = P.ID AND DELETED_AT IS NULL) NUM_COMMENTS " +
                    "FROM POSTS P " +
                    "LEFT JOIN users u ON u.email = author_email " +
                    "LEFT JOIN categories c ON c.id = ITEM_CATEGORY_ID " +
                    "WHERE p.id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            User author = User.builder()
                .email(rs.getString("author_email"))
                .nickname(rs.getString("author_nickname"))
                .build();

            Integer itemCategoryId = rs.getInt("item_category_id");
            if (rs.wasNull()) {
                itemCategoryId = null;
            }

            Category itemCategory = null;
            if (itemCategoryId != null) {
                itemCategory = Category.builder()
                    .name(rs.getString("item_category_name"))
                    .build();
            }

            int postId = rs.getInt("id");
            List<Attachment> attachments = AttachmentRepository.getPostAttachments(postId);

            return Post.builder()
                .id(postId)
                .type(rs.getInt("type"))
                .done(rs.getBoolean("done"))
                .title(rs.getString("title"))
                .authorEmail(rs.getString("author_email"))
                .itemCategoryId(itemCategoryId)
                .itemName(rs.getString("item_name"))
                .itemDate(rs.getTimestamp("item_date"))
                .itemLocation(rs.getString("item_location"))
                .content(rs.getString("content"))
                .createdAt(rs.getTimestamp("created_at"))
                .updatedAt(rs.getTimestamp("updated_at"))
                .author(author)
                .itemCategory(itemCategory)
                .numComments(rs.getInt("NUM_COMMENTS"))
                .attachments(attachments)
                .build();
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static int createPost(Post post) throws SQLException {
        Connection conn = getConnection();
        OraclePreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = (OraclePreparedStatement) conn.prepareStatement(
                "INSERT INTO posts (" +
                    "type, title, author_email, item_category_id, item_name, item_date, item_location, content) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                    "RETURNING ID INTO ?"
            );
            stmt.setInt(1, post.getType());
            stmt.setString(2, post.getTitle());
            stmt.setString(3, post.getAuthorEmail());
            if (post.getItemCategoryId() != null) {
                stmt.setInt(4, post.getItemCategoryId());
            } else {
                stmt.setNull(4, Types.NULL);
            }
            stmt.setString(5, post.getItemName());
            stmt.setTimestamp(6, post.getItemDate());
            stmt.setString(7, post.getItemLocation());
            stmt.setString(8, post.getContent());
            stmt.registerReturnParameter(9, OracleTypes.NUMBER);
            stmt.executeUpdate();
            rs = stmt.getReturnResultSet();
            if (!rs.next()) {
                throw new RuntimeException("failed to get inserted post id");
            }
            return rs.getInt(1);
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static void updatePost(Post post) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                "UPDATE posts SET " +
                    "DONE = ?, " +
                    "ITEM_CATEGORY_ID = ?, " +
                    "ITEM_NAME = ?, " +
                    "ITEM_DATE = ?, " +
                    "ITEM_LOCATION = ?, " +
                    "CONTENT = ? " +
                    "WHERE ID = ?"
            );
            stmt.setBoolean(1, post.isDone());
            if (post.getItemCategoryId() != null) {
                stmt.setInt(2, post.getItemCategoryId());
            } else {
                stmt.setNull(2, Types.NULL);
            }
            stmt.setString(3, post.getItemName());
            stmt.setTimestamp(4, post.getItemDate());
            stmt.setString(5, post.getItemLocation());
            stmt.setString(6, post.getContent());
            stmt.setInt(7, post.getId());
            stmt.executeUpdate();
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static void deletePost(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("DELETE FROM POSTS WHERE ID = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } finally {
            cleanup(stmt, null);
        }
    }
}

package org.pknudev.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.pknudev.model.User;

public class UserRepository extends BaseRepository {
    public static User getUser(String email) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                "SELECT email, password_hash, nickname, created_at FROM users WHERE email = ?");
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return User.builder()
                .email(email)
                .passwordHash(rs.getString("password_hash"))
                .nickname(rs.getString("nickname"))
                .createdAt(rs.getTimestamp("created_at"))
                .build();
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static User getUserByNickname(String nickname) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // TODO: create unique index
            stmt = conn.prepareStatement(
                "SELECT email, password_hash, nickname, created_at FROM users WHERE nickname = ?");
            stmt.setString(1, nickname);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return User.builder()
                .email(rs.getString("email"))
                .passwordHash(rs.getString("password_hash"))
                .nickname(nickname)
                .createdAt(rs.getTimestamp("created_at"))
                .build();
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static void createUser(User user) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(
                "INSERT INTO users (email, password_hash, nickname) VALUES (?, ?, ?)"
            );
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getNickname());
            stmt.executeUpdate();
        } finally {
            cleanup(stmt, null);
        }
    }
}

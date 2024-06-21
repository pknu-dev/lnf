package org.pknudev.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.pknudev.model.EmailVerification;

public class EmailVerificationRepository extends BaseRepository {
    public static EmailVerification getEmailVerification(String code) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                "SELECT code, email, created_at, expire_at FROM email_verifications WHERE code = ?"
            );
            stmt.setString(1, code);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return EmailVerification.builder()
                .code(code)
                .email(rs.getString("email"))
                .createdAt(rs.getTimestamp("created_at"))
                .expireAt(rs.getTimestamp("expire_at"))
                .build();
        } finally {
            cleanup(stmt, rs);
        }
    }

    public static void deleteExpiredEmailVerifications() throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("DELETE FROM email_verifications WHERE expire_at <= SYSDATE");
            stmt.executeUpdate();
        } finally {
            cleanup(stmt, null);
        }
    }

    public static void createEmailVerification(EmailVerification emailVerification) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(
                "INSERT INTO email_verifications (code, email) VALUES (?, ?)"
            );
            stmt.setString(1, emailVerification.getCode());
            stmt.setString(2, emailVerification.getEmail());
            stmt.executeUpdate();
        } finally {
            cleanup(stmt, null);
        }
    }
}

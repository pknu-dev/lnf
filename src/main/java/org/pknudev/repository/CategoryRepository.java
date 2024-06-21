package org.pknudev.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.pknudev.model.Category;

public class CategoryRepository extends BaseRepository {
    public static List<Category> getCategories() throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT id, name, \"order\" FROM categories ORDER BY \"order\"");
            rs = stmt.executeQuery();

            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(Category.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .order(rs.getInt("order"))
                    .build()
                );
            }
            return categories;
        } finally {
            cleanup(stmt, rs);
        }
    }
}

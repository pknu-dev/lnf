package org.pknudev.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Comment implements Serializable {
    private int id;
    private int postId;
    private String authorEmail;
    private String content;
    private Integer parentId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    private User author;
    private List<Comment> children;

    public boolean visible() {
        if (deletedAt == null) {
            return true;
        }
        if (children == null) {
            return false;
        }
        return children.stream().anyMatch(child -> child.deletedAt == null);
    }
}

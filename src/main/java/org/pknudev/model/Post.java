package org.pknudev.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Post implements Serializable {
    // Type constants
    public static final int LOST = 1;
    public static final int FOUND = 2;

    public static boolean isValidType(int type) {
        return type == LOST || type == FOUND;
    }

    private int id;
    private int type;
    private boolean done;
    private String title;
    private String authorEmail;
    private Integer itemCategoryId;
    private String itemName;
    private Timestamp itemDate;
    private String itemLocation;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private User author;
    private Category itemCategory;
    private int numComments;
    private List<Attachment> attachments;
}

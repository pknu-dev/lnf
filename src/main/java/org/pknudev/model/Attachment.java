package org.pknudev.model;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Attachment implements Serializable {
    private String id;
    private int postId;
    private Timestamp createdAt;
}

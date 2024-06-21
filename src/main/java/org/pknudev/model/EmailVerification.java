package org.pknudev.model;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailVerification implements Serializable {
    private String code;
    private String email;
    private Timestamp createdAt;
    private Timestamp expireAt;
}

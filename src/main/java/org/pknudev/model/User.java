package org.pknudev.model;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

import org.pknudev.common.Utility;

@Data
@Builder
public class User implements Serializable {
    private String email;
    private String passwordHash;
    private String nickname;
    private Timestamp createdAt;

    public boolean checkPassword(String password) {
        return Utility.sha256(password).equals(passwordHash);
    }
}
